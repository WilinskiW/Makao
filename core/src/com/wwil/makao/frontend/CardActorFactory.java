package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.wwil.makao.backend.Card;

import java.util.ArrayList;
import java.util.List;

public class CardActorFactory {
    public CardActor createCardActor(Card card) {
        String suit = card.getSuit().getName();
        String rank = card.getRank().getName();
        String suitPath = "assets/Cards/" + suit + "/";
        Texture frontSide = new Texture(Gdx.files.internal(suitPath + suit + rank + ".png"));
        return new CardActor(frontSide,card);
    }

    public List<CardActor> createCardActors(List<Card> cards){
        List<CardActor> cardActors = new ArrayList<>();
        for (Card card : cards) {
            cardActors.add(createCardActor(card));
        }
        return cardActors;
    }
}
