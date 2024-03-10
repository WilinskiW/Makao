package com.wwil.makao.backend;

import java.util.List;

public class AbilityReport {
    private final int performerIndex;
    private final List<Card> toPull;
    private final Card choosenCard;

    public AbilityReport(int performerIndex, List<Card> toPull, Card choosenCard) {
        this.performerIndex = performerIndex;
        this.toPull = toPull;
        this.choosenCard = choosenCard;
    }

    public int getPerformerIndex() {
        return performerIndex;
    }

    public List<Card> getToPull() {
        return toPull;
    }

    public Card getChoosenCard() {
        return choosenCard;
    }
}
