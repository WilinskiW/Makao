package com.wwil.makao;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameplayScreen implements Screen {
    private Makao makao;
    private final OrthographicCamera camera;
    private Stage stage;

    public GameplayScreen(Makao makao) {
        this.makao = makao;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GUIparams.WIDTH, GUIparams.HEIGHT);
        stage = new Stage(new ScreenViewport(camera), makao.getBatch());
        Gdx.input.setInputProcessor(stage);
        CardActor cardActor = new CardActor(new Texture(Gdx.files.internal("Cards/Clubs/CQ.png")));
        CardActor cardActor2 = new CardActor(new Texture(Gdx.files.internal("Cards/Diamonds/D5.png")));
        CardActor cardActor3 = new CardActor(new Texture(Gdx.files.internal("Cards/Diamonds/D9.png")));
        CardActor cardActor4 = new CardActor(new Texture(Gdx.files.internal("Cards/Red Hearts/H3.png")));
        CardActor cardActor5 = new CardActor(new Texture(Gdx.files.internal("Cards/Black Hearts/HJ.png")));

        PlayerHandGroup playerHandGroup = new PlayerHandGroup();
        stage.addActor(playerHandGroup);
        playerHandGroup.setPosition(300,50);

        playerHandGroup.addActor(cardActor);
        playerHandGroup.addActor(cardActor2);
        playerHandGroup.addActor(cardActor3);
        playerHandGroup.addActor(cardActor4);
        playerHandGroup.addActor(cardActor5);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.WHITE);
       // makao.getBatch().setProjectionMatrix(camera.combined);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
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
