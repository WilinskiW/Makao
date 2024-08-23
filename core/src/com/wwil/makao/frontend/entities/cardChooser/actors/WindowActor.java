package com.wwil.makao.frontend.entities.cardChooser.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.wwil.makao.frontend.utils.params.GUIparams;

public class WindowActor extends Actor {
    private final TextureRegion windowTexture;

    public WindowActor() {
        this.windowTexture = new TextureRegion
                (new Texture(Gdx.files.internal("assets/windows/CardChooserWindow.png")));
        setBounds(GUIparams.CHOOSER_WINDOW_X_POS, GUIparams.CHOOSER_WINDOW_Y_POS,
                GUIparams.CHOOSER_WINDOW_WIDTH, GUIparams.CHOOSER_WINDOW_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(windowTexture, getX(), getY());
        batch.setColor(Color.WHITE);
    }
}
