package com.wwil.makao.backend.events;

import com.wwil.makao.backend.MakaoBackend;

public class AttackNextEvent extends AttackEvent{
    public AttackNextEvent(MakaoBackend backend) {
        super(backend);
    }

    @Override
    public void start() {

    }

}
