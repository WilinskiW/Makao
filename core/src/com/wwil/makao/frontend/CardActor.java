package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.Card;

import java.util.List;

public class CardActor extends Actor { //todo klasa z logiką i grafiką
    private Card card;
    private final TextureRegion frontSide;
    private TextureRegion backSide;
    private boolean isUpSideDown = GUIparams.HIDE_COMPUTER_CARD;
    private Group lastParent;
    private Vector3 lastPositionBeforeRemove = null;

    public CardActor(List<TextureRegion> textureRegions, Card card) {
        this.frontSide = textureRegions.get(0);
        this.backSide = textureRegions.get(1);
        this.card = card;
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

    public void setUpSideDown(boolean upSideDown) {
        isUpSideDown = upSideDown;
    }

    public Card getCard() {
        return card;
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

    public void beLastInGroup(){
       Actor lastActor = lastParent.getChildren().peek();
       lastParent.addActorAfter(lastActor,this);
       this.setX(lastActor.getX() + GUIparams.DISTANCE_BETWEEN_CARDS);
       this.setY(lastActor.getY());
       this.setZIndex(lastActor.getZIndex()+1);
    }

    @Override
    public String toString() {
        return String.format("CardActor{x=%.1f,y=%.1f,z=%x}",getX(),getY(),getZIndex());
    }
}

