package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

    public List<Card> createCards(){
        List<Card> cards = new ArrayList<>();

        for (Rank rank : Rank.values()) {
            for(Suit suit : Suit.values()){
                cards.add(new Card(rank,suit));
            }
        }
        return cards;
    }
}
