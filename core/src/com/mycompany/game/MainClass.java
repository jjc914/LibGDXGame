package com.mycompany.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mycompany.game.screens.GameScreen;

public class MainClass extends Game {
	SpriteBatch batch;
	Screen currentScreen;

	public SpriteBatch getBatch() {
		return batch;
	}

	public void create () {
		batch = new SpriteBatch();

		currentScreen = new GameScreen(this);
		setScreen(currentScreen);
	}

	public void render () {

	}

	public void dispose () {

	}
}
