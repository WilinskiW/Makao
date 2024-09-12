package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.utils.sound.SoundManager;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ComputerTurnManager extends TurnManager {
    public ComputerTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        super(uiManager, inputManager, soundManager);
    }

    @Override
    public void show(RoundReport roundReport) {
        float delta = 1.25f;
        final List<PlayReport> computerPlayReports = roundReport.getComputerPlayReports(humanHand().getPlayer());
        final int numberOfComputers = computerPlayReports.size();
        final AtomicInteger completedComputers = new AtomicInteger(0);

        for (int i = 0; i < computerPlayReports.size(); i++) {
            final PlayReport playReport = computerPlayReports.get(i);
            final PlayerHandGroup handGroup = getHandGroup(playReport.getPlayer());

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    processComputerTurn(playReport, handGroup);
                    // Sprawdź, czy to był ostatni ruch komputera
                    if (completedComputers.incrementAndGet() == numberOfComputers) {
                        inputManager.turnOnHumanInput();
                    }
                }
            }, (i + 1) * delta); // Opóźnienie względem indeksu
        }
    }

    private void processComputerTurn(PlayReport playReport, PlayerHandGroup playerHand) {
        switch (playReport.getPlay().getAction()) {
            case END:
                endTurn();
                break;
            case PUT:
                CardActor cardActor = getCardActor(playReport, playerHand);
                putCard(cardActor, playerHand, !cardActor.getCard().isShadow());
                break;
            case PULL:
                pull(playReport, playerHand);
                break;
        }
        uiManager.changeText(playReport);
    }

    @Override
    void endTurn() {
        inputManager.turnOffHumanInput();
    }

    private CardActor getCardActor(PlayReport playReport, PlayerHandGroup playerHand) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        if (cardPlayed.isShadow()) {
            return uiManager.getCardActorFactory().createCardActor(playReport.getPlay().getCardPlayed());
        }
        return playerHand.getCardActor(cardPlayed);
    }


    private PlayerHandGroup getHandGroup(Player player) {
        for (PlayerHandGroup handGroup : uiManager.getHandGroups()) {
            if (handGroup.getPlayer() == player) {
                return handGroup;
            }
        }
        return null;
    }
}
