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

    public boolean isValid(Card chosenCard, boolean isChooserActive) {
        Card stackCard = getStackCard(chosenCard,isChooserActive);

        // Sprawdzanie, czy gracz może położyć kolejną kartę o tej samej randze
        if (!roundManager.getHumanPlayedCards().isEmpty() && !isChooserActive) {
            return isValidForMultiplePut(chosenCard);
        }

       if(roundManager.getPlayerManager().getCurrentPlayer().isAttack()){
           return isValidForDefence(chosenCard,stackCard);
       }
       else{
           return isValidForNormalTurn(chosenCard,stackCard);
       }
    }

    private Card getStackCard(Card chosenCard,boolean isChooserActive){
        if(chosenCard.getRank() == Rank.J && isChooserActive){
            return deckManager.peekStackCardBeforeLast();
        }
        else{
            return deckManager.peekStackCard();
        }
    }

    private boolean isValidForMultiplePut(Card chosenCard){
        return chosenCard.getRank() == roundManager.getHumanPlayedCards().get(0).getRank();
    }

    private boolean isValidForNormalTurn(Card chosenCard, Card stackCard){
        if (stackCard.getRank().equals(Rank.Q) || chosenCard.getRank().equals(Rank.Q)
                || chosenCard.getRank().equals(Rank.JOKER) || stackCard.getRank().equals(Rank.JOKER)) {
            return true;
        }

        return stackCard.getSuit() == chosenCard.getSuit() || stackCard.getRank() == chosenCard.getRank();
    }


    private boolean isValidForDefence(Card chosenCard, Card stackCard){
        if(chosenCard.getRank() == stackCard.getRank()){
            return true;
        }
        return chosenCard.getSuit() == stackCard.getSuit()
                && chosenCard.isBattleCard() && stackCard.getRank().equals(Rank.K);
    }
}
