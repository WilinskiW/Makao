package com.wwil.makao.backend.states;


import com.wwil.makao.backend.model.player.Player;

public class BlockedState extends PunishState {


    BlockedState(Player player, int amountOfPunishes) {
        super(player, amountOfPunishes);
    }

    public boolean canUnblock(){
        return amountOfPunishes == 0;
    }

    @Override
    void setDefaultValueOfActivations() {
        this.isPutActive = false;
        this.isPullActive = false;
        this.isEndActive = true;
    }
}
