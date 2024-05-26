package com.wwil.makao.backend.events;

import com.wwil.makao.backend.MakaoBackend;

//Klasa ta opisuje jak będzie wyglądać tura
public abstract class Event {
    protected final MakaoBackend backend;

    public Event(MakaoBackend backend) {
        this.backend = backend;
    }

    public abstract void start();
}
