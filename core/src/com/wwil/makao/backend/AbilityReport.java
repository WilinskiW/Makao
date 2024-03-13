package com.wwil.makao.backend;

import java.util.List;

public class AbilityReport {
    private final int performerIndex;
    private final List<Card> toPull;
    private final Card choosenCard;
    private final boolean blockNext;
    private final boolean demanded;

    public AbilityReport(int performerIndex, List<Card> toPull, Card choosenCard, boolean blockNext, boolean demanded) {
        this.performerIndex = performerIndex;
        this.toPull = toPull;
        this.choosenCard = choosenCard;
        this.blockNext = blockNext;
        this.demanded = demanded;
    }

    public boolean isDemanded() {
        return demanded;
    }

    public boolean isBlockNext() {
        return blockNext;
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
