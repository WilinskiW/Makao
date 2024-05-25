package com.wwil.makao.backend;

public abstract class Event {
    protected final MakaoBackend backend;

    public Event(MakaoBackend backend) {
        this.backend = backend;
    }

    public abstract void startEvent();

    protected RoundReport roundReport(){
        return backend.getRoundReport();
    }
}
