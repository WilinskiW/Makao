package com.wwil.makao.backend.events;

import com.wwil.makao.backend.MakaoBackend;

public abstract class AttackEvent extends Event {
    public AttackEvent(MakaoBackend backend) {
        super(backend);
    }
}
