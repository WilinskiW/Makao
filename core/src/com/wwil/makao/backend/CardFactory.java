package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class CardFactory {

    public List<Card> createCards(){
        List<Card> cards = new ArrayList<>();
        List<Suit> normalSuits = Suit.getNormalSuits();

        for (Rank rank : Rank.values()) {
            if(rank.equals(Rank.JOKER)){
                continue;
            }
            for(Suit suit : normalSuits){
                cards.add(new Card(rank,suit));
            }
        }

        for(Suit jokerSuit : Suit.getColorSuits()){
            cards.add(new Card(Rank.JOKER,jokerSuit));
        }

        return cards;
    }
}
