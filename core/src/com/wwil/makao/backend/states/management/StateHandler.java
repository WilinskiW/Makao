package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;

public class StateHandler {
    private final StateChanger changer;

    protected StateHandler(StateChanger changer) {
        this.changer = changer;
    }

    public void updateStateAfterPut(Player player, Card card) {
        player.getState().handlePut(player, card, changer);
    }

    public void updateStateAfterPull(Player player) {
        player.getState().handlePull(player, changer);
    }

    public void updateStateAfterEnd(Player player) {
        player.getState().handleEnd(player, changer);
    }
}
