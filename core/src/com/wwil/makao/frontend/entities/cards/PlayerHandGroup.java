package com.wwil.makao.frontend.entities.cards;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.frontend.utils.params.CardsAlignmentParams;
import com.wwil.makao.frontend.utils.params.GUIparams;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandGroup extends Group {
    private final Player player;
    private CardsAlignmentParams alignment;

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
        actor.setX(lastActorX + GUIparams.HANDGROUP_CARDS_GAP);
        setPosition(getX() - alignment.xMove, getY() - alignment.yMove);
    }

    private void placeCardFirst(Actor actor) {
        float firstActorX = getChildren().first().getX();
        actor.setX(firstActorX - GUIparams.HANDGROUP_CARDS_GAP);
        addActorAt(0, actor);
        moveCloserToStartingPosition();
    }

    public void moveCloserToStartingPosition() {
        this.setPosition(this.getX() + alignment.xMove, this.getY() + alignment.yMove);
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
            currentActor.setX(currentActor.getX() - GUIparams.HANDGROUP_CARDS_GAP);
        }
    }

    public Player getPlayer() {
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

    public void setCardsAlignment(CardsAlignmentParams alignment) {
        this.alignment = alignment;
    }
}