package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;
//Zebranie wszystkich ruchów w gracza w jedną klase.
public class RoundReport {
    private boolean correct = true; //czy położono dobrą karte
    private final List<PlayReport> plays = new ArrayList<>();

    public void addPlay(PlayReport playReport) {
        plays.add(playReport);
    }

    public List<PlayReport> getPlays() {
        return plays;
    }

    public void setIncorrect() {
        correct = false;
    }
    public boolean isCorrect() {
        return correct;
    }
}
