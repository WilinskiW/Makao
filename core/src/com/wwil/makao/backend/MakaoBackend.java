package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.backend.cardComponents.CardFactory;
import com.wwil.makao.backend.cardComponents.Rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<PlayerHand> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private RoundReport roundReport;

    //fixme specjalne umiejetnosci
    //Przygotowanie backendu:
    public MakaoBackend() {
        createCardsToGameDeck();
        stack.addCardToStack(takeCardFromGameDeck());
        createPlayers();
    }

    private void createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
    }

    private Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    private void createPlayers() {
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            PlayerHand playerHand = new PlayerHand(giveCards(STARTING_CARDS));
            players.add(playerHand);
        }
    }

    private List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    //////
    //////////// Logika:
    //Jedyna publiczna metoda (odbiera informacje, wykonuje działanie i wysyła)
    public RoundReport executeAction(Play humanPlay) {
        roundReport = new RoundReport();
        //Opcja, gdy gracz jeszcze nie polozyl karty
        if (!humanPlay.isDropped() && !humanPlay.wantsToDraw()) {
            roundReport.addPlay(new PlayReport(currentPlayer(),
                    null, humanPlay, null, isCorrectCard(humanPlay.getCardPlayed())));
            roundReport.setIncorrect();
            return roundReport;
        }

        if (!humanPlay.wantsToDraw() && !isCorrectCard(humanPlay.getCardPlayed())) {
            roundReport.setIncorrect();
            return roundReport;
        }
        playRound(humanPlay);

        return roundReport;
    }

    private boolean isCorrectCard(Card chosenCard) {
        Card stackCard = stack.peekCard();
        if (stackCard.getRank().equals(Rank.Q) || chosenCard.getRank().equals(Rank.Q)
            /*|| chosenCard.getRank().name().equals("JOKER")*/) {
            return true;
        }

        return compareCards(chosenCard, stackCard);
    }

    private boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    private void playRound(Play humanPlay) {
        roundReport.addPlay(executePlay(humanPlay));
        nextPlayer();
        for (int i = 1; i < players.size(); i++) {
            roundReport.addPlay(executePlay(executeComputerPlay()));
            nextPlayer();
        }
    }

    private PlayReport executePlay(Play play) {
        if (play.wantsToDraw()) {
            Card drawn = takeCardFromGameDeck();
            players.get(currentPlayerIndex).addCardToHand(drawn);
            return new PlayReport(currentPlayer(), null, play, drawn, false);
        }
        PullDemander pullDemander = putCard(play.getCardPlayed());
        return new PlayReport(currentPlayer(), pullDemander, play, null, true);
    }

    private void nextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == AMOUNT_OF_PLAYERS) {
            currentPlayerIndex = 0;
        }
    }

    private PullDemander putCard(Card cardPlayed) {
        PullDemander pullDemander = useCardAbility(cardPlayed);
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        return pullDemander;
    }

    private Play executeComputerPlay() {
        PlayerHand playerHand = currentPlayer();
        for (Card card : playerHand.getCards()) {
            if (isCorrectCard(card)) {
                return new Play(card, false, true);
            }
        }
        return new Play(null, true, false);
    }


    private PullDemander useCardAbility(Card card) {
        PullDemander pullDemander = null;
        switch (card.getRank()) {
            case TWO:
                pullDemander = usePlusAbility(2);
                break;
            case THREE:
                pullDemander = usePlusAbility(3);
                break;
            case FOUR:
                useFourAbility();
                break;
            case K:
                pullDemander = useKingAbility(card,pullDemander);
                break;
        }
        return pullDemander;
    }


    private PullDemander usePlusAbility(int amountOfCards) {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(amountOfCards);
        if (currentPlayerIndex != lastIndex) {
            players.get(currentPlayerIndex + 1).addCardsToHand(pulledCards);
            return new PullDemander(currentPlayerIndex+1, pulledCards);
        } else {
            players.get(0).addCardsToHand(pulledCards);
            return new PullDemander(0, pulledCards);
        }
    }

    private void useFourAbility() {
        int lastIndex = players.size() - 1;
        if (currentPlayerIndex != lastIndex) {
            players.get(currentPlayerIndex + 1).setWaiting(true);
        } else {
            players.get(0).setWaiting(true);
        }
    }

    private PullDemander useKingAbility(Card card, PullDemander pullDemander) {
        switch (card.getSuit()) {
            case HEART:
                pullDemander = usePlusAbility(5);
                break;
            case SPADE:
                pullDemander = usePlusFiveAbilityToPreviousPlayer();
        }
        return pullDemander;
    }

    private PullDemander usePlusFiveAbilityToPreviousPlayer() {
        int lastIndex = players.size() - 1;
        List<Card> pulledCards = giveCards(5);
        if (currentPlayerIndex != 0) {
            players.get(currentPlayerIndex - 1).addCardsToHand(pulledCards);
            return new PullDemander(currentPlayerIndex - 1,pulledCards);
        } else {
            players.get(lastIndex).addCardsToHand(pulledCards);
            return new PullDemander(0,pulledCards);
        }
    }

    private PlayerHand currentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public Stack getStack() {
        return stack;
    }

    public List<PlayerHand> getPlayers() {
        return players;
    }
}
