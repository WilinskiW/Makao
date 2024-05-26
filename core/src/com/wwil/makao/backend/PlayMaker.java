package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayMaker {
   private final MakaoBackend backend;
   private final CardValidator validator;

    public PlayMaker(MakaoBackend backend) {
        this.backend = backend;
        this.validator = backend.getValidator();
    }

    public Play generate() {
        Play play = new Play();
        if (getCurrentPlayer().isAttack()) {
            handleDefense(play);
        }

        if(play.getCardsPlayed() == null && !backend.doesEventExist()) {
            handlePlay(play);
        }
        return play;
    }

    private void handleDefense(Play play){
        List<Card> defensiveCards = getCurrentPlayer().findDefensiveCards(getCurrentPlayer().getAttacker());
        if (!defensiveCards.isEmpty()) {
            getNextPlayer().setAttacker(getCurrentPlayer().moveCardBattle());
            play.setCardsPlayed(defensiveCards).setAction(Action.PUT);
        } else {
            play.setAction(Action.PULL);
        }
    }

    private void handlePlay(Play play){
        //Znajdź karty do zagrania
        List<Card> playableCards = getPlayableCards();
        if (!playableCards.isEmpty()) {
            play.setCardsPlayed(playableCards).setAction(Action.PUT);
        }
        else {
            //Dobierz kartę
            play.setAction(Action.PULL);
        }
    }

    private List<Card> getPlayableCards() {
        List<Card> playableCards = new ArrayList<>();

        //Dodajemy karty, które mogą być zagrane
        for (Card card : getCurrentPlayer().getCards()) {
            if (validator.isValidCardForCurrentState(card)) {
                playableCards.add(card);
            }
        }

        //Jeśli nie ma kart możliwych do zagrania, zwracamy pustą listę
        if (playableCards.size() <= 1) {
            return playableCards;
        }

        //Szukamy kart o tej samej randze
        List<Card> cardsWithSameRank = getCurrentPlayer().getPlayableWithSameRank(playableCards);

        //Jeśli nie ma kart o tej samej randze, zwracamy listę kart możliwych do zagrania
        if (cardsWithSameRank.isEmpty()) {
            return Collections.singletonList(playableCards.get(new Random().nextInt(playableCards.size())));
        }
        return cardsWithSameRank;
    }

    private Player getCurrentPlayer() {
        return backend.getCurrentPlayer();
    }

    private Player getNextPlayer() {
        return backend.getNextPlayer();
    }
}
