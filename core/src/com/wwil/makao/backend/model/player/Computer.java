package com.wwil.makao.backend.model.player;


import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.states.management.StateHandler;

import java.util.List;

public class Computer extends Player {
    public Computer(List<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public void handleMakaoAction(Human humanPlayer, StateHandler stateHandler) {
        stateHandler.updateStateAfterFailMakao(humanPlayer);
        humanPlayer.setMakaoInform(false);
    }

}
