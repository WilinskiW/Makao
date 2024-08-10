package com.wwil.makao.frontend.entities.cardsGroup;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.Player;
import com.wwil.makao.backend.Card;
import com.wwil.makao.frontend.CardsAlignmentParams;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.entities.CardActor;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandGroup extends Group {
    private final Player player;
    private CardsAlignmentParams cardsAlignment;

    public PlayerHandGroup(Player player) {
        this.player = player;
    }

    @Override
    public void addActor(Actor actor) {
        if (!getChildren().isEmpty()) {
            chooseWhereCardShouldBe(actor);
        }
        super.addActor(actor);
    }

    private void chooseWhereCardShouldBe(Actor actor) {
        if (getChildren().size % 2 == 1) {
            placeCardFirst(actor);
        } else {
            placeCardLast(actor);
        }
    }

    private void placeCardLast(Actor actor) {
        float lastActorX = getChildren().peek().getX();
        actor.setX(lastActorX + GUIparams.DISTANCE_BETWEEN_CARDS);
        setPosition(getX() - cardsAlignment.xMove, getY() - cardsAlignment.yMove);
    }

    private void placeCardFirst(Actor actor) {
        float firstActorX = getChildren().first().getX();
        actor.setX(firstActorX - GUIparams.DISTANCE_BETWEEN_CARDS);
        addActorAt(0, actor);
        moveCloserToStartingPosition();
    }

    public void moveCloserToStartingPosition() {
        this.setPosition(this.getX() + cardsAlignment.xMove, this.getY() + cardsAlignment.yMove);
    }

    @Override
    public boolean removeActor(Actor actor, boolean unfocused) {
        moveActorsToFillEmptySpace(actor);
        return super.removeActor(actor, unfocused);
    }

    private void moveActorsToFillEmptySpace(Actor actor) {
        int cardIndex = actor.getZIndex();
        for (int i = cardIndex + 1; i < getChildren().size; i++) {
            Actor currentActor = getChildren().get(i);
            currentActor.setZIndex(i - 1);
            currentActor.setX(currentActor.getX() - GUIparams.DISTANCE_BETWEEN_CARDS);
        }
    }

    public Player getPlayerHand() {
        return player;
    }

    public CardActor getCardActor(Card card) {
        for (CardActor cardActor : getCardActors()) {
            if (cardActor.getCard().equals(card)) {
                return cardActor;
            }
        }
        return null;
    }

    public List<CardActor> getCardActors() {
        List<CardActor> cardActors = new ArrayList<>();
        for (Actor actor : getChildren()) {
            CardActor cardActor = (CardActor) actor;
            cardActors.add(cardActor);
        }
        return cardActors;
    }

    public CardsAlignmentParams getCardsAlignment() {
        return cardsAlignment;
    }

    public void setCardsAlignment(CardsAlignmentParams cardsAlignment) {
        this.cardsAlignment = cardsAlignment;
    }
}
