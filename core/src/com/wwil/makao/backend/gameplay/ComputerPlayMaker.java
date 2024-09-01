package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.model.card.Ability;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.BlockedState;
import com.wwil.makao.backend.states.DefenseState;
import com.wwil.makao.backend.states.PullingState;
import com.wwil.makao.backend.states.StateManager;

import java.util.ArrayList;
import java.util.List;

public class ComputerPlayMaker {
    private final RoundManager roundManager;
    private final StateManager stateManager;
    private final CardFinder cardFinder;
    private Player currentPlayer;

    public ComputerPlayMaker(RoundManager roundManager, StateManager stateManager) {
        this.roundManager = roundManager;
        this.cardFinder = new CardFinder(roundManager.getValidator());
        this.stateManager = stateManager;
    }

    public List<Play> generatePlays(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        List<Play> plays = new ArrayList<>();

        if (stateManager.getStateChecker().isPlayerBlocked(currentPlayer)) {
            return handleBlocked(plays);
        }

        List<Card> validCards = findValidCardsForCurrentState();

        if (!validCards.isEmpty()) {
            handleValidCards(plays, validCards);
        } else if (isAllowRescue()) {
            createRescuePlays(plays);
        } else {
            blockCurrentPlayer();
        }

        plays.add(createEndPlay());
        return plays;
    }


    private List<Play> handleBlocked(List<Play> plays) {
        plays.add(createEndPlay());
        BlockedState blockedState = (BlockedState) currentPlayer.getState();
        blockedState.decreaseAmount();
        if (blockedState.canUnblock()) {
            stateManager.getStateChanger().applyDefaultState(currentPlayer);
        }
        return plays;
    }

    private Play createEndPlay() {
        return new Play().setAction(Action.END);
    }

    private List<Card> findValidCardsForCurrentState() {
        return currentPlayer.getState().
                findValidCards(cardFinder, currentPlayer, roundManager.getDeckManager().peekStackCard());
    }

    private void handleValidCards(List<Play> plays, List<Card> validCards) {
        addValidCardPlays(plays, validCards);
        handleDefenseCardPlayIfNeeded(plays);
    }

    private void addValidCardPlays(List<Play> plays, List<Card> cards) {
        for (Card validCard : cards) {
            plays.add(createPutPlay(validCard));
        }
    }

    private Play createPutPlay(Card card) {
        return new Play().setCardPlayed(card).setAction(Action.PUT);
    }

    private void handleDefenseCardPlayIfNeeded(List<Play> plays) {
        if (isPlayerInDefenseState()) {
            stateManager.getStateChanger().transferDefenceState(currentPlayer, plays.get(plays.size() - 1).getCardPlayed());
        }
    }

    private boolean isPlayerInDefenseState() {
        return stateManager.getStateChecker().isDefenseState(currentPlayer);
    }

    private boolean isAllowRescue() {
        if (isPlayerInDefenseState()) {
            DefenseState defenseState = (DefenseState) currentPlayer.getState();
            return defenseState.getAttackingCard().getRank().getAbility() != Ability.WAIT;
        }
        return true;
    }


    private void createRescuePlays(List<Play> plays) {
        Card rescueCard = drawRescueCard();
        plays.add(createPullPlay(rescueCard));

        if (isRescueCardValid(rescueCard)) {
            handleSuccessfulRescue(plays, rescueCard);
        } else if (isPlayerInDefenseState()) {
            punish(plays);
        }
    }

    private Card drawRescueCard() {
        return pullCard(roundManager.getDeckManager());
    }

    private Card pullCard(DeckManager deckManager) {
        return deckManager.takeCardFromGameDeck();
    }

    private Play createPullPlay(Card card) {
        return new Play().setDrawnCard(card).setAction(Action.PULL);
    }

    private boolean isRescueCardValid(Card rescueCard) {
        return currentPlayer.getState().isValid(rescueCard, roundManager.getValidator());
    }

    private void handleSuccessfulRescue(List<Play> plays, Card rescueCard) {
        plays.add(createPutPlay(rescueCard));
        handleDefenseCardPlayIfNeeded(plays);
    }

    private void punish(List<Play> plays) {
        if (hasCardsToPull()) {
            executePunishmentWithPulls(plays);
        } else {
            blockCurrentPlayer();
        }
    }

    private boolean hasCardsToPull(){
        return roundManager.getPullsCount() > 0;
    }

    private void executePunishmentWithPulls(List<Play> plays){
        pullRemainingCards(plays);
        addDefaultStatePlays(plays);
    }

    private void pullRemainingCards(List<Play> plays) {
        stateManager.getStateChanger().applyPullingState(currentPlayer);
        PullingState pullState = (PullingState) currentPlayer.getState();
        int amountOfPulls = pullState.getAmountOfPunishes();

        for (int i = 0; i < amountOfPulls; i++) {
            plays.add(createPullPlay(pullCard(roundManager.getDeckManager())));
            pullState.decreaseAmount();
        }
    }

    private void addDefaultStatePlays(List<Play> plays) {
        stateManager.getStateChanger().applyDefaultState(currentPlayer);
        plays.addAll(this.generatePlays(currentPlayer));
    }

    private void blockCurrentPlayer() {
        stateManager.getStateChanger().applyBlockedState(currentPlayer);
        decreaseBlockAmount();
        if (canPlayerBeUnblocked()) {
            stateManager.getStateChanger().applyDefaultState(currentPlayer);
        }
    }

    private void decreaseBlockAmount() {
        BlockedState blockedState = (BlockedState) currentPlayer.getState();
        blockedState.decreaseAmount();
    }

    private boolean canPlayerBeUnblocked() {
        BlockedState blockedState = (BlockedState) currentPlayer.getState();
        return blockedState.canUnblock();
    }
}
