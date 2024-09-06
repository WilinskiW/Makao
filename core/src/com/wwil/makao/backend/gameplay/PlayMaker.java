package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;

public class PlayMaker {
    private final RoundManager roundManager;
    private final CardFinder cardFinder;

    PlayMaker(RoundManager roundManager) {
        this.roundManager = roundManager;
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
            return makeEndPlay(play);
        } else {
            return createPullPlay(play);
        }
    }

    private boolean isPutAvailable(Player player) {
        return player.getState().isPutActive();
    }

    private boolean tryPut(Play play, Player player) {
        Card card = findValidCardsForCurrentState(player);
        if (card != null) {
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

    private Play makeEndPlay(Play play) {
        return play.setAction(Action.END);
    }

    private Play createPullPlay(Play play) {
        return play.setDrawnCard(pullCard()).setAction(Action.PULL);
    }

    private Card pullCard() {
        return roundManager.getDeckManager().takeCardFromGameDeck();
    }
}
