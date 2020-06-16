package com.mycompany.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mycompany.game.screens.GameScreen;

public class MainClass extends Game {
	Screen currentScreen;
	SpriteBatch batch;

	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();

		currentScreen = new GameScreen(this);
		setScreen(currentScreen);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {

	}
}
