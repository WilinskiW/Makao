package com.wwil.makao.backend;

import java.util.List;

public class CardValidator {
private final MakaoBackend backend;

    public CardValidator(MakaoBackend backend) {
        this.backend = backend;
    }

    public boolean isValidCardForCurrentEvent(Card card, Event event) {
        if(event.isAttack){
             return isValidForBattle(card, (BattleEvent) event);
        }

        return isValidForDefault(card);
    }

    private boolean isValidForBattle(Card chosenCard, BattleEvent battleInfo){
        List<Card> defensiveCards = backend.currentPlayer().findDefensiveCards(battleInfo);
        return defensiveCards.contains(chosenCard);
    }

    public boolean isValidForDefault(Card chosenCard) {
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
}
