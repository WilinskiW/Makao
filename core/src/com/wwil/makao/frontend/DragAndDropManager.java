package com.wwil.makao.frontend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

public class DragAndDropManager {
    private final GameController gameController;
    private final DragAndDrop.Target target;

    public DragAndDropManager(GameController gameController) {
        this.gameController = gameController;
        this.target = prepareTarget(gameController.getStackCardsGroup());
    }

    private DragAndDrop.Target prepareTarget(final StackCardsGroup stackCardsGroup) { // stack - target
    //Target
    return new DragAndDrop.Target(stackCardsGroup) {
        @Override
        public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) { // SOURCE - CARD
            CardActor chosenCardActor = (CardActor) source.getActor();
            gameController.executeHumanAction(chosenCardActor, false,false);
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
            gameController.executeHumanAction(chosenCardActor, true, false);
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
                gameController.getGameplayScreen().getStage().addActor(card);
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
