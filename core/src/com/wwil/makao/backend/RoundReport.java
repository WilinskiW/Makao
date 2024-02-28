package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class RoundReport {

    private boolean correct = true;
    private List<PlayReport> plays = new ArrayList<>();

    public void addPlay(PlayReport playReport) {
        plays.add(playReport);
    }

    public void setIncorrect() {
        correct = false;
    }
    public boolean isCorrect() {
        return correct;
    }
}
