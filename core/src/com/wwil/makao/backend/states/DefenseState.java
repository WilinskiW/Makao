package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public class DefenseState extends PlayerState {
    private final Card attackingCard;

    public DefenseState(Card attackingCard) {
        this.attackingCard = attackingCard;
    }

    @Override
    protected List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardsForDefenceState(player,attackingCard);
    }
    @Override
    PlayReport setActionActivations(PlayReport playReport) {
        return playReport.setPutActive().setPullActive();
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return false;
    }

    Card getAttackingCard() {
        return attackingCard;
    }
}
