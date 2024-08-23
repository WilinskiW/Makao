package com.wwil.makao.backend.states;


import com.wwil.makao.backend.model.player.Player;

public class BlockedState extends PunishState {


    BlockedState(Player player, int amountOfPunishes) {
        super(player, amountOfPunishes);
    }

    boolean canUnblock(){
        return amountOfPunishes == 0;
    }

    @Override
    void setDefaultValueOfActivations() {
        setEndActive(true);
    }
}