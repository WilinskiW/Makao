package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.PlayReport;

public class BlockedState extends PunishState {
    protected BlockedState(int amountOfPunishes) {
        super(amountOfPunishes);
    }

    protected boolean canUnblock(){
        return amountOfPunishes == 0;
    }

    @Override
    PlayReport setActionActivations(PlayReport playReport) {
        return playReport.setEndActive();
    }
}
