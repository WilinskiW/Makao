package com.wwil.makao.backend;

//Klasa ta opisuje jak będzie wyglądać tura
public abstract class Event {
    protected final MakaoBackend backend;
    protected final ComputerPlayMaker playMaker;
    public Event(MakaoBackend backend) {
        this.backend = backend;
        this.playMaker = new ComputerPlayMaker(backend);
    }

    public abstract Play response();
    public abstract void start();
}
