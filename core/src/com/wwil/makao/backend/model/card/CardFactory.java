package com.wwil.makao.backend.model.card;

import java.util.LinkedList;
import java.util.List;

public class CardFactory {
    public LinkedList<Card> createCards(){
        LinkedList<Card> cards = new LinkedList<>();
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
