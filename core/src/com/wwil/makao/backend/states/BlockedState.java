package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.Collections;
import java.util.List;

public class BlockedState extends PlayerState {
    @Override
    public List<Play> generatePlays(Player player, RoundManager roundManager, CardFinder cardFinder) {
        return Collections.singletonList(createEndPlay());
    }

    @Override
    protected List<Card> findCards(CardFinder cardFinder, Card stackCard) {
        return null;
    }

    @Override
    protected List<Play> failRescue(List<Play> plays) {
        return null;
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return false;
    }


}
