package com.wwil.makao.frontend.cardChooserWindow;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.wwil.makao.frontend.CardActor;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.GameController;

import java.util.ArrayList;
import java.util.List;

public class CardChooserGroup extends Group {
    private final GameController gameController;
    private final CardChooserManager manager;
    private final WindowActor window;
    private CardActor displayCard;
    private final List<ArrowButtonActor> arrowButtons;
    private final PutButtonActor putButton;
    private boolean visible = false;

    public CardChooserGroup(GameController gameController) {
        this.gameController = gameController;
        this.manager = new CardChooserManager(this);
        //Tworzenie obiektów okna
        this.window = new WindowActor();
        addActor(window);
        this.arrowButtons = createArrows();
        this.putButton = new PutButtonActor(this);
        addActor(putButton);
        this.displayCard = new CardActor(null);
        setAttributesFromStackCard();
        addActor(displayCard);
        displayCard.setPosition(GUIparams.CHOOSER_CARD_X_POS, GUIparams.CHOOSER_CARD_Y_POS);
        setBounds(GUIparams.CHOOSER_WINDOW_X_POS, GUIparams.CHOOSER_WINDOW_Y_POS,
                GUIparams.CHOOSER_WINDOW_WIDTH, GUIparams.CHOOSER_WINDOW_HEIGHT);
    }

    private List<ArrowButtonActor> createArrows() {
        List<ArrowButtonActor> buttons = new ArrayList<>();
        for (CardChooserButtonParams type : CardChooserButtonParams.values()) {
            if (type != CardChooserButtonParams.PUT) {
                ArrowButtonActor arrow = new ArrowButtonActor(manager, type);
                buttons.add(new ArrowButtonActor(manager, type));
                addActor(arrow);
            }
        }
        return buttons;
    }

    public void show(boolean isVisible) {
        for(Actor actor : getChildren()){
            actor.setVisible(isVisible);
        }
    }

    private void setAttributesFromStackCard() {
        CardActor stackCard = gameController.getStackCardsGroup().peekCardActor();
        manager.setCurrentRankName(stackCard.getCard().getRank().getName());
        manager.setCurrentSuitName(stackCard.getCard().getSuit().getName());
        displayCard.setFrontSide(stackCard.getFrontSide());
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

    public PutButtonActor getPutButton() {
        return putButton;
    }

    public CardChooserManager getManager() {
        return manager;
    }

    public void setDisplayCard(CardActor displayCard) {
        this.displayCard = displayCard;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public WindowActor getWindow() {
        return window;
    }
}
