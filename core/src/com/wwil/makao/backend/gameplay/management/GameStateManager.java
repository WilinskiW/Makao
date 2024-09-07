package com.wwil.makao.backend.gameplay.management;

import com.wwil.makao.backend.model.card.Card;

import java.util.ArrayList;
import java.util.List;

public class GameStateManager {
    private int pullsCount;
    private int waitsCount;
    private final List<Card> cardsPlayedInTurn;

    GameStateManager() {
        this.pullsCount = 0;
        this.waitsCount = 0;
        this.cardsPlayedInTurn = new ArrayList<>();
    }

    void increaseAmountOfPulls(int amount) {
        pullsCount += amount;
    }

    public int giveAmountOfPulls() {
        int amount = pullsCount;
        clearAmountOfPulls();
        return amount;
    }

    void clearAmountOfPulls() {
        pullsCount = 0;
    }

    public int getPullsCount() {
        return pullsCount;
    }

    public int getWaitsCount() {
        return waitsCount;
    }

    void increaseAmountOfWaits() {
        waitsCount++;
    }

    public int giveAmountOfWaits() {
        int amount = waitsCount;
        waitsCount = 0;
        return amount;
    }

    public List<Card> getCardsPlayedInTurn() {
        return cardsPlayedInTurn;
    }
}
