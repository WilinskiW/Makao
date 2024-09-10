package com.wwil.makao.frontend.entities.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.wwil.makao.backend.gameplay.actions.Action;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.frontend.controllers.gameplay.GameController;

public class MakaoButton extends GameButton {
    private final GameController controller;

    public MakaoButton(GameController controller) {
        super(controller,
                new Texture(Gdx.files.internal("buttons/MakaoButton_unclick.png")),
                new Texture(Gdx.files.internal("buttons/MakaoButton_click.png")));
        this.controller = controller;
        setActive(true);
    }

    @Override
    public void sendInput() {
        controller.getSoundManager().play("buttonClick.mp3", 0.05f);
        controller.executePlay(
                new Play().setAction(Action.MAKAO));
    }

    @Override
    public void setActive(boolean shouldActive) {
        super.setActive(shouldActive);
    }
}

