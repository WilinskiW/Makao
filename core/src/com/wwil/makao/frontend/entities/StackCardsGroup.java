package com.wwil.makao.frontend.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.MakaoBackend;

public class StackCardsGroup extends Group {
    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            float firstActorX = getChildren().get(0).getX();
            actor.setX(firstActorX - MathUtils.random(-15,15));
            actor.setY(getChildren().get(0).getY());

            if(getChildren().size > 2){
                transferCardsToBackend();
            }
        }

        super.addActor(actor);
    }

    private void transferCardsToBackend(){
        for(int i = 0; i < getChildren().size-1; i++){
            CardActor cardActor = (CardActor) getChildren().removeIndex(i);
            MakaoBackend.cardsStorage.add(cardActor.getCard());
        }
    }
}
