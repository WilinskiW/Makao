package com.wwil.makao.backend;

import java.util.*;

public class MakaoBackend {
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<Player> players = new ArrayList<>();
    private RoundReport roundReport;
    private final CardValidator validator = new CardValidator(this);
    protected final List<Card> humanPlayedCards = new ArrayList<>();
    private final PlayExecutor playExecutor = new PlayExecutor(this);
    private final PlayMaker playMaker = new PlayMaker(this);
    private int currentPlayerIndex = 0;

    public MakaoBackend() {
        createCardsToGameDeck();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
        createPlayers();
        startRound();
    }

    //todo: Wyłączyć draga z wrzucaniem go do reportu i oddzielenie go od action
    //todo: Schemat aktywacji przycisków (Multi pull i single pull)
    //todo: Report mówi co powinno być aktywne (boolean putActive, boolean pullActive, boolean endActive)
    //todo: Animacje

    public RoundReport processHumanPlay(Play humanPlay) {
        switch (humanPlay.getAction()) {
            case PULL:
                return pullCard(humanPlay);
            case END:
                return endTurn(humanPlay);
            case PUT:
                return putCard(humanPlay);
            default:
                throw new IllegalArgumentException("Unsupported action");
        }
    }

    private RoundReport pullCard(Play humanPlay){
        humanPlay.setDrawnCard(takeCardFromGameDeck());
        roundReport.addPlayRaport(playExecutor.createPlayReport(currentPlayer(),humanPlay));
        return roundReport;
    }

    private RoundReport endTurn(Play humanPlay){
        humanPlayedCards.clear();
        roundReport.addPlayRaport(playExecutor.createPlayReport(currentPlayer(),humanPlay));
        playRound();
        return sendRoundReport();
    }

    private RoundReport putCard(Play humanPlay){
        boolean isValid = validator.isValid(humanPlay.getCardPlayed());
        roundReport.addPlayRaport(
                new PlayReport(getHumanPlayer(), humanPlay)
                        .setCardCorrect(isValid));

        if (isValid) {
            playExecutor.putCard(humanPlay.getCardPlayed());
        }
        return roundReport;
    }

    public boolean isDraggedCardValid(Card choosenCard){
        return validator.isValid(choosenCard);
    }

    private void startRound() {
        roundReport = new RoundReport();
    }


    private void playRound() {
        while (currentPlayer() != getHumanPlayer() || currentPlayer().checkIfPlayerHaveNoCards()) {
            addPlayReports(currentPlayer());
        }
    }

    private void addPlayReports(Player player) {
        List<Play> plays = playMaker.makePlays(player);
        for (Play play : plays) {
            roundReport.addPlayRaport(playExecutor.createPlayReport(player,play));
        }

    }

    private RoundReport sendRoundReport() {
        RoundReport report = roundReport;
        startRound();
        return report;
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

    protected Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    private void createPlayers() {
        for (int i = 0; i < RuleParams.AMOUNT_OF_PLAYERS; i++) {
            Player player = new Player(giveCards(RuleParams.STARTING_CARDS));
            players.add(player);
        }
    }

    protected List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    protected Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    protected void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex > players.size() - 1) {
            currentPlayerIndex = 0;
        }
    }

    protected void playerBefore() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size() - 1;
        }
    }

    protected Player getPlayerBefore() {
        int playerBeforeIndex = currentPlayerIndex - 1;
        if (playerBeforeIndex < 0) {
            playerBeforeIndex = players.size() - 1;
        }
        return players.get(playerBeforeIndex);
    }

    Player getNextPlayer() {
        int nextPlayerIndex = currentPlayerIndex + 1;
        if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return players.get(nextPlayerIndex);
    }

    CardValidator getValidator() {
        return validator;
    }

    Player getHumanPlayer() {
        return players.get(0);
    }

    List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }

    public Stack getStack() {
        return stack;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public PlayMaker getPlayMaker() {
        return playMaker;
    }
}
