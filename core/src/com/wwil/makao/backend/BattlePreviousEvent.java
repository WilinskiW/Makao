package com.wwil.makao.backend;

public class BattlePreviousEvent extends BattleEvent {
    public BattlePreviousEvent(MakaoBackend backend, Card attackingCard) {
        super(backend, attackingCard);
    }
    @Override
    public void start() {
        backend.playerBefore();
    }
}
