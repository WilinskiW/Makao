package com.wwil.makao.backend;

public class AttackPreviousPlayerEvent extends Event {
    private final Player attackingPlayer;

    public AttackPreviousPlayerEvent(MakaoBackend backend) {
        super(backend);
        this.attackingPlayer = backend.getCurrentPlayer();
    }

    @Override
    public void startEvent() {
        backend.playerBefore();
        if(backend.getCurrentPlayer() != backend.getHumanPlayer()) {
            roundReport().addPlayRaport(backend.executePlay(backend.playMaker.generate()));
            endEvent();
        }
    }

    @Override
    public void endEvent() {
        backend.setCurrentPlayer(attackingPlayer);
        if (!backend.getCurrentPlayer().isAttack()) {
            backend.nextPlayer();
        }
    }
}
