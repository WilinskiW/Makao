package com.wwil.makao.backend.gameplay.ai;

import com.wwil.makao.backend.core.RuleParams;
import com.wwil.makao.backend.gameplay.actions.Action;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.gameplay.management.RoundManager;
import com.wwil.makao.backend.gameplay.utils.CardFinder;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.model.player.Player;

import java.util.Random;

public class PlayMaker {
    private final RoundManager roundManager;
    private final CardFinder cardFinder;

    PlayMaker(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.cardFinder = new CardFinder(roundManager.getValidator(),roundManager.getDeckManager());
    }

    public Play generatePlay(Player player) {
        Play play = new Play();
        if (isMakaoReportAvailable()) {
            if (wantToReport()) {
                return createMakaoPlay();
            }
        }

        if (isPutAvailable(player)) {
            if (tryPut(play, player)) {
                return play;
            }
        }

        if (isEndAvailable(player)) {
            return createEndPlay(play);
        } else {
            return createPullPlay(play);
        }
    }

    private boolean isMakaoReportAvailable() {
        return getHumanPlayer().isMakaoInform() && !getHumanPlayer().hasOneCard() ||
                !getHumanPlayer().isMakaoInform() && getHumanPlayer().hasOneCard();
    }

    private boolean wantToReport() {
        Random random = new Random();
        return random.nextInt(100) < RuleParams.CHANCE_FO_REPORT_FAIL_MAKAO;
    }

    private Human getHumanPlayer() {
        return roundManager.getPlayerManager().getHumanPlayer();
    }

    private Play createMakaoPlay() {
        return new Play().setAction(Action.MAKAO);
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

    private Play createEndPlay(Play play) {
        return play.setAction(Action.END);
    }

    private Play createPullPlay(Play play) {
        return play.setDrawnCard(pullCard()).setAction(Action.PULL);
    }

    private Card pullCard() {
        return roundManager.getDeckManager().takeCardFromGameDeck();
    }
}
