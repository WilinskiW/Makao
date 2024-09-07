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

    public boolean isValidForNormalState(Card chosenCard) {
        return !roundManager.getCardsPlayedInTurn().isEmpty() && !chosenCard.isShadow() ?
                isValidForMultiplePut(chosenCard) :
                isValidForStandardPlay(chosenCard, getStackCard());
    }

    public boolean isValidForDefenceState(Card chosenCard) {
        return !roundManager.getCardsPlayedInTurn().isEmpty() && !chosenCard.isShadow() ?
                isValidForMultiplePut(chosenCard) :
                chosenCard.matchesRank(getStackCard()) || isRightForDemand(chosenCard) || isCardJoker(chosenCard);
    }

    private boolean isValidForMultiplePut(Card chosenCard) {
        return chosenCard.matchesRank(roundManager.getCardsPlayedInTurn().get(0));
    }
    private boolean isRightForDemand(Card chosenCard){
        return chosenCard.matchesRank(Rank.J) && getStackCard().isShadow();
    }
    private boolean isValidForStandardPlay(Card chosenCard, Card stackCard) {
        return canBePutOnEverything(chosenCard) ||
                canBePutOnEverything(stackCard) ||
                chosenCard.matchesRank(stackCard) ||
                chosenCard.matchesSuit(stackCard);
    }

    private boolean canBePutOnEverything(Card card) {
        return card.matchesRank(Rank.Q) || isCardJoker(card);
    }

    private boolean isCardJoker(Card card){
        return card.matchesRank(Rank.JOKER);
    }

    private Card getStackCard() {
        return deckManager.peekStackCard();
    }
}
