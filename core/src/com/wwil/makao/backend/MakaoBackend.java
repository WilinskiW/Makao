package com.wwil.makao.backend;

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
    final PlayMaker playMaker = new PlayMaker(this);
    private Event event;

    public MakaoBackend() {
        createCardsToGameDeck();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE,Suit.SPADE));
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
        while (currentPlayer() != getHumanPlayer()){
            checkEvent();
            roundReport.addPlayRaport(executePlay(playMaker.generate()));
            //Scenariusz: Obecny gracz zagrywa K PIK. Poprzedni gracz jest atakowany
            nextPlayer();
        }
    }

    void playerBefore() {
        currentPlayerIndex--;
        if (currentPlayerIndex == 0) {
            currentPlayerIndex = RuleParams.AMOUNT_OF_PLAYERS-1;
        }
    }

    void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == RuleParams.AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    PlayReport executePlay(Play play) {
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
        if(attackingCard.doesStartEvent()){
            event = new AttackPreviousPlayerEvent(this);
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(getNextPlayer(), 5, card);
                break;
            case SPADE:
                attack(getPlayerBefore(), 5, card);
                break;
        }
    }

    public Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Player getPlayerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    public Player getNextPlayer() {
        int playerBeforeIndex = currentPlayerIndex + 1;
        if (playerBeforeIndex >= players.size()) {
            playerBeforeIndex = 0;
        }
        return players.get(playerBeforeIndex);
    }
    private void checkEvent(){
        if(event != null){
            event.startEvent();
            event = null;
        }
    }

    boolean isEventActive(){
        return event != null;
    }

    CardValidator getValidator() {
        return validator;
    }

    public RoundReport getRoundReport() {
        return roundReport;
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
