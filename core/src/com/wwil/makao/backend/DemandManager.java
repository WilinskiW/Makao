package com.wwil.makao.backend;

public class DemandManager {
    private final int performerIndex;
    private final boolean active;
    private final Card demandedCard;

    public DemandManager(int performerIndex, boolean active, Card demandedCard) {
        this.performerIndex = performerIndex;
        this.active = active;
        this.demandedCard = demandedCard;
    }

    public int getPerformerIndex() {
        return performerIndex;
    }

    public Card getDemandedCard() {
        return demandedCard;
    }

    public boolean isActive() {
        return active;
    }
}
