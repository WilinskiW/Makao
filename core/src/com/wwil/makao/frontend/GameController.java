package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.backend.*;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.cardChooser.CardChooserGroup;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardsGroup.PlayerHandGroup;
import com.wwil.makao.frontend.entities.cardsGroup.StackCardsGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//Komunikacja miedzy back endem a front endem

public class GameController {
    private final BackendFacade backend;
    private final UIManager uiManager;
    private final DragAndDropManager dragAndDropManager;
    private final SoundManager soundManager;
    private CardActor choosenCardActor;
    private boolean inputBlockActive;

    public GameController(GameplayScreen gameplayScreen) {
        this.backend = new BackendFacade();
        this.uiManager = new UIManager(this, gameplayScreen);
        this.dragAndDropManager = new DragAndDropManager(this);
        this.soundManager = new SoundManager();
    }

    public void executePlay(Play play) {
        RoundReport report = backend.processHumanPlay(play);
        showHumanPlay(play, report);
        if (play.getAction() == Action.END) {
            showComputersPlays(report);
        }
    }

    private void showHumanPlay(Play play, RoundReport report) {
        PlayReport currentPlayReport = report.getLastPlayReport();
        switch (play.getAction()) {
            case END:
                endHumanTurn();
                break;
            case PUT:
                useCard(currentPlayReport);
                break;
            case PULL:
                pull(currentPlayReport, humanHand(), report.hasPlayerPullBefore(humanHand().getPlayer()));
                break;
        }

        if (currentPlayReport.isChooserActive()) {
            showCardChooser(choosenCardActor);
        }

        updateDragAndDropState(currentPlayReport);
        updateButtonStates(currentPlayReport);
    }

    private void endHumanTurn() {
        uiManager.getCardChooser().setVisibility(false);
        dragAndDropManager.startListening();
        turnOffHumanInput();
    }

    private void useCard(PlayReport playReport) {
        if (playReport.isCardCorrect()) {
            if (uiManager.getCardChooser().isVisible()) {
                putCardFromChooser(playReport);
            } else {
                putCard(choosenCardActor, humanHand(), true);
            }
        } else {
            positionCardInGroup(humanHand(), choosenCardActor);
        }
    }

    private void putCardFromChooser(PlayReport playReport){
        uiManager.getCardChooser().setVisibility(false);
        playReport.setChooserActive(false);
        putCard(choosenCardActor, humanHand(), false);
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
            adjustHumanPull(drawnCardActor, hasHumanPullBefore);
        }
        player.addActor(drawnCardActor);
        soundManager.play("pull.wav");
    }

    private void adjustHumanPull(CardActor drawnCardActor, boolean hasHumanPullBefore) {
        drawnCardActor.setUpSideDown(false);
        dragAndDropManager.prepareDragAndDrop(drawnCardActor);
        if (!hasHumanPullBefore) {
            dragAndDropManager.focusRescueCard(drawnCardActor);
        } else {
            dragAndDropManager.deactivatedCardActors();
        }
    }

    private void updateDragAndDropState(PlayReport lastPlayReport) {
        if (lastPlayReport.isPutActive()) {
            dragAndDropManager.startListening();
        } else {
            dragAndDropManager.deactivatedCardActors();
        }
    }

    private void updateButtonStates(PlayReport lastPlayReport) {
        uiManager.getPullButton().setActive(lastPlayReport.isPullActive());
        uiManager.getEndTurnButton().setActive(lastPlayReport.isEndActive());
    }

    private void showCardChooser(CardActor cardPlayed) {
        CardActor stackCard = uiManager.getStackCardsGroup().peekBeforeLastCardActor();
        putCard(cardPlayed, humanHand(), false);
        uiManager.getCardChooser().setVisibility(true);
        uiManager.getCardChooser().getManager().setDisplayCard(stackCard, cardPlayed);
    }

    protected void changeCardColor(boolean isValid, CardActor cardActor) {
        if (isValid) {
            cardActor.setColor(Color.LIME);
        } else {
            cardActor.setColor(Color.SCARLET);
        }
    }

    private void endIfPlayerWon(PlayerHandGroup handGroup) {
        //Do czasu wprowadzenia menu
        if (handGroup.getPlayer().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
            System.out.println(handGroup.getCardsAlignment() + " won");
            Gdx.app.exit();
        }
    }

    private void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
        if (!human.getChildren().isEmpty()) {
            chosenCard.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(human, chosenCard);
        }
    }

    private void moveCardBackToHumanGroup(PlayerHandGroup humanGroup, CardActor card) {
        humanGroup.addActor(card);
        card.setX(card.getLastPositionBeforeRemove().x);
        card.setY(card.getLastPositionBeforeRemove().y);
        card.setZIndex((int) card.getLastPositionBeforeRemove().z);
    }

    public void executeDragStop(CardActor card) {
        if (!humanHand().getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(humanHand(), card);
        }
    }

    public void turnOffHumanInput() {
        Gdx.input.setInputProcessor(null);
        humanHand().changeTransparencyOfGroup(0.25f);
        setInputBlockActive(true);
    }
    ///////////////////////
//     Komputer
//////////////////////

    private void showComputersPlays(final RoundReport roundReport) {
        float delta = 1.50f;
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
                        turnOnHumanInput();
                    }
                }
            }, (i + 1) * delta); // Opóźnienie względem indeksu
        }
    }

    private void processComputerTurn(PlayReport playReport, PlayerHandGroup playerHand) {
        switch (playReport.getPlay().getAction()) {
            case END:
                endHumanTurn();
                break;
            case PUT:
                Card cardPlayed = playReport.getPlay().getCardPlayed();
                putCard(playerHand.getCardActor(cardPlayed), playerHand, true);
                break;
            case PULL:
                pull(playReport, playerHand, false);
                break;
        }
    }

    private void turnOnHumanInput() {
        setInputBlockActive(false);
        resetButtonsState();
        humanHand().changeTransparencyOfGroup(1f);
       // Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }

    private void resetButtonsState() {
        uiManager.getEndTurnButton().setActive(false);
        uiManager.getPullButton().setActive(true);
    }

    private PlayerHandGroup getHandGroup(Player player) {
        for (PlayerHandGroup handGroup : uiManager.getHandGroups()) {
            if (handGroup.getPlayer() == player) {
                return handGroup;
            }
        }
        return null;
    }



    public PlayerHandGroup humanHand() {
        return uiManager.getHandGroups().get(0);
    }

    public UIManager getUiManager() {
        return uiManager;
    }

    public void setInputBlockActive(boolean inputBlockActive) {
        this.inputBlockActive = inputBlockActive;
    }

    public void setChosenCardActor(CardActor choosenCardActor) {
        this.choosenCardActor = choosenCardActor;
    }

    public boolean isInputBlockActive() {
        return inputBlockActive;
    }

    public BackendFacade getBackend() {
        return backend;
    }

    public DragAndDropManager getDragAndDropManager() {
        return dragAndDropManager;
    }
    public SoundManager getSoundManager() {
        return soundManager;
    }
}