package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;
//Zebranie wszystkich ruchów graczy w jeden obiekt
public class RoundReport {
    private boolean attemptCorrect = true;
    private final List<PlayReport> playReports = new ArrayList<>();
    private boolean blockPullButton = false;

    public void addPlayRaport(PlayReport playReport) {
        playReports.add(playReport);
    }

    public List<PlayReport> getComputerPlayReport(){
        Player humanPlayer = playReports.get(0).getPlayer();
        List<PlayReport> computerPlayReports = new ArrayList<>();
        for(PlayReport playReport : playReports){
            if(playReport.getPlayer() != humanPlayer){
                computerPlayReports.add(playReport);
            }
        }
        return computerPlayReports;
    }

    public PlayReport getHumanReport(){
        return playReports.get(0);
    }

    public PlayReport getLastPlay(){
        return playReports.get(getPlayReports().size()-1);
    }

    public List<PlayReport> getPlayReports() {
        return playReports;
    }

    public void setIncorrect() {
        attemptCorrect = false;
    }

    public void setBlockPullButton(boolean blockPullButton) {
        this.blockPullButton = blockPullButton;
    }

    public boolean isPullButtonBlock() {
        return blockPullButton;
    }

    public boolean isAttemptCorrect() {
        return attemptCorrect;
    }
}
