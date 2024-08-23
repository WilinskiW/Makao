package com.wwil.makao.frontend.entities.cards;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.core.DeckManager;

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

        if(deckManager.isRefreshNeeded()){
            dismantleCardActors();
        }

        super.addActor(actor);
    }

    public CardActor peekCardActor(){
        return (CardActor) getChildren().peek();
    }

    public CardActor peekBeforeLastCardActor(){
        return (CardActor) getChildren().get(getChildren().size - 2);
    }

    private void dismantleCardActors(){
        for(int i = 0; i < getChildren().size-1; i++){
            CardActor cardActor = (CardActor) getChildren().removeIndex(i);
            deckManager.getGameDeck().add(cardActor.getCard());
        }
    }
}