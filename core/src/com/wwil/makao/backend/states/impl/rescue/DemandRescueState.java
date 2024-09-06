package com.wwil.makao.backend.states.impl.rescue;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.impl.rescue.NormalRescueState;

public class DemandRescueState extends NormalRescueState {
    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForDefenceState(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findBestCardForDefenceState(player);
    }
}
