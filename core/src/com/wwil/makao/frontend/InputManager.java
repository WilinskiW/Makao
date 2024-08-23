package com.wwil.makao.frontend;

import com.badlogic.gdx.Gdx;
import com.wwil.makao.backend.states.PlayerState;
import com.wwil.makao.frontend.entities.CardActor;

public class InputManager {
    private final UIManager uiManager;
    private final DragAndDropManager dragAndDropManager;
    private CardActor choosenCardActor;
    private boolean inputBlockActive;


    public InputManager(GameController controller) {
        this.uiManager = controller.getUiManager();
        this.dragAndDropManager = new DragAndDropManager(controller, this, uiManager.getStackCardsGroup());
    }

    public void handleDragAndDrop(CardActor drawnCardActor, boolean hasHumanPullBefore) {
        drawnCardActor.setUpSideDown(false);
        dragAndDropManager.prepareDragAndDrop(drawnCardActor);
        if (!hasHumanPullBefore) {
            dragAndDropManager.focusRescueCard(drawnCardActor);
        } else {
            dragAndDropManager.stopListening();
        }
    }

    public void turnOnHumanInput() {
        inputBlockActive = false;
        PlayerState state = uiManager.getHumanHandGroup().getPlayer().getState();
        state.resetActionFlags();
        updateHumanAvailableActions();
        uiManager.changeTransparencyOfPlayerGroup(uiManager.getHumanHandGroup(), 1f);
        Gdx.input.setInputProcessor(uiManager.getGameplayScreen().getStage());
    }

    public void updateHumanAvailableActions(){
        PlayerState state = uiManager.getHumanHandGroup().getPlayer().getState();
        updateDragAndDropState(state);
        uiManager.updateButtonStates(state);
    }


    private void updateDragAndDropState(PlayerState state) {
        if (state.isPutActive()) {
            dragAndDropManager.startListening();
        }
        else{
            dragAndDropManager.stopListening();
        }
    }


    public void turnOffHumanInput() {
        inputBlockActive = true;
        Gdx.input.setInputProcessor(null);
        uiManager.changeTransparencyOfPlayerGroup(uiManager.getHumanHandGroup(), 0.25f);
    }

    public DragAndDropManager getDragAndDropManager() {
        return dragAndDropManager;
    }

    public boolean isInputBlockActive() {
        return inputBlockActive;
    }

    public void setChosenCardActor(CardActor choosenCardActor) {
        this.choosenCardActor = choosenCardActor;
    }

    public CardActor getChoosenCardActor() {
        return choosenCardActor;
    }
}
