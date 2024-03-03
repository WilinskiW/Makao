package com.wwil.makao.frontend;

public enum CardsAlignmentParams {
    SOUTH (GUIparams.DISTANCE_BETWEEN_CARDS/2f,0),
    EAST(0,GUIparams.DISTANCE_BETWEEN_CARDS/2f),
    NORTH (-GUIparams.DISTANCE_BETWEEN_CARDS/2f,0),
    WEST(0,-GUIparams.DISTANCE_BETWEEN_CARDS/2f);

    public final float xMove;
    public final float yMove;

    CardsAlignmentParams(float xMove, float yMove) {
        this.xMove = xMove;
        this.yMove = yMove;
    }
}
