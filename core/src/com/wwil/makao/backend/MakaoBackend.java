package com.wwil.makao.backend;

import com.wwil.makao.backend.events.Event;
import com.wwil.makao.backend.events.AttackPreviousPlayerEvent;

import java.util.*;

public class MakaoBackend {
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;
    private final CardValidator validator = new CardValidator(this);
    private DemandManager demand = new DemandManager(0, false, null);
    private final List<Card> humanPlayedCards = new ArrayList<>();
    private final List<Card> pullDeck = new ArrayList<>();
    private Event event;

    public MakaoBackend() {
        createCardsToGameDeck();
        stack.addCardToStack(getStartStackCard());
        createPlayers();
        startRound();
    }

    private void createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
    }

    private Card getStartStackCard() {
        for (Card card : gameDeck) {
            if (card.getRank().getAbility().equals(Ability.NONE)) {
                return card;
            }
        }
        return null;
    }

    private Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    private void createPlayers() {
        for (int i = 0; i < RuleParams.AMOUNT_OF_PLAYERS; i++) {
            Player player = new Player(giveCards(RuleParams.STARTING_CARDS));
            players.add(player);
        }
    }

    private List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    //////////// Logika:
    //Jedyna publiczna metoda (odbiera informacje, wykonuje działanie i wysyła)
    private void startRound() {
        roundReport = new RoundReport();
    }

    //todo: Pierwsza karta ratuje przy ciągniętych kart
    //todo: K PIK
    public RoundReport executeAction(Play play) {
        if (play.getAction() == Action.PULL) {
            roundReport.addPlayRaport(executePlay(play));
            return roundReport;
        }

        if (play.getAction() == Action.END) {
            playRound();
            RoundReport report = roundReport;
            startRound();
            return report;
        }

        //scenario for put:
        boolean isValid = validator.isValidCardForCurrentState(play.getCardPlayed());
        roundReport.addPlayRaport(
                new PlayReport(currentPlayer(), play)
                        .setCardCorrect(isValid));


        if (isValid) {
            putCard(play.getCardPlayed());
        }
        return roundReport;
    }


    private void playRound() {
        humanPlayedCards.clear();
        nextPlayer();
        ComputerPlayMaker playMaker = new ComputerPlayMaker(this,validator);
        for (int i = 1; i < players.size(); i++) {
            roundReport.addPlayRaport(executePlay(playMaker.create()));
            //Scenariusz: Obecny gracz zagrywa K PIK. Poprzedni gracz jest atakowany
            if(event != null){
                event.startEvent();
            }
            nextPlayer();
        }
    }
    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == RuleParams.AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    private void previousPlayer(){
        currentPlayerIndex--;
        if (currentPlayerIndex == 0) {
            currentPlayerIndex = RuleParams.AMOUNT_OF_PLAYERS;
        }
    }


    private PlayReport executePlay(Play play) {
        PlayReport playReport = new PlayReport(currentPlayer(), play);
        //Dobierz kartę
        if (currentPlayer().isAttack()) {
            drainPullDeck(playReport);
        } else if (play.getAction() == Action.PULL) {
            pull(playReport);
        }

        //Połóż kartę/karty
        if (play.getCardsPlayed() != null) {
            putCards(play);
        }

        roundReport.setBlockPullButton(true);
        return playReport.setCardCorrect(true);
    }

    private void drainPullDeck(PlayReport playReport) {
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        playReport.setCardsToPull(cardsToPull);
        currentPlayer().addCardsToHand(cardsToPull);
        pullDeck.clear();
        currentPlayer().setAttacker(null);
    }

    private void pull(PlayReport playReport) {
        Card drawn = takeCardFromGameDeck();
        currentPlayer().addCardToHand(drawn);
        //Try Rescue
        if (validator.isValidCardForCurrentState(drawn)) {
            playReport.getPlay().setCardsPlayed(Collections.singletonList(drawn));
        }
        playReport.setDrawn(drawn);
    }


    private void putCards(Play play) {
        for (Card card : play.getCardsPlayed()) {
            putCard(card);
        }
    }

    private void putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayer() == getHumanPlayer()) {
            humanPlayedCards.add(cardPlayed);
        }
        useCardAbility(cardPlayed);
    }

    private void useCardAbility(Card card) {
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                //useChangeSuitAbility();
                break;
            case PLUS_2:
                attack(getNextPlayer(), 2, card);
                break;
            case PLUS_3:
                attack(getNextPlayer(), 3, card); //ATAK
                break;
            case WAIT:
                //useWaitAbility(wildCard);
                break;
            case DEMAND:
                //useDemandAbility(wildCard); //ATAK
                break;
            case KING:
                chooseAbilityForKing(card); //ATAK
                break;
            case WILD_CARD:
                //useWildCard(); //PRAWDOPODOBNY ATAK
        }
    }


    private void attack(Player player, int amountOfCards, Card attackingCard) {
        pullDeck.addAll(giveCards(amountOfCards));
        player.setAttacker(new CardBattle(pullDeck, attackingCard));
        if(attackingCard.doesCardStartEvent()){
            event = new AttackPreviousPlayerEvent(playerBefore());
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(getNextPlayer(), 5, card);
                break;
            case SPADE:
                attack(playerBefore(), 5, card);
                break;
        }
    }

    protected Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    protected Player playerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    protected Player getNextPlayer() {
        int playerBeforeIndex = currentPlayerIndex + 1;
        if (playerBeforeIndex >= players.size()) {
            playerBeforeIndex = 0;
        }
        return players.get(playerBeforeIndex);
    }

    public Player getHumanPlayer() {
        return players.get(0);
    }

    public Stack getStack() {
        return stack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public DemandManager getDemand() {
        return demand;
    }

    public List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }
}
