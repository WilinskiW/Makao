package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PlayerHandGroup extends Group {

    // FIXME: 05.01.2024 Karty się przesuwają po dodaniu, ale nie powradzają do początkowej pozycji po usunięcia

    @Override
    public void addActor(Actor actor) {
        //Działa, dla gracza 1
        if (!getChildren().isEmpty()) {
            setPosition(getX() - GUIparams.DISTANCE_BETWEEN_CARDS/2f, getY());
            float lastActorX = getChildren().get(getChildren().size - 1).getX();
            actor.setX(lastActorX + GUIparams.DISTANCE_BETWEEN_CARDS);
        }
        super.addActor(actor);
    }

    @Override
    public boolean removeActor(Actor actor, boolean unfocus) {
        int cardIndex = actor.getZIndex();
        for (int i = cardIndex + 1; i < getChildren().size; i++) {
            Actor currentActor = getChildren().get(i);
            currentActor.setZIndex(i - 1);
            currentActor.setX(currentActor.getX() - GUIparams.DISTANCE_BETWEEN_CARDS);
        }
        return super.removeActor(actor, unfocus);
    }


}
