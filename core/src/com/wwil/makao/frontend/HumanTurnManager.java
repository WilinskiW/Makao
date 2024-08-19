package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.wwil.makao.backend.Play;
import com.wwil.makao.backend.PlayReport;
import com.wwil.makao.backend.RoundReport;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardsGroup.PlayerHandGroup;

public class HumanTurnManager {
    private final UIManager uiManager;
    private final InputManager inputManager;
    private final SoundManager soundManager;
    public HumanTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        this.uiManager = uiManager;
        this.inputManager = inputManager;
        this.soundManager = soundManager;
    }
    public void showHumanPlay(Play play, RoundReport report) {
        PlayReport currentPlayReport = report.getLastPlayReport();
        switch (play.getAction()) {
            case END:
                endTurn();
                break;
            case PUT:
                useCard(currentPlayReport);
                break;
            case PULL:
                pull(currentPlayReport, humanHand(), report.hasPlayerPullBefore(humanHand().getPlayer()));
                break;
        }

        if (currentPlayReport.isChooserActive()) {
            showCardChooser(inputManager.getChoosenCardActor());
        }

        inputManager.updateDragAndDropState(currentPlayReport);
        uiManager.updateButtonStates(currentPlayReport);
    }

    private void endTurn() {
        uiManager.getCardChooser().setVisibility(false);
        inputManager.getDragAndDropManager().startListening();
        inputManager.turnOffHumanInput();
    }

    private void useCard(PlayReport playReport) {
        if (playReport.isCardCorrect()) {
            if (uiManager.getCardChooser().isVisible()) {
                putCardFromChooser(playReport);
            } else {
                putCard(inputManager.getChoosenCardActor(), humanHand(), true);
            }
        } else {
            uiManager.positionCardInGroup(humanHand(), inputManager.getChoosenCardActor());
        }
    }

    private void putCardFromChooser(PlayReport playReport) {
        uiManager.getCardChooser().setVisibility(false);
        playReport.setChooserActive(false);
        putCard(inputManager.getChoosenCardActor(), humanHand(), false);
    }

    private void putCard(CardActor playedCard, PlayerHandGroup player, boolean alignCards) {
        if (player == humanHand()) {
            playedCard.clearListeners();
        }

        uiManager.addCardActorToStackGroup(playedCard);
        soundManager.play("put.wav");
        endIfPlayerWon(player);

        if (alignCards) {
            player.moveCloserToStartingPosition();
        }
    }


    private void pull(PlayReport playReport, PlayerHandGroup player, boolean hasHumanPullBefore) {
        CardActor drawnCardActor = uiManager.getCardActorFactory().createCardActor(playReport.getDrawn());
        if (player == humanHand()) {
            inputManager.handleDragAndDrop(drawnCardActor, hasHumanPullBefore);
        }
        player.addActor(drawnCardActor);
        soundManager.play("pull.wav");
    }

    private void showCardChooser(CardActor cardPlayed) {
        CardActor stackCard = uiManager.getStackCardsGroup().peekBeforeLastCardActor();
        putCard(cardPlayed, humanHand(), false);
        uiManager.getCardChooser().setVisibility(true);
        uiManager.getCardChooser().getManager().setDisplayCard(stackCard, cardPlayed);
    }

    private void endIfPlayerWon(PlayerHandGroup handGroup) {
        //Do czasu wprowadzenia menu
        if (handGroup.getPlayer().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
            System.out.println(handGroup.getCardsAlignment() + " won");
            Gdx.app.exit();
        }
    }

    private PlayerHandGroup humanHand(){
        return uiManager.getHumanHandGroup();
    }
}
