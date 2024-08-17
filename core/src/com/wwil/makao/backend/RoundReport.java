package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

//Zebranie wszystkich ruchów graczy w jeden obiekt
public class RoundReport {
    private final List<PlayReport> playReports = new ArrayList<>();

    public void addPlayRaport(PlayReport playReport) {
        playReports.add(playReport);
    }

    public List<PlayReport> getComputerPlayReports(Player humanPlayer) {
        List<PlayReport> computerPlayReports = new ArrayList<>();
        for (PlayReport playReport : playReports) {
            if (playReport.getPlayer() != humanPlayer) {
                computerPlayReports.add(playReport);
            }
        }
        return computerPlayReports;
    }
    
    public List<PlayReport> getHumanPlayReports(Player humanPlayer){
        List<PlayReport> computerPlayReports = new ArrayList<>();
        for (PlayReport playReport : playReports) {
            if (playReport.getPlayer() == humanPlayer) {
                computerPlayReports.add(playReport);
            }
        }
        return computerPlayReports;
    }

    public boolean hasPlayerPullBefore(Player player) {
        for (PlayReport playReport : playReports) {
            if (playReport.getPlayer() == player && playReport.getPlay().getAction() == Action.PULL){
                return true;
            }
        }
        return false;
    }

    public PlayReport getLastPlayReport() {
        return playReports.get(getPlayReports().size() - 1);
    }

    public List<PlayReport> getPlayReports() {
        return playReports;
    }

}
