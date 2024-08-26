package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.Gdx;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.backend.gameplay.RoundReport;
import com.wwil.makao.frontend.utils.sound.SoundManager;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;

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
        PlayReport currentPlayReport = report.getHumanLastPlayReport();
        inputManager.updateHumanAvailableActions(currentPlayReport.getPlayerState());
        switch (play.getAction()) {
            case END:
                endTurn();
                break;
            case PUT:
                useCard(currentPlayReport);
                break;
            case PULL:
                pull(currentPlayReport, humanHand(), report.whetherPlayerPulledRescue(humanHand().getPlayer()));
                break;
        }

        if (currentPlayReport.isChooserActive()) {
            showCardChooser(inputManager.getChoosenCardActor());
        }

    }

    private void endTurn() {
        uiManager.getCardChooser().setVisibility(false);
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


    private void pull(PlayReport playReport, PlayerHandGroup player, boolean hasPullBefore) {
        CardActor drawnCardActor = uiManager.getCardActorFactory().createCardActor(playReport.getDrawn());
        player.addActor(drawnCardActor);
        if (player == humanHand()) {
            inputManager.attachDragAndDrop(drawnCardActor,hasPullBefore);
        }
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
