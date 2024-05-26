package com.wwil.makao.backend;

import java.util.List;

public abstract class BattleEvent extends Event {
    private final List<Card> cardToDraw;
    private final Card attackingCard;
    public BattleEvent(MakaoBackend backend, List<Card> cardToDraw, Card attackingCard) {
        super(backend);
        this.cardToDraw = cardToDraw;
        this.attackingCard = attackingCard;
    }

    Card getAttackingCard() {
        return attackingCard;
    }
}
