package com.wwil.makao.frontend.cardChooserWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.wwil.makao.frontend.GUIparams;

public class ArrowButtonActor extends Actor {
    private final CardChooserWindow cardChooserWindow;
    private final CardChooserButtonParams type;
    private final TextureRegion texture;

    public ArrowButtonActor(CardChooserWindow cardChooserWindow, CardChooserButtonParams type) {
        this.cardChooserWindow = cardChooserWindow;
        setBounds(type.getPosX(), type.getPosY(),
                GUIparams.ARROW_BUTTON_WIDTH, GUIparams.ARROW_BUTTON_HEIGHT);
        this.type = type;
        if(type != CardChooserButtonParams.PUT) {
            this.texture = new TextureRegion(new Texture(Gdx.files.internal("buttons/arrow.png")));
        }else {
            this.texture = new TextureRegion(new Texture(Gdx.files.internal("buttons/arrow.png"))); // todo
        }
        addListener(new CardChooserWindowListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture,type.getPosX(),type.getPosY(),getOriginX(),getOriginY(),
                GUIparams.ARROW_BUTTON_WIDTH,GUIparams.ARROW_BUTTON_HEIGHT,getScaleX(),getScaleY(),type.getRotation());
        batch.setColor(Color.WHITE);
    }

    public CardChooserButtonParams getType() {
        return type;
    }

    private class CardChooserWindowListener extends ClickListener{
        @Override
        public void clicked(InputEvent event, float x, float y) {
            System.out.println(type.name()); //todo
            if(type.equals(CardChooserButtonParams.UP_LEFT)){
                cardChooserWindow.decrementRegionsIndex();
            }
            else{
                cardChooserWindow.incrementRegionsIndex();
            }
            cardChooserWindow.changeRank();
            super.clicked(event, x, y);
        }
    }
}

