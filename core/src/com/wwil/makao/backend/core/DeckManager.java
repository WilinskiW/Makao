package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.*;

import java.util.*;
import java.util.stream.Collectors;

public class DeckManager {
    private final LinkedList<Card> gameDeck;
    private final Stack stack;

    DeckManager() {
        this.gameDeck = createCardsToGameDeck();
        this.stack = new Stack();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
    }

    private LinkedList<Card> createCardsToGameDeck() {
        LinkedList<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        return cards;
    }

    private Card getStartStackCard() {
        for (Card card : gameDeck) {
            if (card.getRank().getAbility().equals(Ability.NONE)) {
                return card;
            }
        }
        return null;
    }

    public List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    public void addToStack(Card card) {
        stack.addCardToStack(card);
    }

    public Card peekStackCard() {
        return stack.peekCard();
    }


    public boolean isStackCardBeforeLastIsJoker() {
        List<Card> cards = stack.getCards();
        int size = cards.size();

        // Upewnij się, że są co najmniej dwie karty w stosie
        if (size < 2) {
            return false; // Nie ma przedostatniej karty
        }

        // Uzyskaj przedostatnią kartę
        Card beforeLastCard = cards.get(size - 2);

        // Sprawdź, czy przedostatnia karta to Joker
        return beforeLastCard.matchesRank(Rank.JOKER);
    }


    public Card takeCardFromGameDeck() {
        if (gameDeck.isEmpty()) {
            refreshGameDeck();
        }
        return gameDeck.pollLast();
    }

    private void refreshGameDeck() {
        Card stackCard = stack.pollLast();
        gameDeck.addAll(getStackCards(stackCard));
        stack.getCards().clear();
        stack.addCardToStack(stackCard);
    }

    private List<Card> getStackCards(Card lastStackCard) {
        List<Card> cards = stack.getCards()
                .stream()
                .filter(card -> !card.isShadow())
                .collect(Collectors.toList());
        cards.remove(lastStackCard);
        return cards;
    }
}
