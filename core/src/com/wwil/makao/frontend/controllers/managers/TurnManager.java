package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.utils.exceptions.CardNotFoundException;
import com.wwil.makao.frontend.utils.sound.SoundManager;

import java.util.Collections;
import java.util.List;

public abstract class TurnManager {
    protected final UIManager uiManager;
    protected final InputManager inputManager;
    protected final SoundManager soundManager;
    protected final ActionManager actionManager;

    public TurnManager(UIManager uiManager, InputManager inputManager, SoundManager soundManager) {
        this.uiManager = uiManager;
        this.inputManager = inputManager;
        this.soundManager = soundManager;
        this.actionManager = new ActionManager();
    }

    public abstract void show(RoundReport roundReport);

    abstract void endTurn();

    abstract protected List<Action> putCard(PlayReport playReport, PlayerHandGroup handGroup);

    Action alignCardsIfNeeded(PlayerHandGroup handGroup, boolean alignCards) {
        Action action = new Action() {
            @Override
            public boolean act(float delta) {
                if (alignCards) {
                    handGroup.moveCloserToStartingPosition();
                }
                return true;
            }
        };
        action.setTarget(handGroup);
        return action;
    }

    protected List<Action> pull(PlayReport playReport, PlayerHandGroup handGroup) {
        if (playReport.getDrawn() == uiManager.getGameDeckGroup().peekCardActor().getCard()) {
            return Collections.singletonList(uiManager.pullCardWithAnimation(playReport, handGroup));
        } else {
            throw new CardNotFoundException("Backend - Frontend are not synchronized");
        }
    }

    protected void informMakao() {
        uiManager.getMakaoButton().setActive(false);
    }

    protected PlayerHandGroup humanHand() {
        return uiManager.getHumanHandGroup();
    }
}
