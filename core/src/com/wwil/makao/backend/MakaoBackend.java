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
    public RoundReport processHumanPlay(Play humanPlay) {
        return roundManager.processHumanPlay(humanPlay);
    }

    public boolean isDraggedCardValid(Card choosenCard) {
        return roundManager.getValidator().isValid(choosenCard,false);
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
