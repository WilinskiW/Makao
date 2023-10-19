package com.wwil.makao;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CardActorFactory {
    private String[] symbols = {"As", "J", "Q", "K"};
    private String[] colors = {"pik","trefl","karo", "kier"};


    public List<CardActor> createCardActors(){
        List<CardActor> cards = new ArrayList<>();
        for(int i = 0; i < colors.length; i++){
            cards.addAll(createCardActors(colors[i]));
        }
        return cards;
    }

    private List<CardActor> createCardActors(String color) {
        List<CardActor> cards = new ArrayList<>();

        for (int i = 2; i <= 10; i++) {
            cards.add(new CardActor(new Texture(Gdx.files.internal(color + i + ".png"))));
        }

        for (int i = 0; i < symbols.length; i++) {
            cards.add(new CardActor(new Texture(Gdx.files.internal("pik" + symbols[i] + ".png"))));
        }


        return cards;
    }
}
