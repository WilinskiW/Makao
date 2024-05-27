package com.wwil.makao.backend;


public abstract class BattleEvent extends Event {
    private final Card attackingCard;
    public BattleEvent(MakaoBackend backend, Card attackingCard) {
        super(backend);
        this.attackingCard = attackingCard;
        this.isAttack = true;
    }

    @Override
    public Play response() {
        return playMaker.handleDefense(new Play(),this);
    }
    Card getAttackingCard() {
        return attackingCard;
    }
}
