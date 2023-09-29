package com.wwil.makao;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class BoardDeckGroup extends Group {
    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            float lastActorX = getChildren().get(getChildren().size - 1).getX();
            actor.setX(lastActorX + 10);
        }

        super.addActor(actor);
    }
}
