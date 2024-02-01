package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.Card;

import java.util.List;

public class CardActor extends Actor { //todo klasa z logiką i grafiką
    private final Card card;
    private final TextureRegion frontSide;
    private final TextureRegion backSide;
    private boolean isUpSideDown = true;
    private float lastPosX;
    private float lastPosY;
    private int lastPosZ;
    private Group lastParent;

    public CardActor(List<TextureRegion> textureRegions, Card card) {
        this.frontSide = textureRegions.get(0);
        this.backSide = textureRegions.get(1);
        this.card = card;
        setBounds(0, 0, GUIparams.CARD_WIDTH, GUIparams.CARD_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        TextureRegion currentCardSide;
        if (isUpSideDown) {
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

    public void saveLocation() {
        savePosition();
        savePosZ();
        saveGroup();
    }

    private void saveGroup() {
        lastParent = getParent();
    }

    private void savePosition() { //todo refactor kiedyś
        this.lastPosX = getX();
        this.lastPosY = getY();
    }

    private void savePosZ() { //todo refactor kiedyś
        this.lastPosZ = getZIndex();
    }

    private void loadX() {
        setX(lastPosX);
    }

    private void loadY() {
        setY(lastPosY);
    }

    private void loadPosZ() {
        setZIndex(lastPosZ);
    }

    public void backToPreviousLocation() {
        lastParent.addActor(this);
        loadX();
        loadY();
        loadPosZ();
    }

    @Override
    public String toString() {
        return String.format("CardActor{x=%.1f,y=%.1f}",getX(),getY());
    }
}

