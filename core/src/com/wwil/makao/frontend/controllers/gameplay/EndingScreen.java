package com.wwil.makao.frontend.controllers.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.wwil.makao.frontend.utils.params.GUIparams;
import com.wwil.makao.frontend.utils.text.TextContainer;

public class EndingScreen implements Screen {
    private final Makao makao;
    private OrthographicCamera camera;
    private Stage stage;
    private FitViewport viewport;

    public EndingScreen(Makao makao, String whoWon) {
        this.makao = makao;
        createStage();
        createText(whoWon);
    }

    private void createStage() {
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);

        this.viewport = new FitViewport(0, 0, camera);
        this.stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);
    }

    private void createText(String whoWon) {
        createInfoLabel(whoWon);
        createRestartLabel();
        createExitLabel();
    }

    private void createInfoLabel(String whoWon) {
        TextContainer whoWonInfo = new TextContainer(false);
        whoWonInfo.getLabel().setColor(Color.WHITE);
        whoWonInfo.setText(whoWon + " won");
        stage.addActor(whoWonInfo.getLabel());
    }

    private void createRestartLabel() {
        TextContainer restart = new TextContainer(false);
        restart.getLabel().setColor(Color.WHITE);
        restart.setText("Restart (Press R to restart)");
        restart.getLabel().setPosition(restart.getLabel().getX(), restart.getLabel().getY() - 100);
        stage.addActor(restart.getLabel());
    }

    private void createExitLabel() {
        TextContainer exit = new TextContainer(false);
        exit.getLabel().setColor(Color.WHITE);
        exit.setText("Exit (Press E to exit)");
        exit.getLabel().setPosition(exit.getLabel().getX(), exit.getLabel().getY() - 150);
        stage.addActor(exit.getLabel());
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        makao.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            makao.setScreen(new GameplayScreen(makao));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            Gdx.app.exit();
        }
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
}
