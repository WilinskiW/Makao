package com.wwil.makao.backend.gameplay.ai;

import com.wwil.makao.backend.gameplay.actions.Action;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.management.PlayExecutor;
import com.wwil.makao.backend.gameplay.management.PlayerManager;
import com.wwil.makao.backend.gameplay.management.RoundManager;
import com.wwil.makao.backend.model.player.Player;

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
            PlayReport report = generatePlayReport(currentPlayer);

            if (report.getPlay().getAction() == Action.MAKAO) {
                playerManager.getPlayerComebackHandler().saveCurrentIndexBeforeMakao();
                playerManager.goToHumanPlayer();
                break;
            }

            if (report.getPlay().getAction() == Action.END || currentPlayer.checkIfPlayerHaveNoCards()) {
                break;
            }
        }
    }

    private PlayReport generatePlayReport(Player currentPlayer) {
        Play play = playMaker.generatePlay(currentPlayer);
        PlayReport playReport = playExecutor.createPlayReport(currentPlayer, play);
        roundManager.getRoundReport().addPlayRaport(playReport);
        return playReport;
    }
}
