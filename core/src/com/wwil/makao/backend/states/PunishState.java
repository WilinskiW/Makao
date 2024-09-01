package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class PunishState implements State {
    protected int amountOfPunishes;
    protected boolean isPutActive;
    protected boolean isPullActive;
    protected boolean isEndActive;
    protected PunishState(int amountOfPunishes) {
        this.amountOfPunishes = amountOfPunishes;
    }
    public void decreaseAmount() {
        if (amountOfPunishes > 0) {
            amountOfPunishes--;
        }
    }

    @Override
    public List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return new ArrayList<>();
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return false;
    }

    public int getAmountOfPunishes() {
        return amountOfPunishes;
    }
}
