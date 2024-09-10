package com.wwil.makao.frontend.utils.text;

import com.wwil.makao.backend.gameplay.actions.Action;
import com.wwil.makao.backend.gameplay.actions.PlayReport;

public class ReportToTextConverter {
    public static String convert(PlayReport playReport) {
        Action action = playReport.getPlay().getAction();
        switch (action) {
            case PUT:
                return printPutAction(playReport);
            case PULL:
                return printPullAction(playReport);
            case END:
                return printEndAction(playReport);
            case MAKAO:
                return printMakaoAction(playReport);
            default:
                return "";
        }
    }

    private static String printPutAction(PlayReport playReport) {
        String player = playReport.getPlayer().toString();
        if (!playReport.isCardCorrect()) {
            return playReport.getAfterState().isChooserActive() ? "INVALID CARD" : "";
        }

        String cardDetails = getCardDetails(playReport);
        return player + " put " + cardDetails;
    }

    private static String getCardDetails(PlayReport playReport) {
        String beforeState = playReport.getBeforeState().toString();
        String cardPlayed = playReport.getPlay().getCardPlayed().toString();

        if (playReport.getPlay().getCardPlayed().isShadow()) {
            if (beforeState.contains("card")) {
                return beforeState + cardPlayed;
            } else if (beforeState.contains("demand")) {
                return beforeState + playReport.getPlay().getCardPlayed().getRank();
            } else {
                return beforeState + playReport.getPlay().getCardPlayed().getSuit();
            }
        }
        return cardPlayed;
    }

    private static String printPullAction(PlayReport playReport) {
        String player = playReport.getPlayer().toString();
        String afterState = playReport.getAfterState().toString();
        if(afterState.contains("rescue")){
            return player + afterState;
        }

        return afterState.contains("pulls") ? player + afterState : player + " has pulled last card";
    }

    private static String printMakaoAction(PlayReport playReport) {
        return playReport.getPlayer().toString() + " is reporting MAKAO";
    }

    private static String printEndAction(PlayReport playReport) {
        String player = playReport.getPlayer().toString();
        String afterState = playReport.getAfterState().toString();
        return afterState.contains("blocked") ? player + " " + afterState : "";
    }
}
