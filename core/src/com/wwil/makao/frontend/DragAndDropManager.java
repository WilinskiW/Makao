package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.wwil.makao.backend.Action;
import com.wwil.makao.backend.Play;
import com.wwil.makao.frontend.entities.CardActor;
import com.wwil.makao.frontend.entities.cardsGroup.StackCardsGroup;

public class DragAndDropManager {
    private final GameController gameController;
    private final DragAndDrop.Target target;

    public DragAndDropManager(GameController gameController) {
        this.gameController = gameController;
        this.target = prepareTarget(gameController.getUiManager().getStackCardsGroup());
    }

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) {
        return new DragAndDrop.Target(stackCardsGroup) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                CardActor chosenCardActor = (CardActor) source.getActor();
                gameController.setChosenCardActor(chosenCardActor);
                if (target != null) {
                    gameController.changeCardColor(gameController.getBackend().isDraggedCardValid(chosenCardActor),chosenCardActor);
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
                gameController.setChosenCardActor(chosenCardActor);
                gameController.executePlay(
                        new Play()
                                .setCardPlayed(chosenCardActor.getCard())
                                .setAction(Action.PUT));
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
        deactivatedCardActors();
        prepareDragAndDrop(card);
        card.changeTransparency(1);
    }

    public void deactivatedCardActors(){
        for (CardActor cardActor : gameController.humanHand().getCardActors()) {
            cardActor.clearListeners();
            cardActor.changeTransparency(0.25f);
        }
    }

    public void startListening() {
        for (CardActor cardActor : gameController.humanHand().getCardActors()) {
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
                prepareCardToStage();
                return payload;
            }

            private void prepareCardToStage() {
                card.saveGroup();
                card.setLastPositionBeforeRemove(new Vector3(card.getX(), card.getY(), card.getZIndex()));
                gameController.getUiManager().getGameplayScreen().getStage().addActor(card);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                if (target == null) {
                    gameController.executeDragStop(card);
                }
                super.dragStop(event, x, y, pointer, payload, target);
            }
        };
    }
}
