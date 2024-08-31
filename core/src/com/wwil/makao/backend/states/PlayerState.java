package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public abstract class PlayerState {
    protected boolean isPutActive;
    protected boolean isPullActive;
    protected boolean isEndActive;
    protected Player player;

    public PlayerState(Player player) {
        this.player = player;
        if (player.isHuman()) {
            setDefaultValueOfActivations();
        }
    }

    abstract void setDefaultValueOfActivations();

    public void resetActionFlags() {
        setDefaultValueOfActivations();
    }

    public abstract boolean isValid(Card chosenCard, CardValidator validator);

    public abstract List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard);

    public boolean isPutActive() {
        return isPutActive;
    }

    public void setPutActive(boolean putActive) {
        isPutActive = putActive;
    }

    public boolean isPullActive() {
        return isPullActive;
    }

    public void setPullActive(boolean pullActive) {
        isPullActive = pullActive;
    }

    public boolean isEndActive() {
        return isEndActive;
    }

    public void setEndActive(boolean endActive) {
        isEndActive = endActive;
    }
}
