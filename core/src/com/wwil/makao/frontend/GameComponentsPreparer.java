package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wwil.makao.backend.MakaoBackend;
import com.wwil.makao.backend.Card;
import com.wwil.makao.frontend.cardChooserWindow.ArrowButtonActor;
import com.wwil.makao.frontend.cardChooserWindow.CardChooserWindow;

import java.util.List;

//Przygotowanie elementów graficznych ekranu
public class GameComponentsPreparer {
    private final GameplayScreen gameplayScreen;
    private final GameController controller;
    private final MakaoBackend backend;
    private final List<PlayerHandGroup> handGroups;
    private final CardActorFactory cardActorFactory;
    private final Stage stage;

    public GameComponentsPreparer(GameplayScreen gameplayScreen, GameController controller, Stage stage) {
        this.gameplayScreen = gameplayScreen;
        this.controller = controller;
        this.backend = controller.getBackend();
        this.handGroups = controller.getHandGroups();
        this.cardActorFactory = controller.getCardActorFactory();
        this.stage = stage;
    }

    public void prepare() {
        prepareStackCardsGroup();
        preparePullButton();
        prepareHandGroups();
        showCardChooserWindow();
    }
    //todo tylko do testów
    private void showCardChooserWindow(){
        CardChooserWindow chooserWindow = new CardChooserWindow(controller);
        stage.addActor(chooserWindow);
        chooserWindow.setPosition(GUIparams.CHOOSER_WINDOW_X_POS,GUIparams.CHOOSER_WINDOW_Y_POS);

        for(ArrowButtonActor button : chooserWindow.getArrowButtons()){
            button.setZIndex(3);
            stage.addActor(button);
            button.setRotation(button.getType().getRotation());
        }
        stage.addActor(chooserWindow.getDisplayCard());
        stage.addActor(chooserWindow.getPutButton());
        chooserWindow.show(true);
    }

    private void prepareStackCardsGroup() {
        controller.addCardActorToStackGroup(createStartingCardActorForStackGroup());
        stage.addActor(controller.getStackCardsGroup());
        controller.getStackCardsGroup().setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private CardActor createStartingCardActorForStackGroup() {
        return cardActorFactory.createCardActor(backend.getStack().peekCard());
    }

    private void preparePullButton() {
        PullButtonActor pullButton = new PullButtonActor();
        pullButton.addListener(new PullButtonHandler(pullButton, controller));
        stage.addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        controller.setPullButtonActor(pullButton);
    }

    private void prepareHandGroups() {
        createHandGroups();
        adjustHumanCards();
        positionHandGroupsOnStage();
    }

    public void createHandGroups() {
        for (int i = 0; i < 4; i++) {
            handGroups.add(new PlayerHandGroup(backend.getPlayers().get(i)));
        }
        setPlayersCardActorsAlignmentParams();
        for(PlayerHandGroup handGroup : handGroups){
            for(Card card : handGroup.getPlayerHand().getCards()){
                CardActor cardActor = controller.getCardActorFactory().createCardActor(card);
                handGroup.addActor(cardActor);
            }
        }
    }

    private void setPlayersCardActorsAlignmentParams() {
        handGroups.get(0).setCardsAlignment(CardsAlignmentParams.SOUTH);
        handGroups.get(1).setCardsAlignment(CardsAlignmentParams.EAST);
        handGroups.get(2).setCardsAlignment(CardsAlignmentParams.NORTH);
        handGroups.get(3).setCardsAlignment(CardsAlignmentParams.WEST);
    }


    private void adjustHumanCards() {
        PlayerHandGroup group = controller.getHumanHand();
        for (CardActor card : group.getCardActors()) {
            card.setUpSideDown(false);
            controller.getDragAndDropManager().prepareDragAndDrop(card);
        }
    }

    private void positionHandGroupsOnStage() {
        for (PlayerHandGroup handGroup : controller.getHandGroups()) {
            stage.addActor(handGroup);
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
