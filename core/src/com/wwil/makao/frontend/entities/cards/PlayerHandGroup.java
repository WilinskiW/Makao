package com.wwil.makao.frontend.entities.cards;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.frontend.utils.params.CardsAlignmentParams;
import com.wwil.makao.frontend.utils.params.GUIparams;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandGroup extends Group {
    private final Player player;
    private final CardsAlignmentParams cardsAlignment;

    public PlayerHandGroup(Player player, CardsAlignmentParams cardsAlignment) {
        this.player = player;
        this.cardsAlignment = cardsAlignment;
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        setOrigin(Align.center);
        alignCards();
        setRotation(cardsAlignment.getRotation());
    }

    private void alignCards() {
        int cardCount = getChildren().size;
        float totalWidth = (cardCount - 1) * GUIparams.HANDGROUP_CARDS_GAP; //odejmujemy dobraną kartę

        for (int i = 0; i < cardCount; i++) {
            Actor card = getChildren().get(i);

            // Przesuwamy karty z talii do lewej od środka
            float newX = -(totalWidth / 2f) + (i * GUIparams.HANDGROUP_CARDS_GAP);
            card.setPosition(newX, 0);

            // Reset rotacji
            card.setRotation(0);
        }

        setSize(totalWidth, GUIparams.CARD_HEIGHT);

        // Ustawienie origin na środek po rozłożeniu kart
        this.setOrigin(getWidth() / 2, getHeight() / 2);
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
}
