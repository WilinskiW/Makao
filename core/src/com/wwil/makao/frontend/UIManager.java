package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardChooser.CardChooserGroup;
import com.wwil.makao.frontend.entities.cardsGroup.PlayerHandGroup;
import com.wwil.makao.frontend.entities.cardsGroup.StackCardsGroup;
import com.wwil.makao.frontend.entities.gameButtons.GameButton;

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


    public UIManager(GameController controller, GameplayScreen gameplayScreen) {
        this.controller = controller;
        this.gameplayScreen = gameplayScreen;
        this.stackCardsGroup = new StackCardsGroup(controller.getBackend().getDeckManager());
        this.cardActorFactory = new CardActorFactory();
        this.handGroups = new ArrayList<>();
    }

    public void prepareStage() {
        new GameStagePreparer(this, controller.getBackend()).execute();
    }

    public void updateButtonStates(PlayReport lastPlayReport) {
        pullButton.setActive(lastPlayReport.isPullActive());
        endTurnButton.setActive(lastPlayReport.isEndActive());
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

    public GameButton getPullButton() {
        return pullButton;
    }

    public void setPullButton(GameButton pullButton) {
        this.pullButton = pullButton;
    }

    public GameButton getEndTurnButton() {
        return endTurnButton;
    }

    public void setEndTurnButton(GameButton endTurnButton) {
        this.endTurnButton = endTurnButton;
    }
}
