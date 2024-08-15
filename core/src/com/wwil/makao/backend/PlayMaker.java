package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class PlayMaker {
    private final MakaoBackend backend;
    private final CardFinder cardFinder;
    private List<Play> plays;
    private int amountOfPulls = 0;

    public PlayMaker(MakaoBackend backend) {
        this.backend = backend;
        this.cardFinder = new CardFinder(backend.getValidator());
    }

    public List<Play> makePlays(Player player) {
        plays = new ArrayList<>();

        // Obsługa tury - atak lub tura domyślna
        handleTurn(player);

        if (lastPlayIsEnd()) {
            return plays;
        }

        // Jeśli tura obronna zakończyła się, wykonaj turę domyślną
        handleTurn(player);

        return plays;
    }

    private void handleTurn(Player player) {
        List<Card> validCards = cardFinder.giveValidCards(player, backend.getStack().peekCard());

        if (!validCards.isEmpty()) {
            plays.addAll(createPutPlays(validCards));
        } else {
            plays.addAll(pullRescueCard(player));
        }
    }

    private List<Play> createPutPlays(List<Card> cards) {
        List<Play> plays = new ArrayList<>();
        for (Card card : cards) {
            plays.add(new Play().setCardPlayed(card).setAction(Action.PUT));
        }
        plays.add(new Play().setAction(Action.END));
        return plays;
    }

    private List<Play> pullRescueCard(Player player) {
        List<Play> plays = new ArrayList<>();
        Card rescueCard = backend.takeCardFromGameDeck();

        plays.add(new Play().setDrawnCard(rescueCard).setAction(Action.PULL));

        if (backend.getValidator().isValid(rescueCard)) {
            plays.add(new Play().setCardPlayed(rescueCard).setAction(Action.PUT));
            plays.add(new Play().setAction(Action.END));

            if (player.isAttack()) {
                player.setAttack(false);
                backend.getNextPlayer().setAttack(true);
            }

            return plays;
        }

        if (player.isAttack()) {
            pullTheRestOfTheCards(player);
        } else {
            plays.add(new Play().setAction(Action.END));
        }
        return plays;
    }

    private void pullTheRestOfTheCards(Player player) {
        amountOfPulls--;
        for (int i = 0; i < amountOfPulls; i++) {
            plays.add(new Play().setAction(Action.PULL).setDrawnCard(backend.takeCardFromGameDeck()));
        }
        amountOfPulls = 0;
        player.setAttack(false);
    }


    private boolean lastPlayIsEnd() {
        return plays.get(plays.size() - 1).getAction() == Action.END;
    }

    public void increaseAmountOfPulls(int amount) {
        amountOfPulls += amount;
    }
}