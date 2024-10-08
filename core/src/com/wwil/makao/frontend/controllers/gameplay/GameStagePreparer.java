package com.wwil.makao.frontend.controllers.gameplay;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.frontend.entities.cards.CardActorFactory;
import com.wwil.makao.frontend.entities.gameButtons.MakaoButton;
import com.wwil.makao.frontend.utils.params.CardsAlignmentParams;
import com.wwil.makao.frontend.utils.params.GUIparams;
import com.wwil.makao.frontend.controllers.facedes.BackendFacade;
import com.wwil.makao.frontend.controllers.managers.UIManager;
import com.wwil.makao.frontend.entities.gameButtons.EndTurnButton;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.cardChooser.actors.CardChooserGroup;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.entities.gameButtons.PullButton;
import com.wwil.makao.frontend.utils.text.TextContainer;

//Przygotowanie elementów graficznych ekranu
public class GameStagePreparer {
    private final UIManager uiManager;
    private final BackendFacade backend;
    private final CardActorFactory cardActorFactory;
    private final Stage stage;

    public GameStagePreparer(UIManager uiManager, BackendFacade backend) {
        this.uiManager = uiManager;
        this.backend = backend;
        this.cardActorFactory = uiManager.getCardActorFactory();
        this.stage = uiManager.getGameplayScreen().getStage();
    }

    public void execute() {
        prepareStackCardsGroup();
        prepareGameButtons();
        prepareHandGroups();
        prepareCardChooser();
        prepareTextLabel();
    }

    private void prepareStackCardsGroup() {
        uiManager.addCardActorToStackGroup(cardActorFactory.createCardActor(backend.getStackCard()));
        stage.addActor(uiManager.getStackCardsGroup());
        uiManager.getStackCardsGroup().setPosition(GUIparams.WIDTH / 2f, GUIparams.HEIGHT / 2f);
    }

    private void prepareGameButtons(){
        preparePullButton();
        prepareEndTurnButton();
        prepareMakaoButton();
    }

    private void preparePullButton() {
        GameButton pullButton = new PullButton(uiManager.getController());
        stage.addActor(pullButton);
        pullButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 100);
        uiManager.setPullButton(pullButton);
    }

    private void prepareEndTurnButton() {
        GameButton endTurnButton = new EndTurnButton(uiManager.getController());
        stage.addActor(endTurnButton);
        endTurnButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f - 25);
        uiManager.setEndTurnButton(endTurnButton);
    }

    private void prepareMakaoButton(){
        GameButton makaoButton = new MakaoButton(uiManager.getController());
        stage.addActor(makaoButton);
        makaoButton.setPosition(GUIparams.WIDTH / 2f - 300, GUIparams.HEIGHT / 2f + 50);
        uiManager.setMakaoButton(makaoButton);
    }

    private void prepareHandGroups() {
        createHandGroups();
        adjustHumanCards();
        positionHandGroupsOnStage();
    }

    private void createHandGroups() {
        for (int i = 0; i < backend.getPlayerList().size(); i++) {
            uiManager.getHandGroups().add(new PlayerHandGroup(backend.getPlayerList().get(i)));
            preparePlayer(i);
        }
    }

    private void preparePlayer(int index) {
        PlayerHandGroup handGroup = uiManager.getHandGroups().get(index);
        handGroup.setCardsAlignment(CardsAlignmentParams.getParamFromOrdinal(index));

        for (Card card : handGroup.getPlayer().getCards()) {
            CardActor cardActor = cardActorFactory.createCardActor(card);
            handGroup.addActor(cardActor);
        }
    }

    private void adjustHumanCards() {
        for (CardActor card : uiManager.getHumanHandGroup().getCardActors()) {
            card.setUpSideDown(false);
            uiManager.getController().getInputManager().getDragAndDropManager().prepareDragAndDrop(card);
        }
    }

    private void positionHandGroupsOnStage() {
        for (PlayerHandGroup handGroup : uiManager.getHandGroups()) {
            stage.addActor(handGroup);
        }
        setRotationOfHandGroups();
        setPositionOfHandGroups();
    }

    private void setRotationOfHandGroups() {
        uiManager.getHandGroups().get(1).setRotation(90);
        uiManager.getHandGroups().get(2).setRotation(180);
        uiManager.getHandGroups().get(3).setRotation(-90);
    }

    private void setPositionOfHandGroups() {
        //South
        uiManager.getHandGroups().get(0).setPosition
                (GUIparams.WIDTH / 2f,
                        0);
        //East
        uiManager.getHandGroups().get(1).setPosition(GUIparams.WIDTH + GUIparams.CARD_HEIGHT - 10,
                GUIparams.HEIGHT / 2f);
        //North
        uiManager.getHandGroups().get(2).setPosition(GUIparams.WIDTH / 2f + GUIparams.CARD_WIDTH,
                GUIparams.HEIGHT + GUIparams.CARD_HEIGHT - 5);
        //West
        uiManager.getHandGroups().get(3).setPosition(GUIparams.CARD_WIDTH / 5f - 32,
                GUIparams.HEIGHT / 2f + GUIparams.CARD_HEIGHT / 2f + 25);
    }

    private void prepareCardChooser() {
        CardChooserGroup cardChooser = new CardChooserGroup(uiManager.getController());
        stage.addActor(cardChooser);
        cardChooser.setPosition(0, 0);
        uiManager.setCardChooser(cardChooser);
    }

    private void prepareTextLabel() {
        TextContainer textContainer = new TextContainer(true);
        uiManager.setTextContainer(textContainer);
        stage.addActor(textContainer.getLabel());
    }
}
