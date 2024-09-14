package com.wwil.makao.frontend.utils.dragAndDrop;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.wwil.makao.backend.gameplay.actions.ActionType;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.frontend.utils.params.GUIparams;
import com.wwil.makao.frontend.controllers.gameplay.GameController;
import com.wwil.makao.frontend.controllers.managers.InputManager;
import com.wwil.makao.frontend.controllers.managers.UIManager;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.entities.cards.StackCardsGroup;

public class DragAndDropManager {
    private final GameController gameController;
    private final UIManager uiManager;
    private final InputManager inputManager;
    private final DragAndDrop.Target target;

    public DragAndDropManager(GameController gameController, InputManager inputManager, StackCardsGroup target) {
        this.gameController = gameController;
        this.inputManager = inputManager;
        this.uiManager = gameController.getUiManager();
        this.target = prepareTarget(target);
    }

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) {
        return new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                CardActor chosenCardActor = (CardActor) source.getActor();
                inputManager.setChosenCardActor(chosenCardActor);
                if (target != null) {
                    uiManager.changeCardColor(gameController.getBackend().isDraggedCardValid(chosenCardActor),chosenCardActor);
                }
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
                inputManager.setChosenCardActor(chosenCardActor);
                gameController.executePlay(
                        new Play()
                                .setCardPlayed(chosenCardActor.getCard())
                                .setAction(ActionType.PUT));
            }
        };
    }

    public void prepareDragAndDrop(final CardActor card) {
        final DragAndDrop dragAndDrop = new DragAndDrop();
        dragAndDrop.setDragActorPosition(GUIparams.CARD_WIDTH / 2f, -GUIparams.CARD_HEIGHT / 2f);
        DragAndDrop.Source dropSource = prepareSource(card);
        dragAndDrop.addSource(dropSource);
        dragAndDrop.addTarget(target);
    }
    public void focusRescueCard(final CardActor card) {
        stopListening();
        prepareDragAndDrop(card);
        card.changeTransparency(1);
    }

    public void stopListening(){
        for (CardActor cardActor : uiManager.getHumanHandGroup().getCardActors()) {
            cardActor.clearListeners();
            cardActor.changeTransparency(0.25f);
        }
    }

    public void startListening() {
        for (CardActor cardActor : uiManager.getHumanHandGroup().getCardActors()) {
            prepareDragAndDrop(cardActor);
        }
    }

    private DragAndDrop.Source prepareSource(final CardActor card) {

        return new DragAndDrop.Source(card) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setDragActor(card);
                payload.setObject(card);
                uiManager.deployCardToStage(card);
                return payload;
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    uiManager.positionCardInHumanHandGroup(card);
                }
                super.dragStop(event, x, y, pointer, payload, target);
            }
        };
    }
}
