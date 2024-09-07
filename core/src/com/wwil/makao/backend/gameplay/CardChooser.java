package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.model.card.Ability;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.card.Suit;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.DefenseState;

import java.util.Collections;
import java.util.List;

public class CardChooser {
    private final DeckManager deckManager;

    CardChooser(DeckManager deckManager) {
        this.deckManager = deckManager;
    }

    public Card chooseCardForChangeSuit(Player player) {
        return new Card(peekStackCard().getRank(), giveMostDominantSuit(player)).setShadow(true);
    }
    private Suit giveMostDominantSuit(Player player) {
        int[] counts = new int[4]; // Tablica przechowująca liczbę wystąpień dla każdego koloru

        for (Card card : player.getCards()) {
            Suit cardSuit = card.getSuit();
            if (cardSuit != Suit.BLACK && cardSuit != Suit.RED) { // Pomijanie BLACK i RED
                counts[cardSuit.ordinal()]++; // Inkrementacja odpowiedniego licznika
            }
        }

        int maxIndex = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[maxIndex]) {
                maxIndex = i;
            }
        }

        if (maxIndex == 0) {
            return Suit.getRandom();
        }

        return Suit.values()[maxIndex]; // Zwracanie koloru z największą liczbą wystąpień
    }

    public Card chooseCardForDemand(Player player) {
        List<Card> cards = player.getCards();
        Collections.shuffle(cards);
        for (Card card : cards) {
            if (card.getRank().getAbility() == Ability.NONE) {
                return new Card(card.getRank(), card.getSuit()).setShadow(true);
            }
        }
        return new Card(Rank.getRandomNonFunctional(), peekStackCard().getSuit());
    }

    public Card chooseCardForWildCard(State previousState) {
        if (previousState instanceof DefenseState) {
            DefenseState defenseState = (DefenseState) previousState;
            Card card = defenseState.getAttackingCard();
            return new Card(card.getRank(), Suit.getRandom()).setShadow(true);
        } else
            return new Card(Rank.getRandomAttackingRank(), getLastNonShadowCard().getSuit()).setShadow(true);
    }

    private Card peekStackCard(){
        return deckManager.peekStackCard();
    }

    private Card getLastNonShadowCard(){
        return deckManager.getLastNonShadowCard();
    }
}
