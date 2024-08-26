package com.wwil.makao.backend.core;


import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.gameplay.RoundReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.PlayerManager;

public class MakaoBackend {
    private final DeckManager deckManager;
    private final PlayerManager playerManager;
    private final RoundManager roundManager;

    public MakaoBackend() {
        this.deckManager = new DeckManager();
        this.playerManager = new PlayerManager(RuleParams.AMOUNT_OF_PLAYERS, RuleParams.STARTING_CARDS, deckManager);
        this.roundManager = new RoundManager(playerManager, deckManager);
    }
    public RoundReport processHumanPlay(Play humanPlay) {
        return roundManager.getHumanPlayAnalyzer().processHumanPlay(humanPlay);
    }

    public boolean isCardValid(Card chosenCard) {
        return roundManager.getHumanPlayAnalyzer().isCardValid(chosenCard,false);
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
