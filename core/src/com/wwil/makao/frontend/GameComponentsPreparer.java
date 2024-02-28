package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.wwil.makao.frontend.gameComponents.CardActor;
import com.wwil.makao.frontend.gameComponents.PlayerHandGroup;
import com.wwil.makao.frontend.gameComponents.PullButtonActor;
import com.wwil.makao.frontend.parameters.GUIparams;
//Przygotowanie elementow graficznych ekranu
public class GameComponentsPreparer {
    private final GameplayScreen gameplayScreen;
    private final GameController controller;

    public GameComponentsPreparer(GameplayScreen gameplayScreen, GameController controller) {
        this.gameplayScreen = gameplayScreen;
        this.controller = controller;
    }

    public void prepare() {
        prepareStackCardsGroup();
        preparePullButton();
        prepareHandGroups();
    }

    private void prepareStackCardsGroup() {
        controller.addCardActorToStackGroup(controller.createStartingCardActorForStackGroup());
        gameplayScreen.getStage().addActor(controller.getStackCardsGroup());
        controller.getStackCardsGroup().setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }


    private void preparePullButton() {
        PullButtonActor pullButton = new PullButtonActor();
        gameplayScreen.getStage().addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        controller.setPullButtonActor(pullButton);
    }

    private void prepareHandGroups() {
        controller.createHandGroups();
        adjustHumanCards(controller.getDragAndDropManager().getTarget());
        positionHandGroupsOnStage();
    }


    private void adjustHumanCards(DragAndDrop.Target target) {
        PlayerHandGroup group = controller.getHumanHand();
        for (CardActor card : group.getCardActors()) {
            card.setUpSideDown(false);
            controller.getDragAndDropManager().prepareDragAndDrop(card,target);
        }
    }

    private void positionHandGroupsOnStage() {
        for (PlayerHandGroup handGroup : controller.getHandGroups()) {
            gameplayScreen.getStage().addActor(handGroup);
        }
        setRotationOfHandGroups();
        setPositionOfHandGroups();
    }

    private void setRotationOfHandGroups() {
        controller.getHandGroups().get(1).setRotation(90);
        controller.getHandGroups().get(2).setRotation(180);
        controller.getHandGroups().get(3).setRotation(-90);
    }

    private void setPositionOfHandGroups() {
        // TODO: 05.02.2024 Poprawić ustawianie grupy
        //South
        controller.getHandGroups().get(0).setPosition
                (GUIparams.WIDTH / 2f,
                        0);
        //East
        controller.getHandGroups().get(1).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 10,
                GUIparams.HEIGHT / 2.0f);
        //North
        controller.getHandGroups().get(2).setPosition(GUIparams.WIDTH / 2f + GUIparams.CARD_WIDTH,
                GUIparams.HEIGHT + GUIparams.CARD_HEIGHT - 25);
        //West
        controller.getHandGroups().get(3).setPosition(GUIparams.CARD_WIDTH / 5f - 32,
                GUIparams.HEIGHT / 2f + GUIparams.CARD_HEIGHT / 2f + 25);
    }
}
