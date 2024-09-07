package com.wwil.makao.backend.model.player;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.states.management.StateHandler;

import java.util.List;

public class Human extends Player{
    public Human(List<Card> cards) {
        super(cards);
    }
    private boolean isMakaoInform;

    @Override
    public boolean isHuman() {
        return true;
    }

    @Override
    public void handleMakaoAction(Human humanPlayer, StateHandler stateHandler) {
        humanPlayer.setMakaoInform(true);
    }


    public boolean isMakaoInform() {
        return isMakaoInform;
    }

    public void setMakaoInform(boolean makaoInform) {
        isMakaoInform = makaoInform;
    }
}
