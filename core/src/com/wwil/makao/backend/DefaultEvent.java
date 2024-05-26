package com.wwil.makao.backend;

public class DefaultEvent extends Event {
    public DefaultEvent(MakaoBackend backend) {
        super(backend);
    }

    @Override
    public Play response() {
        return playMaker.putOrPull(new Play());
    }

    @Override
    public void start() {
        backend.nextPlayer();
    }
}
