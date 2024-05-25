package com.wwil.makao.backend;

public class AttackPreviousPlayerEvent extends Event {

    public AttackPreviousPlayerEvent(MakaoBackend backend) {
        super(backend);
    }

    @Override
    public void startEvent() {
        System.out.println("Event begin");
        backend.playerBefore();
        roundReport().addPlayRaport(backend.executePlay(backend.playMaker.generate()));
        backend.nextPlayer();
        backend.nextPlayer();
        System.out.println("Event end");
    }
}
