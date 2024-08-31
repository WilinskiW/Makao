package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public class DefenseState extends PlayerState {
    private final Card attackingCard;

    public DefenseState(Player player,Card attackingCard) {
        super(player);
        this.attackingCard = attackingCard;
    }

    @Override
    void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = true;
        this.isEndActive = false;
    }

    @Override
    public List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardsForDefenceState(player,attackingCard);
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForDefence(chosenCard);
    }
    public Card getAttackingCard() {
        return attackingCard;
    }
}
