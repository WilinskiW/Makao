package com.wwil.makao.frontend;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
public class PlayerHandGroup extends Group {

    // FIXME: 05.01.2024 Karty się przesuwają po dodaniu, ale nie powradzają do początkowej pozycji po usunięcia
    @Override
    public void addActor(Actor actor) {
        //Działa, ale bez wyśrodkowania
        if(!getChildren().isEmpty()) {
            if(getChildren().size > 5){
                setPosition(getX()-34,getY());
            }
            float lastActorX = getChildren().get(getChildren().size - 1).getX();
            actor.setX(lastActorX + GUIparams.DISTANCE_BETWEEN_CARDS);
        }
        super.addActor(actor);
    }

    @Override
    public boolean removeActor(Actor actor, boolean unfocus) {
        int cardIndex = actor.getZIndex();
        for(int i = cardIndex+1; i < getChildren().size; i++){
            Actor currentActor = getChildren().get(i);
            currentActor.setZIndex(i-1);
            currentActor.setX(currentActor.getX()-GUIparams.DISTANCE_BETWEEN_CARDS);
        }
        return super.removeActor(actor, unfocus);
    }
}
