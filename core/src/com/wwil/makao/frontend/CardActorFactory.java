package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wwil.makao.backend.Card;

import java.util.ArrayList;
import java.util.List;

public class CardActorFactory {
    public CardActor createCardActor(Card card) {
        String suit = card.getSuit().getName();
        String rank = card.getRank().getName();
        TextureAtlas textureAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
        return new CardActor(textureAtlas.findRegion(suit+rank),card);
    }
}
