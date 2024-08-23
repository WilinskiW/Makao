package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public abstract class PunishState extends PlayerState {

    int amountOfPunishes;

    protected PunishState(Player player,int amountOfPunishes) {
        super(player);
        this.amountOfPunishes = amountOfPunishes;
    }

    protected void decreasePunishes() {
        if (amountOfPunishes > 0) {
            amountOfPunishes--;
        }
    }

    @Override
    protected List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return null;
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return false;
    }
}
