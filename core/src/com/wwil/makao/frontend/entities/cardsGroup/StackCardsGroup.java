package com.wwil.makao.frontend.entities.cardsGroup;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.DeckManager;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.Stack;
import com.wwil.makao.frontend.entities.CardActor;

public class StackCardsGroup extends Group {
    private final DeckManager deckManager;

    public StackCardsGroup(DeckManager deckManager) {
        this.deckManager = deckManager;
    }

    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            float firstActorX = getChildren().get(0).getX();
            actor.setX(firstActorX - MathUtils.random(-10,10));
            actor.setY(getChildren().get(0).getY());
        }

        if(deckManager.getStack().isRefreshNeeded()){
            dismantleCardActors();
        }

        super.addActor(actor);
    }

    public CardActor peekCardActor(){
        return (CardActor) getChildren().peek();
    }

    private void dismantleCardActors(){
        for(int i = 0; i < getChildren().size-1; i++){
            CardActor cardActor = (CardActor) getChildren().removeIndex(i);
            deckManager.getGameDeck().add(cardActor.getCard());
        }
    }
}
