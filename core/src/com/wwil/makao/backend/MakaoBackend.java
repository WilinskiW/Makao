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
    private int currentPlayerIndex = 0;
    private Event event = new DefaultEvent(this);

    public MakaoBackend() {
        createCardsToGameDeck();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
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

    //////////// Logika:
    //Jedyna publiczna metoda (odbiera informacje, wykonuje działanie i wysyła)
    private void startRound() {
        roundReport = new RoundReport();
    }

    public RoundReport processHumanPlay(Play humanPlay) {
        if (humanPlay.getAction() == Action.PULL) {
            PlayReport humanReport = playExecutor.createReport(humanPlay);
            roundReport.addPlayRaport(humanReport);
            return roundReport;
        }

        if (humanPlay.getAction() == Action.END) {
            humanPlayedCards.clear();
            event.start();
            return endTurn();
        }

        //scenario for put:
        boolean isValid = validator.isValidCardForCurrentState(humanPlay.getCardPlayed());
        roundReport.addPlayRaport(
                new PlayReport(getHumanPlayer(), humanPlay)
                        .setCardCorrect(isValid));


        if (isValid) {
            playExecutor.putCard(humanPlay.getCardPlayed());
        }
        return roundReport;
    }

    private RoundReport endTurn() {
        playRound();
        RoundReport report = roundReport;
        startRound();
        return report;
    }

    private void playRound() {
        while (currentPlayer() != getHumanPlayer() || currentPlayer().checkIfPlayerHaveNoCards()) {
            roundReport.addPlayRaport(playExecutor.createReport(event.response()));
            event.start();
        }
    }

    void playerBefore() {
        currentPlayerIndex--;
        if (currentPlayerIndex < 0) {
            currentPlayerIndex = RuleParams.AMOUNT_OF_PLAYERS - 1;
        }
    }

    void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= RuleParams.AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    void setEvent(Event event){
        this.event = event;
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
        int playerBeforeIndex = currentPlayerIndex + 1;
        if (playerBeforeIndex >= players.size()) {
            playerBeforeIndex = 0;
        }
        return players.get(playerBeforeIndex);
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
