package com.wwil.makao.frontend.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.frontend.parameters.GUIparams;

public class PullButtonActor extends Actor {
    private final Texture unclickTexture;
    private final Texture clickTexture;
    private boolean isClick = false;

    public PullButtonActor() { //todo Możliwy refactor do animacji
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

    public boolean checkIfButtonIsClick(int graphicsY) {
        return checkBoundries(graphicsY) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);

    }

    private boolean checkBoundries(int graphicsY) {
        return Gdx.input.getX() > this.getX()
                && Gdx.input.getX() < this.getWidth() + this.getX()
                && graphicsY > this.getY()
                && graphicsY < this.getHeight() + this.getY();
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
