package com.wwil.makao.backend.gameplay.validation;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.management.GameStateManager;
import com.wwil.makao.backend.model.card.Ability;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.card.Suit;

public class CardValidator {
    private final GameStateManager gameStateManager;
    private final DeckManager deckManager;

    public CardValidator(GameStateManager gameStateManager, DeckManager deckManager) {
        this.gameStateManager = gameStateManager;
        this.deckManager = deckManager;
    }

    public boolean isValidForNormalState(Card chosenCard) {
        return !gameStateManager.getCardsPlayedInTurn().isEmpty() && !chosenCard.isShadow() ?
                isValidForMultiplePut(chosenCard) :
                isValidForStandardPlay(chosenCard, getStackCard());
    }

    public boolean isValidForDefenceState(Card chosenCard) {
        return !gameStateManager.getCardsPlayedInTurn().isEmpty() && !chosenCard.isShadow() ?
                isValidForMultiplePut(chosenCard) :
                chosenCard.matchesRank(getStackCard()) || isRightForDemand(chosenCard) || isJoker(chosenCard);
    }

    private boolean isValidForMultiplePut(Card chosenCard) {
        return chosenCard.matchesRank(gameStateManager.getCardsPlayedInTurn().get(0))
                && !(chosenCard.matchesRank(Rank.K) && chosenCard.matchesSuit(Suit.CLUB))
                && !(chosenCard.matchesRank(Rank.K) && chosenCard.matchesSuit(Suit.DIAMOND));
    }
    private boolean isRightForDemand(Card chosenCard){
        return chosenCard.matchesRank(Rank.J)
                && getStackCard().isShadow()
                && getStackCard().getRank().getAbility() == Ability.NONE;
    }
    private boolean isValidForStandardPlay(Card chosenCard, Card stackCard) {
        return canBePutOnEverything(chosenCard) ||
                isQueen(stackCard) ||
                chosenCard.matchesRank(stackCard) ||
                chosenCard.matchesSuit(stackCard);
    }

    private boolean canBePutOnEverything(Card card) {
        return isQueen(card) || isJoker(card);
    }

    private boolean isQueen(Card card){
        return card.matchesRank(Rank.Q);
    }

    private boolean isJoker(Card card){
        return card.matchesRank(Rank.JOKER);
    }

    private Card getStackCard() {
        if(deckManager.peekStackCard().isShadow()){
            return deckManager.peekStackCard();
        }
        return deckManager.getLastNonShadowCard();
    }
}
