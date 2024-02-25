package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.frontend.gameComponents.CardActor;
import com.wwil.makao.frontend.gameComponents.PlayerHandGroup;
import com.wwil.makao.frontend.gameComponents.PullButtonActor;
import com.wwil.makao.frontend.gameComponents.StackCardsGroup;
import com.wwil.makao.frontend.parameters.CardsAlignmentParams;
import com.wwil.makao.frontend.parameters.GUIparams;

public class GameplayScreenPreparer {
    private final GameplayScreen gameplayScreen;
    private final GameController controller;

    public GameplayScreenPreparer(GameplayScreen gameplayScreen, GameController controller) {
        this.gameplayScreen = gameplayScreen;
        this.controller = controller;
    }

    public void prepareGraphicComponents() {
        gameplayScreen.setCamera(new OrthographicCamera());
        gameplayScreen.getCamera().setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);

        gameplayScreen.setViewport(new FitViewport(0, 0, gameplayScreen.getCamera()));
        gameplayScreen.setStage(new Stage(gameplayScreen.getViewport()));

        Gdx.input.setInputProcessor(gameplayScreen.getStage());
    }

    public void prepareGameComponents() {
        prepareStackCardsGroup();
        preparePullButton();
        controller.setDragTarget(prepareTarget(controller.getStackCardsGroup()));
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
        createHandGroups();
        createAllPlayersCardsActorsThatWereNotDrew();
        positionHandGroupsOnStage();
    }


    private void createAllPlayersCardsActorsThatWereNotDrew() {
        for (int i = 0; i < controller.getHandGroups().size(); i++) {
            createAllPlayerCardsActorsThatWereNotDrew(i);
        }
    }

    private void createHandGroups() {
        for (int i = 0; i < 4; i++) {
            controller.getHandGroups().add(new PlayerHandGroup(controller.getBackend().getPlayers().get(i)));
        }
        setPlayersCardActorsAlignmentParams();
    }

    private void setPlayersCardActorsAlignmentParams() {
        controller.getHandGroups().get(0).setCardsAlignment(CardsAlignmentParams.SOUTH);
        controller.getHandGroups().get(1).setCardsAlignment(CardsAlignmentParams.EAST);
        controller.getHandGroups().get(2).setCardsAlignment(CardsAlignmentParams.NORTH);
        controller.getHandGroups().get(3).setCardsAlignment(CardsAlignmentParams.WEST);
    }

    private void createAllPlayerCardsActorsThatWereNotDrew(int playerIndex) {
        for (Card card : controller.getBackend().getPlayers().get(playerIndex).getCards()) {
            if (card.isDrawn()) {
                controller.getHandGroups().get(playerIndex).addActor(controller.getCardActorFactory().createCardActor(card));
            }
        }
        if (playerIndex == 0) {
            adjustHumanCards(controller.getDragTarget());
        }
    }

    private void adjustHumanCards(DragAndDrop.Target target) {
        PlayerHandGroup group = controller.getHumanHand();
        for (CardActor card : group.getCardActors()) {
            card.setUpSideDown(false);
            prepareDragAndDrop(card, target);
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

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) { // stack - target
        //Target
        return new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) { // SOURCE - CARD
                CardActor chosenCardActor = (CardActor) source.getActor();
                controller.executeDragAction(chosenCardActor);
                return true;
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                source.getActor().setColor(Color.WHITE);
                super.reset(source, payload);
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                CardActor chosenCardActor = (CardActor) source.getActor();
                controller.executeDropAction(chosenCardActor);
            }

        };
    }

    private void prepareDragAndDrop(final CardActor card, DragAndDrop.Target target) {
        final DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(GUIparams.CARD_WIDTH / 2f, -GUIparams.CARD_HEIGHT / 2f);
        DragAndDrop.Source dropSource = prepareSource(card);
        dragAndDrop.addSource(dropSource);
        dragAndDrop.addTarget(target);
    }

    private DragAndDrop.Source prepareSource(final CardActor card) {

        return new DragAndDrop.Source(card) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                payload.setObject(card);
                prepareCardToStage();
                return payload;
            }

            private void prepareCardToStage() {
                card.saveGroup();
                card.setLastPositionBeforeRemove(new Vector3(card.getX(), card.getY(), card.getZIndex()));
                gameplayScreen.getStage().addActor(card);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    controller.executeDragStop(card);
                }
                super.dragStop(event, x, y, pointer, payload, target);
            }
        };
    }

}
