package com.wwil.makao.backend.states.impl.rescue;

import com.wwil.makao.backend.gameplay.utils.CardFinder;
import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.base.RescueState;

public class DemandRescueState extends RescueState {
    public DemandRescueState() {
        setDefaultValueOfActivations();
    }

    public DemandRescueState(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
    }

    @Override
    public State copyState() {
        return new DemandRescueState(isPutActive, isPullActive, isEndActive);
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForDefenceState(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardForDefenceState(player);
    }
}
