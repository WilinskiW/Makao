package com.wwil.makao.frontend.gameComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.frontend.GameController;
import com.wwil.makao.frontend.parameters.GUIparams;

public class PullButtonActor extends Actor{
    private final Texture unclickTexture;
    private final Texture clickTexture;
    private boolean isClick = false;

    public PullButtonActor() {//todo Możliwy refactor do animacji
        this.unclickTexture = new Texture(Gdx.files.internal("assets/Buttons/PullCardButton_unclick.png"));
        this.clickTexture = new Texture(Gdx.files.internal("assets/Buttons/PullCardButton_click.png"));
        setBounds(0, 0, GUIparams.PULL_BUTTON_WIDTH, GUIparams.PULL_BUTTON_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        Texture texture = unclickTexture;
        if (isClick) {
            texture = clickTexture;
        }
        batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                getWidth(), getHeight(), getScaleX(), getScaleY(),
                getRotation(), 0, 0, texture.getWidth(), texture.getHeight(),
                false, false);
        batch.setColor(Color.WHITE);
    }

    public void changeTransparency(float transparency){
        setColor(getColor().r,getColor().g,getColor().b,transparency);
    }
    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
