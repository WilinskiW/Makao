package com.wwil.makao.backend.states.impl.punish;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

import java.util.ArrayList;

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
        amountOfPunishes--;
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return null;
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return false;
    }

    public int getAmountOfPunishes() {
        return amountOfPunishes;
    }
}
