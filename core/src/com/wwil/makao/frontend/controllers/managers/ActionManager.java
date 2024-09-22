package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ActionManager {
    private final Queue<ActionSet> sets = new LinkedList<>();

    public void playActions(List<Action> actions) {
        ActionSet actionSet = new ActionSet(actions);
        sets.add(actionSet);
        if (sets.size() == 1) {
            playFirstSet();
        }
    }

    private void playFirstSet() {
        sets.element().play();
    }

    class ActionSet {
        private final List<Action> actions;
        private int finishedActions = 0;

        public ActionSet(List<Action> actions) {
            this.actions = actions;
        }


        public void play() {
            for (Action action : actions) {
                action.getTarget().addAction(addFinisherToAction(action));
            }
        }

        private Action addFinisherToAction(Action action) {
            return Actions.sequence(action, new Action() {
                @Override
                public boolean act(float delta) {
                    finishedActions++;
                    goToNextSetIfFinished();
                    return true;
                }
            });
        }

        public void goToNextSetIfFinished() {
            if (!isFinished()) {
                return;
            }
            sets.remove(this);
            if (!sets.isEmpty()) {
                playFirstSet();
            }
        }

        private boolean isFinished() {
            return finishedActions == actions.size();
        }
    }
}
