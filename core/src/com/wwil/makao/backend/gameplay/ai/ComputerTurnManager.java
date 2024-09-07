package com.wwil.makao.backend.gameplay.ai;

import com.wwil.makao.backend.gameplay.actions.Action;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.management.PlayExecutor;
import com.wwil.makao.backend.gameplay.management.RoundManager;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.gameplay.management.PlayerManager;

public class ComputerTurnManager {
    private final PlayMaker playMaker;
    private final RoundManager roundManager;
    private final PlayExecutor playExecutor;
    private final PlayerManager playerManager;

    public ComputerTurnManager(RoundManager roundManager) {
        this.playMaker = new PlayMaker(roundManager);
        this.roundManager = roundManager;
        this.playExecutor = roundManager.getPlayExecutor();
        this.playerManager = roundManager.getPlayerManager();
    }

    public void manageComputerTurn() {
        Player currentPlayer = playerManager.getCurrentPlayer();
        while (true) {
            Play play = playMaker.generatePlay(currentPlayer);
            PlayReport playReport = playExecutor.createPlayReport(currentPlayer, play);
            roundManager.getRoundReport().addPlayRaport(playReport);

            if (play.getAction() == Action.MAKAO) {
                playerManager.getPlayerComebackHandler().saveCurrentIndexBeforeMakao();
                playerManager.goToHumanPlayer();
                break;
            }

            if (play.getAction() == Action.END || currentPlayer.checkIfPlayerHaveNoCards()) {
                break;
            }
        }
    }
}
