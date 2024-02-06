package com.wwil.makao.frontend;

public enum CardsAligmentParams {
    SOUTH(GUIparams.DISTANCE_BETWEEN_CARDS/2f,0),
    NORTH(-GUIparams.DISTANCE_BETWEEN_CARDS/2f,0),
    EAST(0,GUIparams.DISTANCE_BETWEEN_CARDS/2f),
    WEST(0,-GUIparams.DISTANCE_BETWEEN_CARDS/2f);
    public final float xMove;
    public final float yMove;

    CardsAligmentParams(float xMove, float yMove) {
        this.xMove = xMove;
        this.yMove = yMove;
    }
}
