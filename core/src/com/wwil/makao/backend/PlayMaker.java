package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class PlayMaker {
   private final MakaoBackend backend;
   private final CardFinder cardFinder;
    public PlayMaker(MakaoBackend backend) {
        this.backend = backend;
        this.cardFinder = new CardFinder(backend.getValidator());
    }
    public List<Play> makePlays(Player player){
        List<Play> plays = new ArrayList<>();

        //Dobierz karty z ataku
        if (player.isAttack()) {
            plays.add(new Play().setAction(Action.PULL));
        }

        //Znajdź karty do zagrania
        List<Card> cards = cardFinder.findCards(player.getCards(), backend.getStack().peekCard());

        if(!cards.isEmpty()){
            for(Card card : cards){
                plays.add(new Play().setCardPlayed(card).setAction(Action.PUT));
            }
        }
        else {
            //Dobierz kartę
            plays.add(new Play().setAction(Action.PULL));
        }
        return plays;
    }
}
