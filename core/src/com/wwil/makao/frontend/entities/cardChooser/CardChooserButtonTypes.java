package com.wwil.makao.frontend.entities.cardChooser;

import com.wwil.makao.frontend.GUIparams;

public enum CardChooserButtonTypes {
    RANK_LEFT(GUIparams.CHOOSER_WINDOW_X_POS+57, GUIparams.CHOOSER_WINDOW_Y_POS+239,
            GUIparams.ARROW_BUTTON_WIDTH, GUIparams.ARROW_BUTTON_HEIGHT, 180),
    RANK_RIGHT(GUIparams.CHOOSER_WINDOW_X_POS+242, GUIparams.CHOOSER_WINDOW_Y_POS+176,
            GUIparams.ARROW_BUTTON_WIDTH, GUIparams.ARROW_BUTTON_HEIGHT, 0),
    SUIT_LEFT(GUIparams.CHOOSER_WINDOW_X_POS+57, GUIparams.CHOOSER_WINDOW_Y_POS+96,
            GUIparams.ARROW_BUTTON_WIDTH, GUIparams.ARROW_BUTTON_HEIGHT, 180),
    SUIT_RIGHT(GUIparams.CHOOSER_WINDOW_X_POS+242, GUIparams.CHOOSER_WINDOW_Y_POS+33,
            GUIparams.ARROW_BUTTON_WIDTH, GUIparams.ARROW_BUTTON_HEIGHT, 0),
    PUT(GUIparams.PUT_X_POS,GUIparams.PUT_Y_POS,GUIparams.PUT_WIDTH,GUIparams.PUT_HEIGHT,0);
    private final float posX;
    private final float posY;
    private final float width;
    private final float height;
    private final float rotation;

    CardChooserButtonTypes(float posX, float posY, float width, float height, float rotation) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getRotation() {
        return rotation;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
