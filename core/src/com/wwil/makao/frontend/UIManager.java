package com.wwil.makao.frontend;

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


    public UIManager(GameController controller,GameplayScreen gameplayScreen) {
        this.controller = controller;
        this.gameplayScreen = gameplayScreen;
        this.stackCardsGroup = new StackCardsGroup(controller.getBackend().getDeckManager());
        this.cardActorFactory = new CardActorFactory();
        this.handGroups = new ArrayList<>();
    }

    public void prepareStage(){
        new GameStagePreparer(this, controller.getBackend()).execute();
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

    public GameController getController() {
        return controller;
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

    public List<PlayerHandGroup> getHandGroups() {
        return handGroups;
    }

    public CardChooserGroup getCardChooser() {
        return cardChooser;
    }

    public GameButton getPullButton() {
        return pullButton;
    }

    public GameButton getEndTurnButton() {
        return endTurnButton;
    }

    public CardActor peekStackCardActor() {
        return stackCardsGroup.peekCardActor();
    }

    public StackCardsGroup getStackCardsGroup() {
        return stackCardsGroup;
    }

    public CardActorFactory getCardActorFactory() {
        return cardActorFactory;
    }

    public GameplayScreen getGameplayScreen() {
        return gameplayScreen;
    }
}
