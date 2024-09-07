package com.wwil.makao.frontend.controllers.managers;

import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.backend.gameplay.RoundReport;
import com.wwil.makao.frontend.utils.sound.SoundManager;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;

public class HumanTurnManager extends TurnManager {

    public HumanTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        super(uiManager, inputManager, soundManager);
    }

    @Override
    public void show(RoundReport report) {
        PlayReport currentPlayReport = report.getHumanLastPlayReport();
        switch (currentPlayReport.getPlay().getAction()) {
            case END:
                endTurn();
                break;
            case PUT:
                useCard(currentPlayReport);
                break;
            case PULL:
                pull(currentPlayReport, humanHand());
                break;
            case MAKAO:
                informMakao();
                break;
        }

        if (currentPlayReport.getState().isChooserActive() && currentPlayReport.isCardCorrect()) {
            showCardChooser(inputManager.getChosenCardActor());
        }

        inputManager.updateHumanAvailableActions(currentPlayReport.getState());
    }

    @Override
    void endTurn() {
        uiManager.getCardChooser().setVisibility(false);
        inputManager.turnOffHumanInput();
    }

    private void useCard(PlayReport playReport) {
        if (playReport.isCardCorrect()) {
            if (uiManager.getCardChooser().isVisible()) {
                putCardFromChooser();
            } else {
                putCard(inputManager.getChosenCardActor(), humanHand(), true);
            }
        } else if(!playReport.getState().isChooserActive()) {
            uiManager.positionCardInGroup(humanHand(), inputManager.getChosenCardActor());
        }
    }

    private void putCardFromChooser() {
        uiManager.getCardChooser().setVisibility(false);
        putCard(inputManager.getChosenCardActor(), humanHand(), false);
    }


    @Override
    protected void putCard(CardActor playedCard, PlayerHandGroup player, boolean alignCards) {
        super.putCard(playedCard, player, alignCards);
        playedCard.clearListeners();
    }

    private void showCardChooser(CardActor cardPlayed) {
        CardActor stackCard = uiManager.getStackCardsGroup().peekBeforeLastCardActor();
        putCard(cardPlayed, humanHand(), false);
        uiManager.getCardChooser().setVisibility(true);
        uiManager.getCardChooser().getManager().setDisplayCard(stackCard, cardPlayed);
    }

    @Override
    protected void pull(PlayReport playReport, PlayerHandGroup player) {
        CardActor drawnCardActor = uiManager.getCardActorFactory().createCardActor(playReport.getDrawn());
        player.addActor(drawnCardActor);
        inputManager.attachDragAndDrop(drawnCardActor);
        inputManager.setChosenCardActor(drawnCardActor);
        soundManager.play("pull.wav");
    }
}
