package com.wwil.makao.backend;


public class MakaoBackend {
    private final DeckManager deckManager;
    private final PlayerManager playerManager;
    private final RoundManager roundManager;

    public MakaoBackend() {
        this.deckManager = new DeckManager();
        this.playerManager = new PlayerManager(RuleParams.AMOUNT_OF_PLAYERS, RuleParams.STARTING_CARDS, deckManager);
        this.roundManager = new RoundManager(playerManager, deckManager);
    }

    //todo: Schemat aktywacji przycisków (Multi pull i single pull)
    //todo: Report mówi co powinno być aktywne (boolean putActive, boolean pullActive, boolean endActive)
    //todo: Animacje

    public RoundReport processHumanPlay(Play humanPlay) {
        return roundManager.processHumanPlay(humanPlay);
    }

    public boolean isDraggedCardValid(Card choosenCard) {
        return roundManager.getValidator().isValid(choosenCard);
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
