package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.gameplay.RoundReport;
import com.wwil.makao.frontend.utils.sound.SoundManager;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ComputerTurnManager {
    private final UIManager uiManager;
    private final InputManager inputManager;
    private final SoundManager soundManager;

    public ComputerTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        this.uiManager = uiManager;
        this.inputManager = inputManager;
        this.soundManager = soundManager;
    }

    public void showComputersPlays(final RoundReport roundReport) {
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
                Card cardPlayed = playReport.getPlay().getCardPlayed();
                putCard(playerHand.getCardActor(cardPlayed), playerHand);
                break;
            case PULL:
                pull(playReport, playerHand);
                break;
        }
    }

    private void endTurn() {
        inputManager.turnOffHumanInput();
    }

    private void putCard(CardActor playedCard, PlayerHandGroup player) {
        uiManager.addCardActorToStackGroup(playedCard);
        soundManager.play("put.wav");
        endIfPlayerWon(player);
        player.moveCloserToStartingPosition();

    }


    private void pull(PlayReport playReport, PlayerHandGroup player) {
        CardActor drawnCardActor = uiManager.getCardActorFactory().createCardActor(playReport.getDrawn());
        player.addActor(drawnCardActor);
        soundManager.play("pull.wav");
    }

    private void endIfPlayerWon(PlayerHandGroup handGroup) {
        if (handGroup.getPlayer().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
            System.out.println(handGroup.getCardsAlignment() + " won");
            Gdx.app.exit();
        }
    }

    private PlayerHandGroup humanHand() {
        return uiManager.getHumanHandGroup();
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
