package com.wwil.makao.backend.states.impl.punish;

import com.wwil.makao.backend.core.RuleParams;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.management.StateChanger;

public class MakaoPunishState extends PullingState{

    public MakaoPunishState() {
        super(RuleParams.PULLS_AFTER_WRONG_MAKAO);
    }

    @Override
    public void handlePull(Player player, StateChanger changer) {
        super.handlePull(player, changer);
        changer.setActions(player,false,false,true, false);
    }
}
