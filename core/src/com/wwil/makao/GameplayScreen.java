package com.wwil.makao;

import com.badlogic.gdx.Screen;

public class GameplayScreen implements Screen {
    @Override
    public void show() {
        System.out.println("START");
    }

    @Override
    public void render(float delta) {
        System.out.println("RENDER");
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        System.out.println("DISPOSE");
    }
}
