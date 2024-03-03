package com.wwil.makao.frontend.gameComponents;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Timer;
import com.wwil.makao.frontend.GameController;

public class PullButtonHandler extends InputListener {
    private final PullButtonActor pullButtonActor;
    private final GameController controller;

    public PullButtonHandler(PullButtonActor pullButtonActor, GameController controller) {
        this.pullButtonActor = pullButtonActor;
        this.controller = controller;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(!controller.isInputBlockActive()){
            performPullButtonClick();
        }
        return super.touchDown(event, x, y, pointer, button);
    }

    private void performPullButtonClick() {
        pullButtonActor.setClick(true);
        pullButtonActor.changeTransparency(0.5f);
        //controller.turnOffHumanInput();
        controller.executeHumanAction(null,false);
        performButtonAnimation();
    }


    private void performButtonAnimation() {
        Timer.Task undoClick = new com.badlogic.gdx.utils.Timer.Task() {
            @Override
            public void run() {
                pullButtonActor.setClick(false);
            }
        };
        Timer.schedule(undoClick, 0.5f);
    }
}
