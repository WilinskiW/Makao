package com.wwil.makao.frontend.entities.groups;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.Stack;
import com.wwil.makao.frontend.entities.CardActor;

public class StackCardsGroup extends Group {
    private final Stack stack;

    public StackCardsGroup(Stack stack) {
        this.stack = stack;
    }

    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            float firstActorX = getChildren().get(0).getX();
            actor.setX(firstActorX - MathUtils.random(-10,10));
            actor.setY(getChildren().get(0).getY());
        }

        if(stack.isRefreshNeeded()){
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
            MakaoBackend.gameDeck.add(cardActor.getCard());
        }
    }
}
