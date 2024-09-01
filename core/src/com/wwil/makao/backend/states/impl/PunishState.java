package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

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

    public PunishState(int amountOfPunishes, boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.amountOfPunishes = amountOfPunishes;
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
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
