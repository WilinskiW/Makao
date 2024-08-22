package com.wwil.makao.backend.states;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.Action;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Ability;
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

        //Sprawdzenie czy gracz jest zablokowany
        if (stateManager.isPlayerBlocked(currentPlayer)) {
           return handleBlocked(plays);
        }

        //Sprawdzenie czy gracz ma jakieś karty do zagrania
        List<Card> validCards = currentPlayer.getState().
                findValidCards(cardFinder, currentPlayer, roundManager.getDeckManager().peekStackCard());

        if (!validCards.isEmpty()) {
            //Kładzie prawidłowe karty
            createPutPlays(plays, validCards);
            handleDefenseCardPlay(plays);
        } else if (isAllowRescue()) {
            //Sprawdzamy czy stan zezwala na ratunek. Jeżeli tak to:
            //Dobieramy jedną kartę. Jeżeli zgadza trzeba ją od razu położyć.
            createRescuePlays(plays);
        }
        else{
            blockPlayer();
        }

        plays.add(createEndPlay());
        return plays;
    }


    private List<Play> handleBlocked(List<Play> plays){
        plays.add(createEndPlay());
        BlockedState blockedState = (BlockedState) currentPlayer.getState();
        blockedState.decreasePunishes();
        if(blockedState.canUnblock()){
            stateManager.applyDefaultState(currentPlayer);
        }
        return plays;
    }

    private boolean isAllowRescue() {
        if (stateManager.isDefenseState(currentPlayer)) {
            DefenseState defenseState = (DefenseState) currentPlayer.getState();
            return defenseState.getAttackingCard().getRank().getAbility() != Ability.WAIT;
        }
        return true;
    }

    private void handleDefenseCardPlay(List<Play> plays) {
        if (stateManager.isDefenseState(currentPlayer)) {
            stateManager.transferDefenceState(currentPlayer, plays.get(plays.size() - 1).getCardPlayed());
        }
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
            //Próba udana
            plays.add(createPutPlay(rescueCard));
            handleDefenseCardPlay(plays);
        } else if (stateManager.isDefenseState(currentPlayer)) {
            //Próba nieudana. Dodaj konsekwencje
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
            addDefaultStatePlays(plays);
        } else {
            blockPlayer();
        }
    }

    private void pullRemainingCards(List<Play> plays) {
        stateManager.applyPullingState(currentPlayer);
        PullingState pullState = (PullingState) currentPlayer.getState();
        int amountOfPulls = pullState.amountOfPunishes;

        for (int i = 0; i < amountOfPulls; i++) {
            plays.add(createPullPlay(pullCard(roundManager.getDeckManager())));
            pullState.decreasePunishes();
        }
    }

    private void addDefaultStatePlays(List<Play> plays) {
        stateManager.applyDefaultState(currentPlayer);
        plays.addAll(this.generatePlays(currentPlayer, roundManager));
    }

    private void blockPlayer() {
        stateManager.applyBlockedState(currentPlayer);
        BlockedState blockedState = (BlockedState) currentPlayer.getState();
        blockedState.decreasePunishes();
        if(blockedState.canUnblock()){
            stateManager.applyDefaultState(currentPlayer);
        }
    }


    protected Play createEndPlay() {
        return new Play().setAction(Action.END);
    }
}
