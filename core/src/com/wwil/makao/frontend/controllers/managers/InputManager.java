package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.Gdx;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.frontend.utils.dragAndDrop.DragAndDropManager;
import com.wwil.makao.frontend.controllers.gameplay.GameController;
import com.wwil.makao.frontend.entities.cards.CardActor;

public class InputManager {
    private final UIManager uiManager;
    private final DragAndDropManager dragAndDropManager;
    private CardActor chosenCardActor;
    private boolean inputBlockActive;

    public InputManager(GameController controller) {
        this.uiManager = controller.getUiManager();
        this.dragAndDropManager = new DragAndDropManager(controller, this, uiManager.getStackCardsGroup());
    }

    public void attachDragAndDrop(CardActor drawnCardActor) {
        drawnCardActor.setUpSideDown(false);
        dragAndDropManager.prepareDragAndDrop(drawnCardActor);
    }

    public void turnOffHumanInput() {
        inputBlockActive = true;
        uiManager.getMakaoButton().setActive(false);
        Gdx.input.setInputProcessor(null);
    }

    public void turnOnHumanInput() {
        inputBlockActive = false;
        uiManager.getMakaoButton().setActive(true);
        updateHumanAvailableActions(uiManager.getHumanHandGroup().getPlayer().getState());
        Gdx.input.setInputProcessor(uiManager.getGameplayScreen().getStage());
    }

    public void updateHumanAvailableActions(State state) {
        updateDragAndDropState(state);
        uiManager.updateButtonStates(state.isPullActive(), state.isEndActive(), state.isMakaoActive());
    }

    private void updateDragAndDropState(State state) {
        if (state.isFocusDrawnCard() && state.isPutActive()) {
            dragAndDropManager.focusRescueCard(chosenCardActor);
        } else if (state.isPutActive()) {
            dragAndDropManager.startListening();
            uiManager.changeTransparencyOfPlayerGroup(uiManager.getHumanHandGroup(), 1f);
        } else {
            dragAndDropManager.stopListening();
            uiManager.changeTransparencyOfPlayerGroup(uiManager.getHumanHandGroup(), 0.25f);
        }
    }

    public DragAndDropManager getDragAndDropManager() {
        return dragAndDropManager;
    }

    public boolean isInputBlockActive() {
        return inputBlockActive;
    }

    public void setChosenCardActor(CardActor choosenCardActor) {
        this.chosenCardActor = choosenCardActor;
    }

    public CardActor getChosenCardActor() {
        return chosenCardActor;
    }
}