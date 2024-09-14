package com.wwil.makao.backend.model.player;


import com.wwil.makao.backend.gameplay.actions.ActionType;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.states.management.StateHandler;

import java.util.List;

public class Computer extends Player {
    public Computer(int id, List<Card> cards) {
        super(id, cards);
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public void handleMakaoAction(Human humanPlayer, StateHandler stateHandler) {
        humanPlayer.setMakaoInform(false);
        stateHandler.updatePlayerState(humanPlayer, new Play().setAction(ActionType.MAKAO));
    }
}
