package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.wwil.makao.backend.Card;
import com.wwil.makao.frontend.entities.CardActor;

public class CardActorFactory {
    public CardActor createCardActor(Card card) {
        TextureAtlas textureAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
        String rank = card.getRank().getName();
        String suit = card.getSuit().getName();
        return new CardActor(textureAtlas.findRegion(suit+rank),card);
    }
}
