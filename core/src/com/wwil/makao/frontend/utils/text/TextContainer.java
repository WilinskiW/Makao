package com.wwil.makao.frontend.utils.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.wwil.makao.frontend.utils.params.GUIparams;

public class TextContainer {
    private final Label label;
    public TextContainer() {
        this.label = new Label("", adjustStyle());
        this.label.setSize(30, 30);
        this.label.setPosition(GUIparams.WIDTH / 2f + 75, GUIparams.HEIGHT / 2f + 275);
        this.label.setAlignment(Align.center);
    }

    private Label.LabelStyle adjustStyle() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("fonts/messageFont.fnt"));
        labelStyle.fontColor = Color.valueOf("151515");
        return labelStyle;
    }

    public Label getLabel() {
        return label;
    }

    public void setText(String text) {
        label.setText(text);
    }
}
