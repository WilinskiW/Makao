package com.wwil.makao.backend.states.impl.rescue;

import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.utils.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.base.RescueState;
import com.wwil.makao.backend.states.management.StateChanger;

public class NormalRescueState extends RescueState {
    public NormalRescueState() {
        setDefaultValueOfActivations();
    }

    public NormalRescueState(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
    }

    @Override
    public State copyState() {
        return new NormalRescueState(isPutActive, isPullActive, isEndActive);
    }


    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForNormalState(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findForNormalState(player, stackCard);
    }

    @Override
    public void handleEnd(Player player, StateChanger changer) {
        changer.applyNormalState(player);
        changer.setActions(player, false, false, false);
    }
}
