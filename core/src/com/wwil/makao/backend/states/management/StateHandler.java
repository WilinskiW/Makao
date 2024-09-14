package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.model.player.Player;

public class StateHandler {
    private final StateChanger changer;

    protected StateHandler(StateChanger changer) {
        this.changer = changer;
    }

    public void updatePlayerState(Player player, Play play) {
        switch (play.getAction()) {
            case PUT:
                updateStateAfterPut(player, play.getCardPlayed());
                break;
            case PULL:
                updateStateAfterPull(player);
                break;
            case END:
                updateStateAfterEnd(player);
                break;
            case MAKAO:
                updateStateAfterFailMakao(player);
                break;
        }
    }

    private void updateStateAfterPut(Player player, Card card) {
        player.getState().handlePut(player, card, changer);
    }

    private void updateStateAfterPull(Player player) {
        player.getState().handlePull(player, changer);
    }

    private void updateStateAfterEnd(Player player) {
        player.getState().handleEnd(player, changer);
    }

    private void updateStateAfterFailMakao(Player player) {
        if (player instanceof Human) {
            changer.applyMakaoPunishState(player);
        } else {
            throw new IllegalArgumentException("Player is not a human instance!");
        }
    }
}
