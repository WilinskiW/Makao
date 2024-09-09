package com.wwil.makao.frontend.utils.text;

import com.wwil.makao.backend.gameplay.actions.Action;
import com.wwil.makao.backend.gameplay.actions.PlayReport;

public class ReportToTextConverter {
    public static String convert(PlayReport playReport) {
        String player = playReport.getPlayer().toString();
        String state = playReport.getState().toString();
        Action action = playReport.getPlay().getAction();

        switch (action) {
            case PUT:
                if (playReport.getPlay().getCardPlayed().isShadow()) {
                    return player + " choosed " + playReport.getPlay().getCardPlayed();
                }
                return player + " putted " + playReport.getPlay().getCardPlayed();
            case PULL:
                return player + " pulled";
            case END:
                return player + " ended turn";
            case MAKAO:
                return player + " is reporting MAKAO";
            default:
                return "chuj wie";
        }

    }
}
