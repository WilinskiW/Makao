package com.wwil.makao.backend.states;

import com.wwil.makao.backend.model.player.Player;

public class PullingState extends PunishState{
    protected PullingState(Player player, int amountOfPunishes) {
        super(player, amountOfPunishes);
    }

    @Override
    void setDefaultValueOfActivations() {
        this.isPutActive = false;
        this.isPullActive = true;
        this.isEndActive = false;
    }
}
