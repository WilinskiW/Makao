package com.wwil.makao.backend.events;

import com.wwil.makao.backend.Player;

public class AttackPreviousPlayerEvent implements Event {
    private final Player attackedPlayer;

    public AttackPreviousPlayerEvent(Player attackedPlayer) {
        this.attackedPlayer = attackedPlayer;
    }

    @Override
    public void startEvent() {

    }
}
