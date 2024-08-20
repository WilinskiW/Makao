package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;

import java.util.ArrayList;
import java.util.List;

public class DefenseState extends PlayerState {

    @Override
    protected List<Card> findCards(CardFinder cardFinder, Card stackCard) {
        return cardFinder.findCardsForDefenceState(player, stackCard);
    }

    @Override
    protected List<Play> failRescue(List<Play> plays) {
        if (roundManager.getAmountOfWaits() > 0) {
            player.changeState(new BlockedState());
            return new ArrayList<>();
        }

        plays.addAll(pullRemainingCards());
        generatePlaysForDefaultStates(plays);
        return plays;
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return false;
    }

    private List<Play> pullRemainingCards() {
        List<Play> plays = new ArrayList<>();
        for (int i = 0; i < roundManager.getAmountOfPulls(); i++) {
            plays.add(createPullPlay(pullCard(roundManager.getDeckManager())));
        }
        roundManager.setAmountOfPulls(0);
        player.changeState(new DefaultState());
        return plays;
    }

    private void generatePlaysForDefaultStates(List<Play> plays){
        player.changeState(new DefaultState());
        plays.addAll(player.getState().generatePlays(player, roundManager, cardFinder));
    }
}
