package com.wwil.makao.frontend;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Makao extends Game {
	private SpriteBatch batch;
	private GameplayScreen gameplayScreen;


	@Override
	public void create() {
		batch = new SpriteBatch();
		gameplayScreen = new GameplayScreen(this);
		setScreen(gameplayScreen);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
