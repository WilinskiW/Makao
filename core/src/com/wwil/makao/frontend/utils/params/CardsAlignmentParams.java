package com.wwil.makao.frontend.utils.params;

public enum CardsAlignmentParams {
    SOUTH(GUIparams.HANDGROUP_CARDS_GAP / 2f, 0),
    EAST(0, GUIparams.HANDGROUP_CARDS_GAP / 2f),
    NORTH(-GUIparams.HANDGROUP_CARDS_GAP / 2f, 0),
    WEST(0, -GUIparams.HANDGROUP_CARDS_GAP / 2f);

    public final float xMove;
    public final float yMove;

    CardsAlignmentParams(float xMove, float yMove) {
        this.xMove = xMove;
        this.yMove = yMove;
    }

    public static CardsAlignmentParams getParamFromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
