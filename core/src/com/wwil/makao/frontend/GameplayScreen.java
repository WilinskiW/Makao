package com.wwil.makao.frontend;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameplayScreen implements Screen {
    private final Makao makao;
    private OrthographicCamera camera;
    private Stage stage;
    private FitViewport viewport;
    private final GameController gameController;

    // TODO: INPUT Raport
    // TODO: Animacje rzucania kart przez boty
    // TODO: Aktywacja specjalnych zdolności kart ~ Specjalne zdolności
    // TODO: Obrona
    // TODO: Komunikaty kcji graczy
    // TODO: Główne menu
    // TODO: Dźwięk i muzyk
    // TODO: Możliwość zmiany skinów do kart
    //tworzy główny ekran gry
    public GameplayScreen(Makao makao) {
        this.makao = makao;
        this.gameController = new GameController(this);
        GameplayScreenPreparer screenPreparer = new GameplayScreenPreparer(this, gameController);
        screenPreparer.prepareGraphicComponents();
        screenPreparer.prepareGameComponents();
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
        gameController.handlePullButtonInput();
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

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public void setViewport(FitViewport viewport) {
        this.viewport = viewport;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}