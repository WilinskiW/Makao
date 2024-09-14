package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.utils.sound.SoundManager;

import java.util.ArrayList;
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

    protected List<Action> putCard(CardActor playedCard, PlayerHandGroup player, boolean alignCards) {
        List<Action> listOfActions = new ArrayList<>();
        //1. Animacja kładzenia karta
        Action addToStack = uiManager.addCardActorToStackGroup(playedCard);
        listOfActions.add(addToStack);

//        listOfActions.add(getFinishAction(playedCard, player));

        //2. Animacja sprawdzenia końca gry
//        Action endIfPlayerWon = endIfPlayerWon(player);
//        listOfActions.add(endIfPlayerWon);
//
////        //3. Animacja
//        Action alignCard = alignCardIfNeeded(player, alignCards);
//        listOfActions.add(alignCard);
//
////        //4. Sound
//        Action playSound = playPutSound(playedCard);
//        listOfActions.add(playSound);
//        listOfActions.forEach(action -> action.setTarget(playedCard));
        //Jedna animacja składa
        //return sekwencje akcji
        return listOfActions;
    }

    private Action alignCardIfNeeded(PlayerHandGroup player, boolean alignCards) {
        return new Action() {
            @Override
            public boolean act(float delta) {
                if (alignCards) {
                    player.moveCloserToStartingPosition();
                }
                return true;
            }
        };
    }

    private Action playPutSound(CardActor playedCard) {
        return new Action() {
            @Override
            public boolean act(float delta) {
                if (!playedCard.getCard().isShadow()) {
                    soundManager.playPut();
                }
                return false;
            }
        };
    }

    private Action getFinishAction(CardActor cardActor, Group group) {
        return new Action() {

            @Override
            public boolean act(float delta) {
                cardActor.setX(0);
                cardActor.setY(0);
                group.addActor(cardActor);
                this.setTarget(cardActor);
                return false;
            }
        };
    }

    protected void pull(PlayReport playReport, PlayerHandGroup player) {
        CardActor drawnCardActor = uiManager.getCardActorFactory().createCardActor(playReport.getDrawn());
        player.addActor(drawnCardActor);
        soundManager.playPull();
    }

    private Action endIfPlayerWon(PlayerHandGroup handGroup) {
        return new Action() {
            @Override
            public boolean act(float delta) {
                if (handGroup.getPlayer().checkIfPlayerHaveNoCards() && handGroup.getChildren().isEmpty()) {
                    uiManager.changeToEndingScreen(handGroup.getPlayer().toString());
                }
                return true;
            }
        };
    }

    protected void informMakao() {
        uiManager.getMakaoButton().setActive(false);
    }

    protected PlayerHandGroup humanHand() {
        return uiManager.getHumanHandGroup();
    }
}
