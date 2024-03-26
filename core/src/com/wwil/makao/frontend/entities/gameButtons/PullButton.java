package com.wwil.makao.frontend.entities.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.wwil.makao.frontend.GameController;

public class PullButton extends GameButton {
    private final GameController controller;
    public PullButton(GameController controller) {
        super(controller, new Texture(Gdx.files.internal("assets/Buttons/PullCardButton_unclick.png")),
                new Texture(Gdx.files.internal("assets/Buttons/PullCardButton_click.png")));
        this.controller = controller;
    }

    @Override
    public void sendInput() {
        controller.executeTurn(null,false,false,false);
    }
}
