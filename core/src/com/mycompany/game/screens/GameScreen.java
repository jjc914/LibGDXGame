package com.mycompany.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mycompany.game.Constants;
import com.mycompany.game.MainClass;
import com.mycompany.game.sprites.Player;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class GameScreen implements Screen {
    MainClass mainClass;

    private World world;
    //world
    private Player player;

    private Box2DDebugRenderer box2DDebugRenderer = new Box2DDebugRenderer();

    private Viewport viewport;
    private OrthographicCamera camera;

    private TmxMapLoader mapLoader; //helps load the map
    private TiledMap map; //the loaded map object
    private OrthogonalTiledMapRenderer renderer; //renders the map
    private Socket socket;

    public GameScreen(MainClass game)
    {
        mainClass = game;
        //map loader
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("tilemaps/lvl1.tmx");
        //camera loader
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,-200), true);
        player = new Player(world);

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;
        for(MapObject mapObject : map.getLayers().get(Constants.BASE_COL).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() +rectangle.getHeight() / 2);
            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);

        }

        renderer = new OrthogonalTiledMapRenderer(map);
        connectSocket();
        configSocketEvents();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        handleInput(delta);

        Gdx.gl.glClearColor(0, 0 , 0 ,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainClass.getBatch().setProjectionMatrix(camera.combined);
        renderer.render();

        box2DDebugRenderer.render(world, camera.combined);
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

    public void update(float delta)
    {
        world.step(1/120f,6,2);
//        camera.position.x = player.getBox2Body().getPosition().x;
        camera.update();
        renderer.setView(camera);
    }

    private void handleInput(float delta) {
        float moveForce = 200;

        // left-right movement
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {

            Vector2 force = new Vector2(moveForce, player.getBox2Body().getLinearVelocity().y); //1. create force
//            player.getBox2Body().applyLinearImpulse(force, player.getBox2Body().getWorldCenter(), true); //apply force
            player.getBox2Body().setLinearVelocity(force);
//            player.getBox2Body().setTransform(new Vector2(player.getBox2Body().getPosition().x + 1f, player.getBox2Body().getPosition().y), 0f);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {

            Vector2 force = new Vector2(-moveForce, player.getBox2Body().getLinearVelocity().y); //1. create force
//            player.getBox2Body().applyLinearImpulse(force, player.getBox2Body().getWorldCenter(), true); //apply force
            player.getBox2Body().setLinearVelocity(force);
//            player.getBox2Body().setTransform(new Vector2(player.getBox2Body().getPosition().x + 1f, player.getBox2Body().getPosition().y), 0f);
        }
        else
        {
            player.getBox2Body().setLinearVelocity(0f, player.getBox2Body().getLinearVelocity().y);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
        {
            System.out.println(player.box2Body.getLinearVelocity().y);
            Vector2 force = new Vector2(0f, 100f);
//            player.getBox2Body().applyForce(force, player.getBox2Body().getWorldCenter(), true); //apply force
            player.getBox2Body().setLinearVelocity(force); //apply force
        }
    }

    public void connectSocket() {
        System.out.println("[SocketIO] Connecting...");
        try {
            socket = IO.socket("http://10.0.17.255:8000");
            socket.connect();
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void configSocketEvents() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("[SocketIO] " + "Connected");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[SocketIO] " + "Connected with ID " + id);
                } catch (JSONException e) {
                    System.out.println("[SocketIO] " + "Error connecting");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[SocketIO] " + "New player connected with ID" + id);
                }
                catch(JSONException e) {
                    System.out.println("[SocketIO] " + "Error getting new player ID");
                }
            }
        }).on("updatePlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String updateData = data.getString("updateData");
                    System.out.println("[SocketIO] " + "Caught update data " + updateData);
                }
                catch(JSONException e) {
                    {
                        System.out.println("[SocketIO] " + "Error getting new player ID");
                    }
                }
            }
        }) ;
    }
}