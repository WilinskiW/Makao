package com.wwil.makao.backend.model.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum Suit {
    HEART("kier"), DIAMOND("karo"), CLUB("trefl"), SPADE("pik"),
    RED("red"),BLACK("black");
    private final String name;

    public static List<Suit> getNormalSuits(){
        List<Suit> symbolSuits = new ArrayList<>();
        for(Suit suit : values()){
            if(!suit.equals(RED) && !suit.equals(BLACK)){
                symbolSuits.add(suit);
            }
        }
        return symbolSuits;
    }

    public static List<Suit> getColorSuits(){
        return Arrays.asList(RED, BLACK);
    }

    public static Suit getSuit(String nameOfSuit){
        for(Suit suit : values()){
            if(suit.name.equals(nameOfSuit)){
                return suit;
            }
        }
        return null;
    }

    public static Suit getRandom(){
        int randomIndex = new Random().nextInt(getNormalSuits().size()-1);
        return getNormalSuits().get(randomIndex);
    }

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

 /*
    Heart - Kier
    Diamond - Karo
    Club - Trefl
    Spade - Pik
 */