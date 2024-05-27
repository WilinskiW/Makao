package com.wwil.makao.backend;

public class BattleNextEvent extends BattleEvent {
    public BattleNextEvent(MakaoBackend backend, Card attackingCard) {
        super(backend, attackingCard);
    }
    @Override
    public void start() {
        backend.nextPlayer();
    }

}
