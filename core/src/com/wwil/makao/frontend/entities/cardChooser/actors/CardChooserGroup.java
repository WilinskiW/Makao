package com.wwil.makao.frontend.entities.cardChooser.actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.frontend.entities.cardChooser.managers.CardChooserManager;
import com.wwil.makao.frontend.entities.cards.CardActor;
import com.wwil.makao.frontend.utils.params.GUIparams;
import com.wwil.makao.frontend.controllers.gameplay.GameController;
import com.wwil.makao.frontend.entities.cardChooser.models.CardChooserButtonTypes;

import java.util.ArrayList;
import java.util.List;

public class CardChooserGroup extends Group {
    private final GameController gameController;
    private final CardChooserManager manager;
    private final WindowActor window;
    private final CardActor displayCard;
    private final List<ArrowButtonActor> arrowButtons;
    private final PutButtonActor putButton;

    public CardChooserGroup(GameController gameController) {
        this.gameController = gameController;
        this.manager = new CardChooserManager(this);
        //Tworzenie obiektów okna
        this.window = new WindowActor();
        this.putButton = new PutButtonActor(this);
        this.displayCard = new CardActor(null);
        assignElementsToGroup();
        this.arrowButtons = createArrows();
        displayCard.setPosition(GUIparams.CHOOSER_CARD_POS.x, GUIparams.CHOOSER_CARD_POS.y);
        setBounds(GUIparams.CHOOSER_WINDOW_POS.x, GUIparams.CHOOSER_WINDOW_POS.y,
                GUIparams.CHOOSER_WINDOW_WIDTH, GUIparams.CHOOSER_WINDOW_HEIGHT);
        this.setVisible(false);
    }

    private List<ArrowButtonActor> createArrows() {
        List<ArrowButtonActor> buttons = new ArrayList<>();
        for (CardChooserButtonTypes type : CardChooserButtonTypes.values()) {
            if (type != CardChooserButtonTypes.PUT) {
                ArrowButtonActor arrow = new ArrowButtonActor(manager, type);
                buttons.add(arrow);
                addActor(arrow);
            }
        }
        return buttons;
    }

    private void assignElementsToGroup() {
        addActor(window);
        addActor(putButton);
        addActor(displayCard);
    }

    public void setVisibility(boolean visible) {
        this.setVisible(visible);
        resetArrowsVisibility();
        manager.resetIndexes();
    }

    private void resetArrowsVisibility() {
        for (ArrowButtonActor arrow : getArrowButtons()) {
            arrow.setVisible(true);
        }
    }

    public CardChooserManager getManager() {
        return manager;
    }

    public GameController getGameController() {
        return gameController;
    }

    public CardActor getDisplayCard() {
        return displayCard;
    }

    public List<ArrowButtonActor> getArrowButtons() {
        return arrowButtons;
    }
}
