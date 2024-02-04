package com.wwil.makao.frontend;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class StackCardsGroup extends Group {
    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            float firstActorX = getChildren().get(0).getX();
            actor.setX(firstActorX - MathUtils.random(-15,15));
            actor.setY(getChildren().get(0).getY());

        }

        super.addActor(actor);
    }

}
