package com.wwil.makao.frontend.entities.gameButtons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.frontend.GUIparams;
import com.wwil.makao.frontend.GameController;

public abstract class GameButton extends Actor {
    private final GameController controller;
    private final Texture unclickTexture;
    private final Texture clickTexture;
    private boolean isClick = false;
    private boolean isActive = true;

    public GameButton(GameController controller, Texture unclickTexture, Texture clickTexture) {
        this.controller = controller;
        this.unclickTexture = unclickTexture;
        this.clickTexture = clickTexture;
        this.addListener(new ButtonListener());
        setBounds(0, 0, GUIparams.GAME_BUTTON_WIDTH, GUIparams.GAME_BUTTON_HEIGHT);
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

    private class ButtonListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if(!controller.isInputBlockActive() && isActive) {
                performButtonClick();
            }
            super.clicked(event, x, y);
        }

        public void performButtonClick() {
            setClick(true);
            sendInput();
            performButtonAnimation();
        }

        private void performButtonAnimation() {
            Timer.Task undoClick = new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    setClick(false);
                }
            };
            Timer.schedule(undoClick, 0.5f);
        }
    }

    public abstract void sendInput();

    public void setActive(boolean shouldActive){
        if(shouldActive){
            changeTransparency(1f);
            isActive = true;
        }
        else{
            changeTransparency(0.5f);
            isActive = false;
        }
    }

    private void changeTransparency(float transparency){
        setColor(getColor().r,getColor().g,getColor().b,transparency);
    }
    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
