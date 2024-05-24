package com.wwil.makao.backend;

import java.util.*;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;
    private final CardValidator validator = new CardValidator(this);
    private DemandManager demand = new DemandManager(0, false, null);
    private final List<Card> humanPlayedCards = new ArrayList<>();
    private final List<Card> pullDeck = new ArrayList<>();

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
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            Player player = new Player(giveCards(STARTING_CARDS));
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
        for (int i = 1; i < players.size(); i++) {
            roundReport.addPlayRaport(executePlay(createComputerPlay()));
            nextPlayer();
        }
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    private Play createComputerPlay() {
        Play play = new Play();
        if (currentPlayer().isAttack()) {
            handleDefense(play);
        }

        if(play.getCardsPlayed() == null) {
            handlePlay(play);
        }
        return play;
    }

    private void handleDefense(Play play){
        List<Card> defensiveCards = currentPlayer().findDefensiveCards(currentPlayer().getAttacker());
        if (!defensiveCards.isEmpty()) {
            getNextPlayer().setAttacker(currentPlayer().moveCardBattle());
            System.out.println("Gracz " + currentPlayerIndex + " broni sie :" + defensiveCards);
            play.setCardsPlayed(defensiveCards).setAction(Action.PUT);
        } else {
            play.setAction(Action.PULL);
        }
    }

    private void handlePlay(Play play){
        //Znajdź karty do zagrania
        List<Card> playableCards = getPlayableCards();
        if (!playableCards.isEmpty()) {
             play.setCardsPlayed(playableCards).setAction(Action.PUT);
        }
        else {
            //Dobierz kartę
            play.setAction(Action.PULL);
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
        //Scenariusz: Obecny gracz zagrywa K PIK. Poprzedni gracz jest atakowany

        roundReport.setBlockPullButton(true);
        return playReport.setCardCorrect(true);
    }

    private void drainPullDeck(PlayReport playReport) {
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        System.out.println("Gracz " + currentPlayerIndex + " ciagnie karty " + cardsToPull);
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
            System.out.println("Gracz " + currentPlayerIndex + " pierwsza karta ratuje!!!");
        }
        playReport.setDrawn(drawn);
    }


    private void putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayer() == getHumanPlayer()) {
            humanPlayedCards.add(cardPlayed);
        }
        useCardAbility(cardPlayed);
    }

    private void putCards(Play play) {
        for (Card card : play.getCardsPlayed()) {
            putCard(card);
        }
    }

    private List<Card> getPlayableCards() {
        List<Card> playableCards = new ArrayList<>();

        //Dodajemy karty, które mogą być zagrane
        for (Card card : currentPlayer().getCards()) {
            if (validator.isValidCardForCurrentState(card)) {
                playableCards.add(card);
            }
        }

        //Jeśli nie ma kart możliwych do zagrania, zwracamy pustą listę
        if (playableCards.isEmpty()) {
            return playableCards;
        }

        //Szukamy kart o tej samej randze
        List<Card> cardsWithSameRank = currentPlayer().getPlayableWithSameRank(playableCards);

        //Jeśli nie ma kart o tej samej randze, zwracamy listę kart możliwych do zagrania
        if (cardsWithSameRank.isEmpty()) {
            return Collections.singletonList(playableCards.get(new Random().nextInt(playableCards.size())));
        }
        return cardsWithSameRank;
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
        System.out.println("Gracz " + currentPlayerIndex + " atakuje Gracza: " + (currentPlayerIndex + 1));
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

    private Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    private Player playerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    private Player getNextPlayer() {
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
