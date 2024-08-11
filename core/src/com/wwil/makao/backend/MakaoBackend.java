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
        stack.addCardToStack(getStartStackCard());
        //stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
        createPlayers();
        startRound();
    }

    public RoundReport processHumanPlay(Play humanPlay) {
        if(humanPlay.getAction() == Action.DRAG && roundReport.haveManyPlayReports()){
            deleteUnnecessaryDrags();
        }

        if (humanPlay.getAction() == Action.PULL) {
            roundReport.addPlayRaport(playExecutor.createPlayReport(humanPlay));
            return roundReport;
        }

        //Gracz decyduje o końcu tury
        if (humanPlay.getAction() == Action.END) {
            humanPlayedCards.clear();
            return endTurn();
        }

        //scenario for put:
        boolean isValid = validator.isValid(humanPlay.getCardPlayed());
        roundReport.addPlayRaport(
                new PlayReport(getHumanPlayer(), humanPlay)
                        .setCardCorrect(isValid));

        if (isValid && !humanPlay.isDragging()) {
            playExecutor.putCard(humanPlay.getCardPlayed());
        }
        return roundReport;
    }

    private void deleteUnnecessaryDrags(){
        PlayReport dragReport = roundReport.getLastPlayReport();
        roundReport.getPlayReports().removeIf(playReport -> playReport.getPlay().getAction() == Action.DRAG);
        roundReport.addPlayRaport(dragReport);
    }

    private void startRound() {
        roundReport = new RoundReport();
    }

    private RoundReport endTurn() {
        nextPlayer();
        playRound();
        return sendRoundReport();
    }

    private void playRound() {
        while (currentPlayer() != getHumanPlayer() || currentPlayer().checkIfPlayerHaveNoCards()) {
            addPlayReports();
            nextPlayer();
        }
    }

    private void addPlayReports(){
        List<Play> plays = playMaker.makePlays(currentPlayer());
        for(Play play : plays){
            roundReport.addPlayRaport(playExecutor.createPlayReport(play));
        }
    }

    private RoundReport sendRoundReport(){
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

    void playerBefore() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = players.size()-1;
        }
    }

    void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex > players.size()-1) {
            currentPlayerIndex = 0;
        }
    }
    Player currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    Player getPlayerBefore() {
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
}
