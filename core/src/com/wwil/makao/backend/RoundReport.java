package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;
//Zebranie wszystkich ruchów graczy w jeden obiekt
public class RoundReport {
    private boolean correct = true;
    private final List<PlayReport> playReports = new ArrayList<>();

    public void addPlay(PlayReport playReport) {
        playReports.add(playReport);
    }

    public List<PlayReport> getPlayReports() {
        return playReports;
    }

    public void setIncorrect() {
        correct = false;
    }
    public boolean isCorrect() {
        return correct;
    }
}
