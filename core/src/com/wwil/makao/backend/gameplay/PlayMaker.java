package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.management.StateHandler;

public class PlayMaker {
    private final RoundManager roundManager;
    private final StateHandler stateHandler;
    private final CardFinder cardFinder;

    public PlayMaker(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.stateHandler = roundManager.getStateManager().getStateHandler();
        this.cardFinder = new CardFinder(roundManager.getValidator());
    }

    public Play generatePlay(Player player) {
        Play play = new Play();
        if (isPutAvailable(player)) {
            if (tryPut(play, player)) {
                return play;
            }
        }

        if (isEndAvailable(player)) {
            return makeEndPlay(play, player);
        } else {
            return createPullPlay(play, player);
        }
    }

    private boolean isPutAvailable(Player player) {
        return player.getState().isPutActive();
    }

    private boolean tryPut(Play play, Player player) {
        Card card = findValidCardsForCurrentState(player);
        if (card != null) {
            stateHandler.updateStateAfterPut(player);
            play.setCardPlayed(card).setAction(Action.PUT);
            return true;
        }
        return false;
    }

    private Card findValidCardsForCurrentState(Player player) {
        return player.getState().findValidCard(cardFinder, player, roundManager.getDeckManager().peekStackCard());
    }

    private boolean isEndAvailable(Player player) {
        return player.getState().isEndActive();
    }

    private Play makeEndPlay(Play play, Player player){
        stateHandler.updateStateAfterEnd(player);
        return play.setAction(Action.END);
    }

    private Play createPullPlay(Play play, Player player){
        stateHandler.updateStateAfterPull(player, hasPlayerPulledBefore(player));
        return play.setDrawnCard(pullCard()).setAction(Action.PULL);
    }

    private boolean hasPlayerPulledBefore(Player player){
        return roundManager.getRoundReport().whetherPlayerPulledRescue(player);
    }

    private Card pullCard(){
        return roundManager.getDeckManager().takeCardFromGameDeck();
    }
}
