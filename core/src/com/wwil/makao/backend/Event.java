package com.wwil.makao.backend;

public abstract class Event {
    protected final MakaoBackend backend;
    protected boolean active = true;

    public Event(MakaoBackend backend) {
        this.backend = backend;
    }

    public abstract void startEvent();

    public abstract void endEvent();

    protected RoundReport roundReport(){
        return backend.getRoundReport();
    }

    boolean isActive() {
        return active;
    }

    Event setActive(boolean active) {
        this.active = active;
        return this;
    }
}
