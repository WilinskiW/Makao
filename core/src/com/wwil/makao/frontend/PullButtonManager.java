package com.wwil.makao.frontend;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

public class PullButtonManager extends ClickListener {
    private final PullButtonActor pullButtonActor;
    private final GameController controller;

    public PullButtonManager(PullButtonActor pullButtonActor, GameController controller) {
        this.pullButtonActor = pullButtonActor;
        this.controller = controller;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if(!controller.isInputBlockActive()) {
            performPullButtonClick();
        }
        super.clicked(event, x, y);
    }

    private void performPullButtonClick() {
        pullButtonActor.setClick(true);
        controller.executeHumanAction(null,false,false);
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
