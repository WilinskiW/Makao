package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wwil.makao.backend.Card;
import com.wwil.makao.frontend.gameComponents.CardActor;

import java.util.ArrayList;
import java.util.List;

public class CardActorFactory {
    public CardActor createCardActor(Card card) {
        String suit = card.getSuit().getName();
        String rank = card.getRank().getName();
        TextureAtlas textureAtlas = new TextureAtlas("newCards/classicFrontCard.atlas");

        List<TextureRegion> textureRegions = new ArrayList<>();
        textureRegions.add(textureAtlas.findRegion(suit+rank));
        textureRegions.add(textureAtlas.findRegion("blanckCardGray"));

        return new CardActor(textureRegions,card);
    }

    public List<CardActor> createCardActors(List<Card> cards){
        List<CardActor> cardActors = new ArrayList<>();
        for (Card card : cards) {
            cardActors.add(createCardActor(card));
        }
        return cardActors;
    }
}
