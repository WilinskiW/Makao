package com.wwil.makao.frontend.entities.gameButtons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.wwil.makao.backend.gameplay.actions.ActionType;
import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.frontend.controllers.gameplay.GameController;

public class EndTurnButton extends GameButton {
    private final GameController controller;
    public EndTurnButton(GameController controller) {
        super(controller,
                new Texture(Gdx.files.internal("buttons/EndTurnButton_unclick.png")),
                new Texture(Gdx.files.internal("buttons/EndTurnButton_click.png")));
        this.controller = controller;
        setActive(false);
    }

    @Override
    public void sendInput() {
        controller.getSoundManager().playButtonClick();
        controller.executePlay(
                new Play()
                        .setAction(ActionType.END));
    }
}
