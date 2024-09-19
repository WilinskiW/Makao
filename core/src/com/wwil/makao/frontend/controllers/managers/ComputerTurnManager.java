package com.wwil.makao.frontend.controllers.managers;


import com.badlogic.gdx.scenes.scene2d.Action;
import com.wwil.makao.backend.gameplay.actions.ActionType;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.utils.exceptions.CardNotFoundException;
import com.wwil.makao.frontend.utils.sound.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class ComputerTurnManager extends TurnManager {
    public ComputerTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        super(uiManager, inputManager, soundManager);
    }

    @Override
    public void show(RoundReport roundReport) {
        final List<PlayReport> computerPlayReports = roundReport.getComputerPlayReports(humanHand().getPlayer());
        for (PlayReport playReport : computerPlayReports) {
            if (playReport.getPlay().getAction() == ActionType.PUT) {
                actionManager.playActions(getPlayerTurn(playReport));
            } else {
                processComputerTurn(playReport, getHandGroup(playReport.getPlayer()));
            }
        }
        inputManager.turnOnHumanInput();
    }

    private List<Action> getPlayerTurn(PlayReport playReport) {
        PlayerHandGroup handGroup = getHandGroup(playReport.getPlayer());
        return processComputerTurn(playReport, handGroup);
    }

    private List<Action> processComputerTurn(PlayReport playReport, PlayerHandGroup playerHand) {
        switch (playReport.getPlay().getAction()) {
            case END:
                endTurn();
                break;
            case PUT:
                CardActor cardActor = getCardActor(playReport, playerHand);
                return putCard(cardActor, playerHand, !cardActor.getCard().isShadow());
            case PULL:
                pull(playReport, playerHand);
                break;
        }
        uiManager.changeText(playReport);
        return null;
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

    @Override
    protected List<Action> putCard(CardActor playedCard, PlayerHandGroup handGroup, boolean alignCards) {
        List<Action> listOfActions = new ArrayList<>();
        Action addToStack = uiManager.putCardWithAnimation(playedCard, handGroup);
        listOfActions.add(addToStack);

        Action aligningAction = alignCardsIfNeeded(handGroup, alignCards);
        listOfActions.add(aligningAction);

        return listOfActions;
    }

    @Override
    protected void pull(PlayReport playReport, PlayerHandGroup player) {
        if(playReport.getDrawn() == uiManager.getGameDeckGroup().peekCardActor().getCard()){
            CardActor drawnCardActor = uiManager.getGameDeckGroup().popCardActor();
            player.addActor(drawnCardActor);

        }
        else {
            throw new CardNotFoundException("Backend - Frontend nie są synchronizowane");
        }
        soundManager.playPull();
    }
}
