package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class PlayMaker {
    private final RoundManager roundManager;
    private final CardFinder cardFinder;
    private int amountOfPulls = 0;

    public PlayMaker(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.cardFinder = new CardFinder(roundManager.getValidator());
    }

    public List<Play> makePlays(Player player) {
        List<Play> plays = new ArrayList<>();

        // Obsługa tury - atak lub tura domyślna
        handleTurn(player, plays);

        if (!lastPlayIsEnd(plays)) {
            // Jeśli tura obronna zakończyła się, wykonaj turę domyślną
            handleTurn(player, plays);
        }

        return plays;
    }

    private void handleTurn(Player player, List<Play> plays) {
        List<Card> validCards = cardFinder.giveValidCards(player, roundManager.getDeckManager().peekStackCard());

        if (!validCards.isEmpty()) {
            plays.addAll(createPutPlays(validCards));
        } else {
            plays.addAll(pullRescueCard(player));
        }
    }

    private List<Play> createPutPlays(List<Card> cards) {
        List<Play> plays = new ArrayList<>();
        for (Card card : cards) {
            plays.add(createPutPlay(card));
        }
        plays.add(createEndPlay());
        return plays;
    }

    private Play createPutPlay(Card card){
        return new Play().setCardPlayed(card).setAction(Action.PUT);
    }

    private Play createEndPlay(){
        return new Play().setAction(Action.END);
    }

    private List<Play> pullRescueCard(Player player) {
        List<Play> plays = new ArrayList<>();
        Card rescueCard = pullCard();

        plays.add(createPullPlay(rescueCard));

        if (roundManager.getValidator().isValid(rescueCard)) {
            plays.addAll(createValidRescuePlays(player ,rescueCard));
        }
        else if (player.isAttack()) {
            pullRemainingCards(player, plays);
        } else {
            plays.add(createEndPlay());
        }
        return plays;
    }

    private Card pullCard(){
        return roundManager.getDeckManager().takeCardFromGameDeck();
    }

    private Play createPullPlay(Card card){
        return new Play().setDrawnCard(card).setAction(Action.PULL);
    }

    private List<Play> createValidRescuePlays(Player player, Card rescueCard){
        List<Play> plays = new ArrayList<>();
        plays.add(createPutPlay(rescueCard));
        plays.add(createEndPlay());

        if (player.isAttack()) {
            player.setAttack(false);
            roundManager.getPlayerManager().getNextPlayer().setAttack(true);
        }

        return plays;
    }

    private void pullRemainingCards(Player player, List<Play> plays) {
        plays.addAll(createPullPlays());
        resetAttackState(player);
    }

    private List<Play> createPullPlays(){
        List<Play> plays = new ArrayList<>();
        amountOfPulls--;
        for (int i = 0; i < amountOfPulls; i++) {
            plays.add(createPullPlay(pullCard()));
        }
        return plays;
    }

    private void resetAttackState(Player player){
        amountOfPulls = 0;
        player.setAttack(false);
    }


    private boolean lastPlayIsEnd(List<Play> plays) {
        return plays.get(plays.size() - 1).getAction() == Action.END;
    }

    public void increaseAmountOfPulls(int amount) {
        amountOfPulls += amount;
    }
}