package com.wwil.makao.frontend.utils.params;

public enum CardsAlignmentParams {
    SOUTH(GUIparams.HANDGROUP_CARDS_GAP / 2f, 0, 0),
    EAST(0, GUIparams.HANDGROUP_CARDS_GAP / 2f, 90),
    NORTH(-GUIparams.HANDGROUP_CARDS_GAP / 2f, 0, 180),
    WEST(0, -GUIparams.HANDGROUP_CARDS_GAP / 2f, -90);

    public final float xMove;
    public final float yMove;
    public final float rotation;

    CardsAlignmentParams(float xMove, float yMove, float rotation) {
        this.xMove = xMove;
        this.yMove = yMove;
        this.rotation = rotation;
    }

    public float getRotation() {
        return rotation;
    }

    public static CardsAlignmentParams getParamFromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
