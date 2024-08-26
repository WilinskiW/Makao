package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.Gdx;
import com.wwil.makao.backend.states.PlayerState;
import com.wwil.makao.frontend.utils.dragAndDrop.DragAndDropManager;
import com.wwil.makao.frontend.controllers.gameplay.GameController;
import com.wwil.makao.frontend.entities.cards.CardActor;

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

    public void turnOffHumanInput() {
        inputBlockActive = true;
        Gdx.input.setInputProcessor(null);
    }

    public void turnOnHumanInput() {
        inputBlockActive = false;
        updateHumanAvailableActions(uiManager.getHumanHandGroup().getPlayer().getState());
        Gdx.input.setInputProcessor(uiManager.getGameplayScreen().getStage());
    }

    public void updateHumanAvailableActions(PlayerState state){
        updateDragAndDropState(state);
        uiManager.updateButtonStates(state);
    }


    private void updateDragAndDropState(PlayerState state) {
        if (state.isPutActive()) {
            dragAndDropManager.startListening();
            uiManager.changeTransparencyOfPlayerGroup(uiManager.getHumanHandGroup(), 1f);
        }
        else{
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
        this.choosenCardActor = choosenCardActor;
    }

    public CardActor getChoosenCardActor() {
        return choosenCardActor;
    }
}
