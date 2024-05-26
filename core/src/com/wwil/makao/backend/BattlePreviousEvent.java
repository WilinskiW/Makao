package com.wwil.makao.backend;

import java.util.List;

public class BattlePreviousEvent extends BattleEvent {
    public BattlePreviousEvent(MakaoBackend backend, List<Card> cardToDraw, Card attackingCard) {
        super(backend, cardToDraw, attackingCard);
    }

    @Override
    public Play response() {
        return playMaker.handleDefense(new Play(), this);
    }

    @Override
    public void start() {
        backend.playerBefore();
    }
}
