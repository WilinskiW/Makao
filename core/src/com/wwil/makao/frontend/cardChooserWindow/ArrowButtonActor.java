package com.wwil.makao.frontend.cardChooserWindow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.wwil.makao.frontend.GUIparams;

public class ArrowButtonActor extends Actor {
    private final CardChooserManager chooserManager;
    private final CardChooserButtonParams type;
    private final TextureRegion texture;

    public ArrowButtonActor(CardChooserManager chooserManager, CardChooserButtonParams type) {
        this.chooserManager = chooserManager;
        setBounds(type.getPosX(), type.getPosY(), type.getWidth(), type.getHeight());
        this.type = type;
        this.texture = new TextureRegion(new Texture(Gdx.files.internal("buttons/arrow.png")));
        addListener(new ArrowListener());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, type.getPosX(), type.getPosY(), getOriginX(), getOriginY(),
                GUIparams.ARROW_BUTTON_WIDTH, GUIparams.ARROW_BUTTON_HEIGHT, getScaleX(), getScaleY(), type.getRotation());
        batch.setColor(Color.WHITE);
    }

    private class ArrowListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            switch (type){
                case UP_LEFT:
                    chooserManager.changeRank(-1);
                    break;
                case UP_RIGHT:
                    chooserManager.changeRank(1);
                    break;
                case DOWN_LEFT:
                    chooserManager.changeSuit(-1);
                    break;
                case DOWN_RIGHT:
                    chooserManager.changeSuit(1);
                    break;
            }
            super.clicked(event, x, y);
        }
    }

    public CardChooserButtonParams getType() {
        return type;
    }
}

