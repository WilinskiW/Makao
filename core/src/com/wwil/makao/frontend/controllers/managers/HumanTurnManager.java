package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.utils.sound.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class HumanTurnManager extends TurnManager {

    public HumanTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        super(uiManager, inputManager, soundManager);
    }

    @Override
    public void show(RoundReport report) {
        PlayReport currentPlayReport = report.getHumanLastPlayReport();
        List<Action> actionToAnimate = new ArrayList<>();
        switch (currentPlayReport.getPlay().getAction()) {
            case END:
                endTurn();
                break;
            case PUT:
                useCard(currentPlayReport);
                break;
            case PULL:
                actionToAnimate.addAll(pull(currentPlayReport, humanHand()));
                break;
            case MAKAO:
                informMakao();
                break;
        }

        if (!actionToAnimate.isEmpty()) {
            actionManager.playActions(actionToAnimate);
        }

        if (currentPlayReport.getAfterState().isChooserActive() && currentPlayReport.isCardCorrect()) {
            showCardChooser(currentPlayReport);
        }

        inputManager.updateHumanAvailableActions(currentPlayReport.getAfterState());
        uiManager.changeText(currentPlayReport);
    }

    @Override
    void endTurn() {
        uiManager.getCardChooser().setVisibility(false);
        inputManager.turnOffHumanInput();
    }

    private void useCard(PlayReport playReport) {
        if (playReport.isCardCorrect()) {
            if (uiManager.getCardChooser().isVisible()) {
                putCardFromChooser(playReport);
            } else {
                putCard(playReport, humanHand());
            }
        } else if (!playReport.getAfterState().isChooserActive()) {
            uiManager.positionCardInGroup(humanHand(), inputManager.getChosenCardActor());
        }
    }

    private void putCardFromChooser(PlayReport playReport) {
        uiManager.getCardChooser().setVisibility(false);
        putCard(playReport, humanHand());
    }


    @Override
    protected List<Action> putCard(PlayReport playReport, PlayerHandGroup player) {
        if (player.getPlayer().hasOneCard()) {
            uiManager.getMakaoButton().setActive(true);
        }
        uiManager.addCardToStack(inputManager.getChosenCardActor());
        inputManager.getChosenCardActor().clearListeners();
        soundManager.playPut();
        uiManager.endGameIfPlayerWon(player);
        return null;
    }

    private void showCardChooser(PlayReport playReport) {
        CardActor stackCard = uiManager.getStackCardsGroup().peekBeforeLastCardActor();
        putCard(playReport, humanHand());
        uiManager.getCardChooser().setVisibility(true);
        uiManager.getCardChooser().getManager().setDisplayCard(stackCard, inputManager.getChosenCardActor());
    }

    @Override
    protected List<Action> pull(PlayReport playReport, PlayerHandGroup handGroup) {
        CardActor cardActor = uiManager.getGameDeckGroup().peekCardActor();
        inputManager.attachDragAndDrop(cardActor);
        inputManager.setChosenCardActor(cardActor);

        return super.pull(playReport, handGroup);
    }
}
