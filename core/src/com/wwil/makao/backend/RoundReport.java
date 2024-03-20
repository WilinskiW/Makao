package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;
//Zebranie wszystkich ruchów graczy w jeden obiekt
public class RoundReport {
    private boolean correct = true;
    private final List<PlayReport> playReports = new ArrayList<>();
    private boolean shouldActiveChooser = false;

    public void addPlay(PlayReport playReport) {
        playReports.add(playReport);
    }

    public PlayReport getLastPlay(){
        return playReports.get(getPlayReports().size()-1);
    }

    public List<PlayReport> getPlayReports() {
        return playReports;
    }

    public boolean isChooserActive() {
        return shouldActiveChooser;
    }

    public void setIncorrect() {
        correct = false;
    }

    public void setChooserActivation(boolean active) {
        this.shouldActiveChooser = active;
    }

    public boolean isCorrect() {
        return correct;
    }
}
