package com.wwil.makao.backend;

import java.util.List;

public class CardBattle {
    private final List<Card> cardToDraw;
    private final Card attackingCard;

    public CardBattle(List<Card> cardToDraw, Card attackingCard) {
        this.cardToDraw = cardToDraw;
        this.attackingCard = attackingCard;
    }


    public List<Card> getCardToDraw() {
        return cardToDraw;
    }

    public Card getAttackingCard() {
        return attackingCard;
    }

}
