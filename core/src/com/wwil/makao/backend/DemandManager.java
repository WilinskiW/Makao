package com.wwil.makao.backend;

public class DemandManager {
    private final int performerIndex;

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active;
    private final Card demandedCard;

    public DemandManager(int performerIndex, boolean active, Card demandedCard) {
        this.performerIndex = performerIndex;
        this.active = active;
        this.demandedCard = demandedCard;
    }

    public int getPerformerIndex() {
        return performerIndex;
    }

    public Card getCard() {
        return demandedCard;
    }

    public boolean isActive() {
        return active;
    }

    @Override //todo Test
    public String toString() {
        return "DemandManager{" +
                "performerIndex=" + performerIndex +
                ", active=" + active +
                ", demandedCard=" + demandedCard +
                '}';
    }
}
