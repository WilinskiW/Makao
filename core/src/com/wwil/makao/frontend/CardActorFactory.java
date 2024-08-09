package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.wwil.makao.backend.Card;
import com.wwil.makao.frontend.entities.CardActor;

public class CardActorFactory {
    private final TextureAtlas textureAtlas;
    public CardActorFactory() {
        this.textureAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
    }

    public CardActor createCardActor(Card card) {
        try {
            String rank = card.getRank().getName();
            String suit = card.getSuit().getName();
            return new CardActor(textureAtlas.findRegion(suit + rank), card);
        }
        catch (NullPointerException e){
            throw new CardNotFoundException();
        }

    }
}
