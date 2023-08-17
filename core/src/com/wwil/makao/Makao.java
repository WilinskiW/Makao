package com.wwil.makao;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Makao extends Game {
	private SpriteBatch batch;
	private GameplayScreen gameplayScreen;


	@Override
	public void create() {
		batch = new SpriteBatch();
		gameplayScreen = new GameplayScreen();
		setScreen(gameplayScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
