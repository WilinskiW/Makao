package com.wwil.makao.frontend.cardChooserWindow;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.frontend.CardActor;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.GameController;

import java.util.ArrayList;
import java.util.List;

public class CardChooserWindow extends Actor {
    private final TextureRegion window;
    private final TextureAtlas cardAtlas;
    private TextureRegion frontside;
    private CardActor cardActor;
    private final GameController gameController;
    private List<ArrowButtonActor> buttons;
    private int currentCardIndex = 0;

    public CardChooserWindow(GameController gameController) {
        this.window = new TextureRegion
                (new Texture(Gdx.files.internal("assets/windows/CardChooserWindow.png")));
        this.cardAtlas = new TextureAtlas("cards/classicFrontCard.atlas");
        this.frontside = cardAtlas.getRegions().get(0);
        this.gameController = gameController;
        createButtons();
        createCardActor();
        setBounds(GUIparams.CHOOSER_WINDOW_X_POS, GUIparams.CHOOSER_WINDOW_Y_POS,
                GUIparams.CHOOSER_WINDOW_WIDTH, GUIparams.CHOOSER_WINDOW_HEIGHT);
    }

    private void createCardActor(){
        this.cardActor = new CardActor(frontside);
        cardActor.setPosition(855,392); //todo
    }

    private void createButtons(){
        List<ArrowButtonActor> buttons = new ArrayList<>();
        for(CardChooserButtonParams type : CardChooserButtonParams.values()){
            buttons.add(new ArrowButtonActor(this,type));
        }
        this.buttons = buttons;
    }

    public void show(boolean isToShow){
        setVisible(isToShow);
        getCardActor().setVisible(isToShow);
        for(ArrowButtonActor arrowButtonActor : getButtons()){
            arrowButtonActor.setVisible(isToShow);
        }
    }

    public void changeRank(){
        frontside.setRegion(cardAtlas.getRegions().get(currentCardIndex));
    }

    public void changeSuit(){

    }

    public void incrementRegionsIndex(){
        if(currentCardIndex == cardAtlas.getRegions().size-1){
            currentCardIndex = 0;
        }
        currentCardIndex++;
    }

    public void decrementRegionsIndex(){
        if(currentCardIndex == 0){
            currentCardIndex = cardAtlas.getRegions().size-1;
        }
        currentCardIndex--;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(window,getX(),getY());
        batch.setColor(Color.WHITE);    }

    public void setCardActor(CardActor cardActor) {
        this.cardActor = cardActor;
    }

    public CardActor getCardActor() {
        return cardActor;
    }

    public List<ArrowButtonActor> getButtons() {
        return buttons;
    }
}
