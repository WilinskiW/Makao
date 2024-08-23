package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.player.Player;

import java.util.ArrayList;
import java.util.List;

//Zebranie wszystkich ruchów graczy w jeden obiekt
public class RoundReport {
    private final List<PlayReport> playReports = new ArrayList<>();

    void addPlayRaport(PlayReport playReport) {
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
        int counter = 0;
        for (PlayReport playReport : playReports) {
            if (playReport.getPlayer() == player && playReport.getPlay().getAction() == Action.PULL){
                counter++;
            }

            if(counter >= 2){
                return true;
            }
        }

        return false;
    }

    public PlayReport getHumanLastPlayReport() {
        for(int i = playReports.size()-1; i >= 0; i--){
            if(playReports.get(i).getPlayer().isHuman()){
                return playReports.get(i);
            }
        }
        throw new IllegalArgumentException("Human nie żadnego playreporta");
    }
}
