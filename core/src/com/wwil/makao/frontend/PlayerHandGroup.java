package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PlayerHandGroup extends Group {
    private CardsAligmentParams cardsAligment;

    @Override
    public void addActor(Actor actor) {
        if (!getChildren().isEmpty()) {
            chooseWhereCardShouldBe(actor);
        }
        super.addActor(actor);
    }

    private void chooseWhereCardShouldBe(Actor actor){
        if (getChildren().size % 2 == 1) {
            placeCardAsFirst(actor);
        } else {
            placeCardAsLast(actor);
        }
    }

    private void placeCardAsLast(Actor actor){
        float lastActorX = getChildren().peek().getX();
        actor.setX(lastActorX + GUIparams.DISTANCE_BETWEEN_CARDS);
        setPosition(getX() - cardsAligment.xMove, getY() - cardsAligment.yMove);
    }

    private void placeCardAsFirst(Actor actor){
        float firstActorX = getChildren().first().getX();
        actor.setX(firstActorX - GUIparams.DISTANCE_BETWEEN_CARDS);
        addActorAt(0, actor);
        repositionGroup();
    }

    public void repositionGroup() {
        this.setPosition(this.getX() + cardsAligment.xMove, this.getY() + cardsAligment.yMove);
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

    public void setCardsAligment(CardsAligmentParams cardsAligment) {
        this.cardsAligment = cardsAligment;
    }
}
