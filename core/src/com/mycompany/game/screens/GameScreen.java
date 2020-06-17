package com.mycompany.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mycompany.game.Constants;
import com.mycompany.game.MainClass;

import io.socket.client.IO;
import io.socket.client.Socket;


public class GameScreen implements Screen {
    MainClass mainClass;
    Texture testTexture;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TmxMapLoader mapLoader; //helps load the map
    private TiledMap map; //the loaded map object
    private OrthogonalTiledMapRenderer renderer; //renders the map
    private Socket socket;

    public GameScreen(MainClass game)
    {
        mainClass = game;

        mapLoader = new TmxMapLoader(); //create an instance of built-in map loader object
        map = mapLoader.load("tilemaps/lvl1.tmx"); //using map loader object, load the tiled map that you made

        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        renderer = new OrthogonalTiledMapRenderer(map); //render the map.

        connectSocket();
    }

    public void update(float delta)
    {
        camera.update();
        renderer.setView(camera); //sets the view from our camera so it would render only what our camera can see.
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0 , 0 ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainClass.getBatch().setProjectionMatrix(camera.combined);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        map.dispose();
        renderer.dispose();
    }

    public void connectSocket() {
        System.out.println("[SocketIO] Connecting...");
        try {
            socket = IO.socket("http://localhost:8080");
            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}