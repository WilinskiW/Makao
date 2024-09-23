package com.wwil.makao.frontend.controllers.managers;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.wwil.makao.backend.gameplay.actions.ActionType;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.utils.sound.SoundManager;

import java.util.ArrayList;
import java.util.List;

public class ComputerTurnManager extends TurnManager {
    public ComputerTurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        super(uiManager, inputManager, soundManager);
    }

    @Override
    public void show(RoundReport roundReport) {
        List<PlayReport> computerPlayReports = roundReport.getComputerPlayReports();
        for (PlayReport playReport : computerPlayReports) {
            List<Action> animationSequence = processComputerTurn(playReport, getHandGroup(playReport.getPlayer()));
            if (animationSequence != null) {
                actionManager.playActions(animationSequence);
            }
        }
        inputManager.turnOnHumanInput();
    }


    private List<Action> processComputerTurn(PlayReport playReport, PlayerHandGroup playerHand) {
        switch (playReport.getPlay().getAction()) {
            case END:
                endTurn();
                return null;
            case PUT:
                return putCard(playReport, playerHand);
            case PULL:
                return pull(playReport, playerHand);
            default:
                return null;
        }
    }

    @Override
    void endTurn() {
        inputManager.turnOffHumanInput();
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
    protected List<Action> putCard(PlayReport playReport, PlayerHandGroup handGroup) {
        List<Action> listOfActions = new ArrayList<>();
        CardActor cardActor = getCardActor(playReport, handGroup);

        Action addToStack = uiManager.putCardWithAnimation(playReport, cardActor, handGroup);
        listOfActions.add(addToStack);

        Action aligningAction = alignCardsIfNeeded(handGroup, !cardActor.getCard().isShadow());
        listOfActions.add(aligningAction);

        return listOfActions;
    }

    private CardActor getCardActor(PlayReport playReport, PlayerHandGroup playerHand) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        if (cardPlayed.isShadow()) {
            return uiManager.getCardActorFactory().createCardActor(playReport.getPlay().getCardPlayed());
        }
        return playerHand.getCardActor(cardPlayed);
    }
}
