package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.List;

public class PlayerHandGroup extends Group {
    private List<CardActor> cardActors;

    public void addActors(List<CardActor> cards){
        for(CardActor cardActor : cards){
            addActor(cardActor);
        }
    }

    // FIXME: 05.01.2024 Karty się przesuwają po dodaniu, ale nie powradzają do początkowej pozycji po usunięcia
    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()) {
            if(getChildren().size > 5){
                setPosition(getX()-34,getY());
            }
            float lastActorX = getChildren().get(getChildren().size - 1).getX();
            actor.setX(lastActorX + 65);
        }

        super.addActor(actor);
    }
}
