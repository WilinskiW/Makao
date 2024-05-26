package com.wwil.makao.backend;

import com.wwil.makao.backend.events.Event;
import com.wwil.makao.backend.events.StandartTurn;

import java.util.*;

public class MakaoBackend {
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<Player> players = new ArrayList<>();
    private RoundReport roundReport;
    private final CardValidator validator = new CardValidator(this);
    protected final List<Card> humanPlayedCards = new ArrayList<>();
    private final ComputerPlayMaker computerPlayMaker = new ComputerPlayMaker(this);
    private final PlayExecutor reportCreator = new PlayExecutor(this);
    private int currentPlayerIndex = 0;
    private Event activeEvent = new StandartTurn(this);

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
            PlayReport humanReport = reportCreator.executePlay(humanPlay);
            roundReport.addPlayRaport(humanReport);
            return roundReport;
        }

        if (humanPlay.getAction() == Action.END) {
            humanPlayedCards.clear();
            return endTurn();
        }

        //scenario for put:
        boolean isValid = validator.isValidCardForCurrentState(humanPlay.getCardPlayed());
        roundReport.addPlayRaport(
                new PlayReport(getHumanPlayer(), humanPlay)
                        .setCardCorrect(isValid));


        if (isValid) {
            reportCreator.putCard(humanPlay.getCardPlayed());
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
        nextPlayer();
        while (getCurrentPlayer() != getHumanPlayer() || getCurrentPlayer().checkIfPlayerHaveNoCards()) {
            roundReport.addPlayRaport(reportCreator.executePlay(computerPlayMaker.create()));
            nextPlayer();
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

    Player getCurrentPlayer() {
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
