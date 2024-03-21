package com.wwil.makao.frontend.entities.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.wwil.makao.frontend.GameController;

public class EndTurnButton extends GameButton {
    private final GameController controller;
    public EndTurnButton(GameController controller) {
        super(controller, new Texture(Gdx.files.internal("buttons/EndTurnButton_unclick.png")),
                new Texture(Gdx.files.internal("buttons/EndTurnButton_click.png")));
        this.controller = controller;
    }

    @Override
    public void sendInput() {
        controller.executeTurn(null,false,true,false,false);
    }
}
