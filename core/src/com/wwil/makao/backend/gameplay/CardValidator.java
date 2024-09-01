package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;

public class CardValidator {
    private final RoundManager roundManager;
    private final DeckManager deckManager;

    CardValidator(RoundManager roundManager, DeckManager deckManager) {
        this.roundManager = roundManager;
        this.deckManager = deckManager;
    }

    public boolean isValidForNormalTurn(Card chosenCard) {
        if (!roundManager.getCardsPlayedInTurn().isEmpty()) {
            return isValidForMultiplePut(chosenCard);
        }

        Card stackCard = getStackCard(chosenCard, false);
        if (stackCard.getRank().equals(Rank.Q) || chosenCard.getRank().equals(Rank.Q)
                || chosenCard.getRank().equals(Rank.JOKER) || stackCard.getRank().equals(Rank.JOKER)) {
            return true;
        }

        return stackCard.getSuit() == chosenCard.getSuit() || stackCard.getRank() == chosenCard.getRank();
    }

    private boolean isValidForMultiplePut(Card chosenCard) {
        return chosenCard.getRank() == roundManager.getCardsPlayedInTurn().get(0).getRank();
    }

    private Card getStackCard(Card chosenCard, boolean isChooserActive) {
        if (chosenCard.getRank() == Rank.J && isChooserActive) {
            return deckManager.peekStackCardBeforeLast();
        } else {
            return deckManager.peekStackCard();
        }
    }


    public boolean isValidForDefence(Card chosenCard) {
        if (!roundManager.getCardsPlayedInTurn().isEmpty()) {
            return isValidForMultiplePut(chosenCard);
        }

        Card stackCard = getStackCard(chosenCard, false);
        if (chosenCard.getRank() == stackCard.getRank()) {
            return true;
        }
        return chosenCard.getSuit() == stackCard.getSuit()
                && chosenCard.isBattleCard() && stackCard.getRank().equals(Rank.K);
    }
}
