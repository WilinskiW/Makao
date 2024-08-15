package com.wwil.makao.backend;

public class CardValidator {
private final MakaoBackend backend;

    public CardValidator(MakaoBackend backend) {
        this.backend = backend;
    }

    public boolean isValid(Card chosenCard) {
        Card stackCard = backend.getStack().peekCard();

        // Sprawdzanie, czy gracz może położyć kolejną kartę o tej samej randze
        if (!backend.getHumanPlayedCards().isEmpty()) {
            return chosenCard.getRank() == backend.getHumanPlayedCards().get(0).getRank();
        }

       if(backend.currentPlayer().isAttack()){
           return isValidForDefence(chosenCard,stackCard);
       }
       else{
           return isValidForNormalTurn(chosenCard,stackCard);
       }
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
