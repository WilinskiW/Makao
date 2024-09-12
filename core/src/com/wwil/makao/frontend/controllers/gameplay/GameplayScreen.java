package com.wwil.makao.frontend.controllers.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wwil.makao.frontend.utils.params.GUIparams;

public class GameplayScreen implements Screen {
    private final Makao makao;
    private OrthographicCamera camera;
    private Stage stage;
    private FitViewport viewport;

    //tworzy główny ekran gry
    public GameplayScreen(Makao makao) {
        this.makao = makao;
        createStage();
        GameController gameController = new GameController(makao, this);
        gameController.getUiManager().prepareStage();
    }

    private void createStage() {
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);

        this.viewport = new FitViewport(0, 0, camera);
        this.stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.FOREST);
        makao.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.setWorldSize(width, height);
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

    public Stage getStage() {
        return stage;
    }
}