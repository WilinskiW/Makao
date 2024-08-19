package com.wwil.makao.frontend;

import com.wwil.makao.backend.*;
import com.wwil.makao.frontend.entities.CardActor;

import java.util.List;

public class BackendFacade {
    private final MakaoBackend backend;

    public BackendFacade() {
        this.backend = new MakaoBackend();
    }

    public RoundReport processHumanPlay(Play humanPlay){
        return backend.processHumanPlay(humanPlay);
    }

    public boolean isDraggedCardValid(CardActor cardActor){
        return backend.isCardValid(cardActor.getCard());
    }

    public List<Player> getPlayers(){
        return backend.getPlayerManager().getPlayers();
    }

    public DeckManager getDeckManager(){
        return backend.getDeckManager();
    }
}
