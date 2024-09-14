package com.wwil.makao.frontend.controllers.managers;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


import java.util.*;

public class ActionManager {
    private static ActionManager instance = new ActionManager();
    private Queue<ActionSet> sets = new LinkedList<>();


    //aby zablokować tworzenie obiektu tej klasy wystarczy ustawić konstruktor na prywatny
    ActionManager() {

    }

    static ActionManager getInstance() {
        return instance;
    }

    public void playAction(Action action) {
        playActions(Collections.singletonList(action));
    }

    public void playActions(List<Action> actions){
        ActionSet actionSet = new ActionSet(actions);
        sets.add(actionSet);
        if (sets.size() == 1) {
            playFirstSet();
        }
    }

    private void playFirstSet() {
        sets.element().play();
    }

    class ActionSet{
        private List<Action> actions;
        private int finishedActions = 0;

        public ActionSet(List<Action> actions) {
            this.actions = actions;
        }


        public void play() {
            for (Action action : actions) {
                action.getTarget().addAction(addFinisherToAction(action));
            }
        }

        private Action addFinisherToAction(Action action){
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
