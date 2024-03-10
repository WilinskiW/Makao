package com.wwil.makao.backend;

import java.util.List;

public class AbilityReport {
    private final int performerIndex;
    private final List<Card> toPull;
    private final Card choosenCard;
    private final boolean blockNext;

    public AbilityReport(int performerIndex, List<Card> toPull, Card choosenCard, boolean blockNext) {
        this.performerIndex = performerIndex;
        this.toPull = toPull;
        this.choosenCard = choosenCard;
        this.blockNext = blockNext;
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
