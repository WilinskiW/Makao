package com.wwil.makao.backend.states;


import com.wwil.makao.backend.gameplay.PlayReport;

public class PullingState extends PunishState{
    protected PullingState(int amountOfPunishes) {
        super(amountOfPunishes);
    }

    @Override
    PlayReport setActionActivations(PlayReport playReport) {
        return playReport.setPullActive();
    }
}
