package com.wwil.makao.frontend.entities.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.frontend.utils.params.GUIparams;

public class CardActor extends Actor {
    private Card card;
    private TextureRegion frontSide;
    private TextureRegion backSide;
    private boolean isUpSideDown;
    private Group lastParent;
    private Vector3 lastPositionBeforeRemove = null;

    public CardActor(TextureRegion frontSide, Card card) {
        this.frontSide = frontSide;
        this.backSide = new TextureRegion
                (new TextureAtlas("cards/classicFrontCard.atlas").findRegion("blankCardGray"));
        this.isUpSideDown = true;
        this.card = card;
        this.setOrigin(GUIparams.CARD_WIDTH / 2f, GUIparams.CARD_HEIGHT / 2f);
        setBounds(0, 0, GUIparams.CARD_WIDTH, GUIparams.CARD_HEIGHT);
    }

    public CardActor(TextureRegion frontSide) {
        this.frontSide = frontSide;
        setBounds(0, 0, GUIparams.CARD_WIDTH, GUIparams.CARD_HEIGHT);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        TextureRegion currentCardSide;
        if (backSide != null && isUpSideDown) {
            currentCardSide = backSide;
        } else {
            currentCardSide = frontSide;
        }

        batch.draw(currentCardSide, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation(), true);
        batch.setColor(Color.WHITE);
    }

    public void leaveGroup() {
        Vector2 stageVector = getStageVector();
        int groupSize = getParent().getChildren().size;
        getStage().addActor(this);
        setX(stageVector.x - groupSize * GUIparams.GAP_BETWEEN_DECK_CARDS);
        setY(stageVector.y);
    }

    public Vector2 getStageVector() {
        return localToStageCoordinates(new Vector2(getX(), getY()));
    }

    public void changeTransparency(float alpha) {
        this.setColor(getColor().r, getColor().g, getColor().b, alpha);
    }

    public void changeFrontSide(TextureRegion textureRegion) {
        setFrontSide(textureRegion);
    }

    public void setUpSideDown(boolean upSideDown) {
        isUpSideDown = upSideDown;
    }

    public void setFrontSide(TextureRegion frontSide) {
        this.frontSide = frontSide;
    }

    public Card getCard() {
        return card;
    }

    public TextureRegion getFrontSide() {
        return frontSide;
    }

    public void saveGroup() {
        lastParent = getParent();
    }

    public void setLastPositionBeforeRemove(Vector3 lastPositionBeforeRemove) {
        this.lastPositionBeforeRemove = lastPositionBeforeRemove;
    }

    public Vector3 getLastPositionBeforeRemove() {
        return lastPositionBeforeRemove;
    }

    public void beLastInGroup() {
        Actor lastActor = lastParent.getChildren().peek();
        lastParent.addActorAfter(lastActor, this);
        this.setX(lastActor.getX() + GUIparams.HANDGROUP_CARDS_GAP);
        this.setY(lastActor.getY());
        this.setZIndex(lastActor.getZIndex() + 1);
    }
}

