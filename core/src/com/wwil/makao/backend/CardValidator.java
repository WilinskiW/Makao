package com.wwil.makao.backend;

import java.util.List;

public class CardValidator {
private final MakaoBackend backend;

    public CardValidator(MakaoBackend backend) {
        this.backend = backend;
    }

    public boolean isValidCardForCurrentState(Card card) {
        if (backend.getDemand().isActive()) {
            return isValidForDemand(card);
        }

        if(backend.getHumanPlayer().isAttack()){
            return isValidForDefence(card);
        }

        return isValidForRegularPlayCard(card);
    }

    private boolean isValidForRegularPlayCard(Card chosenCard) {
        Card stackCard = backend.getStack().peekCard();
        if (!backend.getHumanPlayedCards().isEmpty()) {
            return chosenCard.getRank() == backend.getHumanPlayedCards().get(0).getRank();
        }

        if (stackCard.getRank().equals(Rank.Q) || chosenCard.getRank().equals(Rank.Q)
                || chosenCard.getRank().equals(Rank.JOKER) || stackCard.getRank().equals(Rank.JOKER)) {
            return true;
        }

        return stackCard.getSuit() == chosenCard.getSuit() || stackCard.getRank() == chosenCard.getRank();
    }

    private boolean isValidForDemand(Card card) {
        Rank chosenCardRank = card.getRank();
        if (backend.getHumanPlayedCards().size() == 1) {
            return chosenCardRank == backend.getDemand().getCard().getRank() ||
                    chosenCardRank.equals(Rank.JOKER) ||
                    (chosenCardRank.equals(Rank.J) && backend.getStack().isJackOnTop()) ||
                    backend.getStack().isJackBeforeJoker() && chosenCardRank.equals(Rank.J);
        }
        return chosenCardRank == card.getRank();
    }

    private boolean isValidForDefence(Card chosenCard){
        List<Card> defensiveCards = backend.getHumanPlayer().findDefensiveCards(backend.getHumanPlayer().getAttacker());
        return defensiveCards.contains(chosenCard);
    }

}
