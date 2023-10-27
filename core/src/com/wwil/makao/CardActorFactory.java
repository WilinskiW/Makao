package com.wwil.makao;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CardActorFactory {
    private String[] symbols = {"AS", "J", "Q", "K"};
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
        String suitPath = "assets/Cards/" + color + "/";
        //2-10
        for (int i = 2; i <= 10; i++) {
            Texture frontSide =new Texture(Gdx.files.internal(suitPath + color + i + ".png"));
            cards.add(new CardActor(frontSide,Rank.specifyRankInWords(i),Suits.giveSuit(color)));
        }
        //specjalne
        for (String symbol : symbols) {
            Texture frontSide = new Texture(Gdx.files.internal(suitPath + color + symbol + ".png"));
            cards.add(new CardActor(frontSide, Rank.specifyRankInWords(symbol),Suits.giveSuit(color)));
        }
        return cards;
    }
}
