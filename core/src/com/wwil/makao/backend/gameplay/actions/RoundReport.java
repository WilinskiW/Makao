package com.wwil.makao.backend.gameplay.actions;

import com.wwil.makao.backend.model.player.Player;

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

    public PlayReport getHumanLastPlayReport() {
        for(int i = playReports.size()-1; i >= 0; i--){
            if(playReports.get(i).getPlayer().isHuman()){
                return playReports.get(i);
            }
        }
        throw new IllegalArgumentException("Human nie żadnego playreporta");
    }
}
