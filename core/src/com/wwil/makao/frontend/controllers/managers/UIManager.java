package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.wwil.makao.frontend.controllers.gameplay.GameController;
import com.wwil.makao.frontend.controllers.gameplay.GameStagePreparer;
import com.wwil.makao.frontend.controllers.gameplay.GameplayScreen;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cardChooser.actors.CardChooserGroup;
import com.wwil.makao.frontend.entities.cards.PlayerHandGroup;
import com.wwil.makao.frontend.entities.cards.StackCardsGroup;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;
import com.wwil.makao.frontend.entities.cards.CardActorFactory;
import com.wwil.makao.frontend.utils.exceptions.CardNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UIManager {
    private final GameController controller;
    private final GameplayScreen gameplayScreen;
    private final StackCardsGroup stackCardsGroup;
    private final CardActorFactory cardActorFactory;
    private final List<PlayerHandGroup> handGroups;
    private CardChooserGroup cardChooser;
    private GameButton pullButton;
    private GameButton endTurnButton;
    private GameButton makaoButton;


    public UIManager(GameController controller, GameplayScreen gameplayScreen) {
        this.controller = controller;
        this.gameplayScreen = gameplayScreen;
        this.stackCardsGroup = new StackCardsGroup();
        this.cardActorFactory = new CardActorFactory();
        this.handGroups = new ArrayList<>();
    }

    public void prepareStage() {
        new GameStagePreparer(this, controller.getBackend()).execute();
    }

    public void updateButtonStates(boolean isPullActive, boolean isEndActive, boolean isMakaoActive) {
        pullButton.setActive(isPullActive);
        endTurnButton.setActive(isEndActive);
        makaoButton.setActive(isMakaoActive);
    }

    public void changeCardColor(boolean isValid, CardActor cardActor) {
        if (isValid) {
            cardActor.setColor(Color.LIME);
        } else {
            cardActor.setColor(Color.SCARLET);
        }
    }

    public void positionCardInGroup(PlayerHandGroup human, CardActor chosenCard) {
        if (!human.getChildren().isEmpty()) {
            chosenCard.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(human, chosenCard);
        }
    }

    private void moveCardBackToHumanGroup(PlayerHandGroup humanGroup, CardActor card) {
        humanGroup.addActor(card);
        card.setX(card.getLastPositionBeforeRemove().x);
        card.setY(card.getLastPositionBeforeRemove().y);
        card.setZIndex((int) card.getLastPositionBeforeRemove().z);
    }

    public void deployCardToStage(CardActor card){
        card.saveGroup();
        card.setLastPositionBeforeRemove(new Vector3(card.getX(), card.getY(), card.getZIndex()));
        gameplayScreen.getStage().addActor(card);
    }

    public GameController getController() {
        return controller;
    }

    public void positionCardInHumanHandGroup(CardActor card) {
        if (!getHumanHandGroup().getChildren().isEmpty()) {
            card.beLastInGroup();
        } else {
            moveCardBackToHumanGroup(getHumanHandGroup(), card);
        }
    }

    public void addCardActorToStackGroup(CardActor cardActor) {
        try {
            gameplayScreen.getStage().addActor(cardActor);
        } catch (NullPointerException e) {
            throw new CardNotFoundException();
        }
        if(cardActor.getCard().isShadow()){
            cardActor.setColor(Color.GRAY);
        }
        cardActor.setUpSideDown(false);
        stackCardsGroup.addActor(cardActor);
    }

    public void changeTransparencyOfPlayerGroup(PlayerHandGroup playerHandGroup, float alpha) {
        for (CardActor cardActor : playerHandGroup.getCardActors()) {
            cardActor.changeTransparency(alpha);
        }
    }

    public GameplayScreen getGameplayScreen() {
        return gameplayScreen;
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public CardActor peekStackCardActor() {
        return stackCardsGroup.peekCardActor();
    }

    public CardActorFactory getCardActorFactory() {
        return cardActorFactory;
    }

    public List<PlayerHandGroup> getHandGroups() {
        return handGroups;
    }

    public PlayerHandGroup getHumanHandGroup(){
        return handGroups.get(0);
    }

    public CardChooserGroup getCardChooser() {
        return cardChooser;
    }

    public void setCardChooser(CardChooserGroup cardChooser) {
        this.cardChooser = cardChooser;
    }
    public void setPullButton(GameButton pullButton) {
        this.pullButton = pullButton;
    }

    public void setEndTurnButton(GameButton endTurnButton) {
        this.endTurnButton = endTurnButton;
    }

    public GameButton getMakaoButton() {
        return makaoButton;
    }

    public void setMakaoButton(GameButton makaoButton) {
        this.makaoButton = makaoButton;
    }
}
