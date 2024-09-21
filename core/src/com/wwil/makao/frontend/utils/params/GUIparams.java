package com.wwil.makao.frontend.utils.params;

import com.badlogic.gdx.math.Vector2;

public class GUIparams {
    public static int WIDTH = 1680;
    public static int HEIGHT = 768;
    public static final int CARD_WIDTH = 168;
    public static final int CARD_HEIGHT = 248;
    public static final int GAME_BUTTON_WIDTH = 128;
    public static final int GAME_BUTTON_HEIGHT = 64;
    public static final Vector2 CHOOSER_WINDOW_POS = new Vector2(WIDTH / 2f - 50, HEIGHT / 2f);
    public static final float CHOOSER_WINDOW_WIDTH = 300;
    public static final float CHOOSER_WINDOW_HEIGHT = 263;
    public static final Vector2 CHOOSER_CARD_POS = new Vector2(CHOOSER_WINDOW_POS.x + 65, CHOOSER_WINDOW_POS.y + 8);
    public static final float ARROW_BUTTON_WIDTH = 48;
    public static final float ARROW_BUTTON_HEIGHT = 64;
    public static final Vector2 PUT_POS = new Vector2(WIDTH / 2f - 50, HEIGHT / 2f - 40);
    public static final float PUT_WIDTH = 299;
    public static final float PUT_HEIGHT = 40;
    public static final float HANDGROUP_CARDS_GAP = CARD_WIDTH / 2.5f;
    public static final float GAP_BETWEEN_DECK_CARDS = 7;
    public static final boolean HIDE_COMPUTER_CARD = false;
}
