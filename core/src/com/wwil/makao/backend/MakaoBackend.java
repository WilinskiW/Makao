package com.wwil.makao.backend;

import com.badlogic.gdx.Gdx;
import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.backend.cardComponents.CardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<PlayerHand> players = new ArrayList<>();
    private boolean inputBlock = false;
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;


    //Konstruktor tworzy karty i graczy.
    public MakaoBackend() {
        createCardsToGameDeck();
        stack.addCardToStack(takeCardFromGameDeck());
        createPlayers();
        //giveTestCards();
    }

    //Jedyna publiczna metoda (zbiera informacje i wysyła)
    public RoundReport playCard(Play humanPlay) {
        roundReport = new RoundReport();
        //todo przycisk
        if (humanPlay.wantsToDraw()) {
            throw new UnsupportedOperationException("Dodamy potem");
        }
        if (!isCorrectCard(humanPlay.getCardPlayed())) {
            roundReport.setIncorrect();
            return roundReport;
        }
        setInputBlock(true);
        playRound(humanPlay);
        setInputBlock(false);

        return roundReport;
    }

    private void playRound(Play humanPlay) {
        roundReport.addPlay(executePlay(humanPlay));
        for (int i = 1; i < players.size(); i++) {
          roundReport.addPlay(executePlay(executeComputerPlay()));
        }
    }

    private Play executeComputerPlay() {
        PlayerHand playerHand = currentPlayer();
        for (Card card : playerHand.getCards()) {
            if (isCorrectCard(card)) {
                return new Play(card, false);
            }
        }
        return new Play(null, true);
    }


    private PlayReport executePlay(Play play) { //todo nie uwzględnia jeszcze że ktoś chce dobrać
        Card cardPlayed = play.getCardPlayed();
        useCardAbility(cardPlayed);
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        endIfPlayerWon();
        currentPlayerIndex++;
        if (currentPlayerIndex == 3) {
            currentPlayerIndex = 0;
        }
       return new PlayReport(currentPlayer(),play.getCardPlayed());
    }

    private PlayerHand currentPlayer() {
        return players.get(currentPlayerIndex);
    }


    private boolean isCorrectCard(Card chosenCard) {
        Card stackCard = peekCardFromStack();
        if (stackCard.getRank().name().equals("Q") || chosenCard.getRank().name().equals("Q")
            /*|| chosenCard.getRank().name().equals("JOKER")*/) {
            return true;
        }

        return compareCards(chosenCard, stackCard);
    }

    private void useCardAbility(Card card) {
        switch (card.getRank()) {
            case TWO:
                usePlusNextPlayerAbility(2);
                break;
            case THREE:
                usePlusNextPlayerAbility(3);
                break;
            case FOUR:
                useFourAbility();
                break;
            case K:
                useKingAbility(card);
                break;
        }
    }

    private void createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
    }

    ///TEST///

//    private void giveTestCards() {
//        players.get(3).getCards().clear();
//        players.get(3).addCardToHand(new Card(Rank.FOUR, Suit.CLUB));
//        players.get(3).addCardToHand(new Card(Rank.FOUR, Suit.DIAMOND));
//        players.get(3).addCardToHand(new Card(Rank.FOUR, Suit.SPADE));
//        players.get(3).addCardToHand(new Card(Rank.FOUR, Suit.HEART));
//    }


    private void createPlayers() {
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            PlayerHand playerHand = new PlayerHand(giveCards(STARTING_CARDS));
            players.add(playerHand);
        }
    }

    private Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    private List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    private Card playerPullCard(int playerIndex) {
        Card card = takeCardFromGameDeck();
        players.get(playerIndex).addCardToHand(card);
        return card;
    }

    private void usePlusNextPlayerAbility(int amountOfCards) {
        int lastIndex = players.size() - 1;
        if (currentPlayerIndex != lastIndex) {
            players.get(currentPlayerIndex + 1).addCardsToHand(giveCards(amountOfCards));
        } else {
            players.get(0).addCardsToHand(giveCards(amountOfCards));
        }
    }

    private void useFourAbility() {
        int lastIndex = players.size() - 1;
        if (lastIndex != currentPlayerIndex) {
            players.get(currentPlayerIndex + 1).setWaiting(true);
        } else {
            players.get(0).setWaiting(true);
        }
    }

    private void useKingAbility(Card card) {
        switch (card.getSuit()) {
            case HEART:
                usePlusNextPlayerAbility(5);
                break;
            case SPADE:
                usePlusFiveAbilityToPreviousPlayer();
        }
    }

    private void usePlusFiveAbilityToPreviousPlayer() {
        int lastIndex = players.size() - 1;
        if (currentPlayerIndex != 0) {
            players.get(currentPlayerIndex - 1).addCardsToHand(giveCards(5));
        } else {
            players.get(lastIndex).addCardsToHand(giveCards(5));
        }
    }

    private void endIfPlayerWon() { //todo refactor -> bardziej na front
        if (currentPlayer().checkIfPlayerHaveNoCards()) {
            System.out.printf("Player %d won!", currentPlayerIndex + 1);
            Gdx.app.exit();
        }
    }
    //TODO: JOKER

    private Card peekCardFromStack() {
        List<Card> stackCards = stack.getCards();
        int lastIndexOfStack = stackCards.size() - 1;
        return stack.getCards().get(lastIndexOfStack);
    }

    private boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    public Stack getStack() {
        return stack;
    }

    public List<PlayerHand> getPlayers() {
        return players;
    }

    private boolean isInputBlock() {
        return inputBlock;
    }

    private void setInputBlock(boolean inputBlock) {
        this.inputBlock = inputBlock;
    }
}

//Sama logika