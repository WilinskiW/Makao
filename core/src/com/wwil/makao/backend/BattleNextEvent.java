package com.wwil.makao.backend;

import java.util.List;

public class BattleNextEvent extends BattleEvent {
    public BattleNextEvent(MakaoBackend backend, List<Card> cardToDraw, Card attackingCard) {
        super(backend, cardToDraw, attackingCard);
    }
    @Override
    public Play response() {
        return playMaker.handleDefense(new Play(), this);
    }
    @Override
    public void start() {
        backend.nextPlayer();
    }

}
