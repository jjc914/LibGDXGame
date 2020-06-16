package com.mycompany.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mycompany.game.MainClass;

public class GameScreen implements Screen {
    MainClass mainClass;
    Texture testTexture;

    public GameScreen(MainClass main) {
        this.mainClass = main;
        testTexture = new Texture("test.jpg");

        System.out.println("started screen");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        mainClass.getBatch().begin();

        mainClass.getBatch().draw(testTexture, 0, 0);

        mainClass.getBatch().end();
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

    }
}
