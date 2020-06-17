package com.mycompany.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.mycompany.game.MainClass;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class GameScreen implements Screen {
    MainClass mainClass;
    Texture testTexture;


    private TmxMapLoader mapLoader; //helps load the map
    private TiledMap map; //the loaded map object
    private OrthogonalTiledMapRenderer renderer; //renders the map
    private Socket socket;

    public GameScreen(MainClass game)
    {
        //existing constructor code
        mapLoader = new TmxMapLoader(); //create an instance of built-in map loader object
        map = mapLoader.load("tilemaps/lvl1.tmx"); //using map loader object, load the tiled map that you made
        renderer = new OrthogonalTiledMapRenderer(map); //render the map.

        connectSocket();
        configSocketEvents();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        renderer.render();
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
        map.dispose();
        renderer.dispose();
    }

    public void connectSocket() {
        System.out.println("[SocketIO] Connecting...");
        try {
            socket = IO.socket("http://localhost:8080");
            socket.connect();;
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void configSocketEvents() {
        socket.on(socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("[SocketIO] Connected!");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[SocketIO] Connected user id: " + id);
                }
                catch (JSONException e){
                    System.out.println(e);
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[SocketIO] New player connected with user id: " + id);
                }
                catch (JSONException e){
                    System.out.println(e);
                }
            }
        });
    }
}
