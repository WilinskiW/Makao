package com.wwil.makao.backend.states;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.Action;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class ComputerPlayFactory {
    private final RoundManager roundManager;
    private final StateManager stateManager;
    private final CardFinder cardFinder;
    private Player currentPlayer;

    ComputerPlayFactory(RoundManager roundManager, StateManager stateManager) {
        this.roundManager = roundManager;
        this.cardFinder = new CardFinder(roundManager.getValidator());
        this.stateManager = stateManager;
    }

    public List<Play> generatePlays(Player currentPlayer, RoundManager roundManager) {
        this.currentPlayer = currentPlayer;
        List<Play> plays = new ArrayList<>();

        List<Card> validCards = this.currentPlayer.getState().findValidCards(cardFinder, this.currentPlayer, roundManager.getDeckManager().peekStackCard());

        if (!validCards.isEmpty()) {
            createPutPlays(plays, validCards);
        } else if (this.currentPlayer.getState().isRescueAllow()) {
            createRescuePlays(plays);
        }
        plays.add(createEndPlay());
        return plays;
    }


    private void createPutPlays(List<Play> plays, List<Card> cards) {
        for (Card validCard : cards) {
            plays.add(createPutPlay(validCard));
        }
    }

    private Play createPutPlay(Card card) {
        return new Play().setCardPlayed(card).setAction(Action.PUT);
    }

    private void createRescuePlays(List<Play> plays) {
        Card rescueCard = pullCard(roundManager.getDeckManager());
        plays.add(createPullPlay(rescueCard));

        if (currentPlayer.getState().isValid(rescueCard, roundManager.getValidator())) {
            plays.add(createPutPlay(rescueCard));
        } else if (currentPlayer.getState().hasToPunishAfterFailRescue()) {
            punish(plays);
        }
    }

    private Card pullCard(DeckManager deckManager) {
        return deckManager.takeCardFromGameDeck();
    }

    private Play createPullPlay(Card card) {
        return new Play().setDrawnCard(card).setAction(Action.PULL);
    }

    private void punish(List<Play> plays) {
        if (roundManager.getAmountOfPulls() > 0) {
            pullRemainingCards(plays);
            generatePlaysForDefaultState(plays);
        } else {
            blockPlayer();
        }
    }

    private void pullRemainingCards(List<Play> plays) {
        stateManager.applyPullingState(currentPlayer);
        PunishState pullState = (PunishState) currentPlayer.getState();
        int amountOfPulls = pullState.amountOfPunishes;

        for (int i = 0; i < amountOfPulls; i++) {
            plays.add(createPullPlay(pullCard(roundManager.getDeckManager())));
            pullState.decreasePunishes();
        }
    }

    private void generatePlaysForDefaultState(List<Play> plays){
        stateManager.applyDefaultState(currentPlayer);
        plays.addAll(this.generatePlays(currentPlayer,roundManager));
    }

    private void blockPlayer(){
        stateManager.applyBlockedState(currentPlayer);
    }


    protected Play createEndPlay() {
        return new Play().setAction(Action.END);
    }
}
