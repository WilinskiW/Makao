package com.wwil.makao.frontend.controllers.facedes;

import com.wwil.makao.backend.core.MakaoBackend;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.frontend.entities.cards.CardActor;

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

    public List<Player> getPlayerList(){
        return backend.getPlayerManager().getPlayers();
    }

    public Card getStackCard(){
        return backend.getDeckManager().peekStackCard();
    }
}
