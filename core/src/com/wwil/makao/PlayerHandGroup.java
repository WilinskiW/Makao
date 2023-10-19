package com.wwil.makao;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.List;

public class PlayerHandGroup extends Group {

    public void addActors(List<CardActor> cards){
        for(CardActor cardActor : cards){
            addActor(cardActor);
        }
    }
    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            float lastActorX = getChildren().get(getChildren().size - 1).getX();
            actor.setX(lastActorX + 50);
        }

        super.addActor(actor);
    }
}
