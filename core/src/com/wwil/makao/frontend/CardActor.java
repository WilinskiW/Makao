package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.backend.Card;
import com.wwil.makao.backend.Rank;
import com.wwil.makao.backend.Suit;

public class CardActor extends Actor { //todo klasa z logiką i grafiką
    private Card card;
    private Texture frontSide;
    private Texture backSide = new Texture(Gdx.files.internal("Cards/backCard.png")); // TODO: 02.11.2023 Do dyskusji
    private boolean isUpSideDown = true;


    public CardActor(Texture frontSide, Card card) {
        this.frontSide = frontSide;
        this.card = card;
        setBounds(0,0,GUIparams.CARD_WIDTH,GUIparams.CARD_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        Texture texture;
        if(isUpSideDown){
            texture = backSide;
        }
        else {
            texture = frontSide;
        }
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation(), 0, 0, texture.getWidth(), texture.getHeight(),
                false, false);
        batch.setColor(Color.WHITE);
    }

    public void setUpSideDown(boolean upSideDown) {
        isUpSideDown = upSideDown;
    }

    public Texture getFrontSide() {
        return frontSide;
    }

    public Texture getBackSide() {
        return backSide;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}
