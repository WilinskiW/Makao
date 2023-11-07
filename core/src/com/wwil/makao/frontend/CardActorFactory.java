package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.wwil.makao.backend.Card;

public class CardActorFactory {
    private CardActor createCardActor(Card card) {
        String suit = card.getSuit().getName();
        String rank = card.getRank().getName();
        String suitPath = "assets/Cards/" + suit + "/";
        Texture frontSide = new Texture(Gdx.files.internal(suitPath + suit + rank + ".png"));
        return new CardActor(frontSide,card);
    }
}
