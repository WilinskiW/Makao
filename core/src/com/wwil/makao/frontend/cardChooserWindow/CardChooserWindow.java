package com.wwil.makao.frontend.cardChooserWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.frontend.CardActor;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.GameController;

import java.util.ArrayList;
import java.util.List;

public class CardChooserWindow extends Actor {
    private final TextureRegion windowTexture;
    private final CardActor displayCard;
    private final GameController gameController;
    private final CardChooserManager manager;
    private final List<ArrowButtonActor> arrowButtons;
    private final PutButtonActor putButton;

    public CardChooserWindow(GameController gameController) {
        this.windowTexture = new TextureRegion
                (new Texture(Gdx.files.internal("assets/windows/CardChooserWindow.png")));
        this.gameController = gameController;
        this.manager = new CardChooserManager(this);
        //Tworzenie obiektów okna
        this.arrowButtons = createArrows();
        this.putButton = createPutButton();
        this.displayCard = createCardActor();
        displayCard.setPosition(GUIparams.CHOOSER_CARD_X_POS, GUIparams.CHOOSER_CARD_Y_POS);

        setBounds(GUIparams.CHOOSER_WINDOW_X_POS, GUIparams.CHOOSER_WINDOW_Y_POS,
                GUIparams.CHOOSER_WINDOW_WIDTH, GUIparams.CHOOSER_WINDOW_HEIGHT);
    }

    private List<ArrowButtonActor> createArrows() {
        List<ArrowButtonActor> buttons = new ArrayList<>();
        for (CardChooserButtonParams type : CardChooserButtonParams.values()) {
            if (type != CardChooserButtonParams.PUT) {
                buttons.add(new ArrowButtonActor(manager, type));
            }
        }
        return buttons;
    }

    private PutButtonActor createPutButton() {
        return new PutButtonActor(manager);
    }

    private CardActor createCardActor() {
        CardActor stackCard = gameController.getStackCardsGroup().peekCardActor();
        manager.setCurrentRankName(stackCard.getCard().getRank().getName());
        manager.setCurrentSuitName(stackCard.getCard().getSuit().getName());
        return new CardActor(stackCard.getFrontSide());
    }

    public void show(boolean isToShow) {
        setVisible(isToShow);
        getDisplayCard().setVisible(isToShow);
        for (ArrowButtonActor arrowButtonActor : getArrowButtons()) {
            arrowButtonActor.setVisible(isToShow);
        }
        getPutButton().setVisible(isToShow);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(windowTexture, getX(), getY());
        batch.setColor(Color.WHITE);
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

}
