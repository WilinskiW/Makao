package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public class PlayMaker {
    private final RoundManager roundManager;
    private final CardFinder cardFinder;

    public PlayMaker(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.cardFinder = new CardFinder(roundManager.getValidator());
    }

    public List<Play> makePlays(Player player) {
        return player.getState().generatePlays(player, roundManager, cardFinder);
    }
}