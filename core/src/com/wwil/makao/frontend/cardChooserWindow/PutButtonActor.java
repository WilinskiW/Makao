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
import com.wwil.makao.frontend.GameController;

public class PutButtonActor extends Actor {
    private final CardChooserGroup cardChooser;
    private final GameController controller;
    private final CardChooserButtonParams type;
    private final TextureRegion texture;


    public PutButtonActor(CardChooserGroup cardChooser) {
        this.cardChooser = cardChooser;
        this.controller = cardChooser.getGameController();
        this.type = CardChooserButtonParams.PUT;
        this.texture = new TextureRegion(new Texture(Gdx.files.internal("buttons/Put.png")));
        setBounds(type.getPosX(), type.getPosY(), type.getWidth(), type.getHeight());
        addListener(new PutListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, type.getPosX(), type.getPosY(), getOriginX(), getOriginY(),
                GUIparams.PUT_WIDTH, GUIparams.PUT_HEIGHT, getScaleX(), getScaleY(), type.getRotation());
        batch.setColor(Color.WHITE);
    }

    private class PutListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            controller.executeHumanAction(cardChooser.getManager().giveCardActor(),true,true);
            super.clicked(event, x, y);
        }
    }
}
