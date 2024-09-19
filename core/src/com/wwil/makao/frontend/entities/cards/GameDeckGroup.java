package com.wwil.makao.frontend.entities.cards;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.core.GameDeck;
import com.wwil.makao.backend.model.card.Card;

public class GameDeckGroup extends Group {
    private final GameDeck gameDeck;

    public GameDeckGroup(GameDeck gameDeck) {
        this.gameDeck = gameDeck;
        CardActorFactory cardActorFactory = new CardActorFactory();
        for(Card card : gameDeck.getCards()){
            cardActorFactory.createCardActor(card).setUpSideDown(true);
        }
    }

    @Override
    public void addActor(Actor actor) {
        if(!getChildren().isEmpty()){
            float lastX = getChildren().peek().getX();
            actor.setX(lastX + 7);
        }
        super.addActor(actor);
    }

    public CardActor popCardActor(){
        return (CardActor) getChildren().pop();
    }

    public CardActor peekCardActor(){
        return (CardActor) getChildren().peek();
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }
}
