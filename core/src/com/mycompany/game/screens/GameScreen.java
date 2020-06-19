package com.mycompany.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mycompany.game.Constants;
import com.mycompany.game.MainClass;
import com.mycompany.game.sprites.Opponent;
import com.mycompany.game.sprites.Player;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class GameScreen implements Screen {
    private MainClass mainClass;
    private GameScreen gameScreen;

    public Animation runningAnimation;

    private TextureAtlas atlas;

    private World world;
    private Player player;

    private Viewport viewport;
    private OrthographicCamera camera;

    private TmxMapLoader mapLoader; //helps load the map
    private TiledMap map; //the loaded map object
    private OrthogonalTiledMapRenderer renderer; //renders the map
    private Box2DDebugRenderer box2DDebugRenderer;

    private Socket socket;

    HashMap<String, Opponent> opponents;

    public GameScreen(MainClass game) {
        mainClass = game;
        gameScreen = this;

        atlas = new TextureAtlas(Constants.SPRITE_SHEET);

        Array<TextureAtlas.AtlasRegion> runningFrames = atlas.findRegions("SpriteSheet");
        runningAnimation = new Animation(1f/120f, runningFrames, Animation.PlayMode.LOOP);
        System.out.println(runningAnimation.getKeyFrames().length);

        createCamera();
        createWorld();
        createCollisionListener();

        opponents = new HashMap<String, Opponent>();

        connectSocket();
        configSocketEvents();
    }

    private void createCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT, camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
    }

    private void createWorld() {
        box2DDebugRenderer = new Box2DDebugRenderer();
        mapLoader = new TmxMapLoader();

        map = mapLoader.load("tilemaps/lvl1.tmx");
        world = new World(new Vector2(0,-500), true);

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
    }

    private void createCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                // Check if player is grounded
                if (bodyA.getUserData() == player.groundedBody || bodyB.getUserData() == player.groundedBody)
                {
                    if (!(bodyA.getUserData() == player.playerBody || bodyB.getUserData() == player.playerBody))
                    {
                        player.setGrounded(true);
                    }
                }

                //check right wall
                if (bodyA.getUserData() == player.rightBody || bodyB.getUserData() == player.rightBody)
                {
                    if (!(bodyA.getUserData() == player.playerBody || bodyB.getUserData() == player.playerBody))
                    {
                        player.setRightCollision(true);
                    }
                }

                //check left wall
                if (bodyA.getUserData() == player.leftBody || bodyB.getUserData() == player.leftBody)
                {
                    if (!(bodyA.getUserData() == player.playerBody || bodyB.getUserData() == player.playerBody))
                    {
                        player.setLeftCollision(true);
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Body bodyA = contact.getFixtureA().getBody();
                Body bodyB = contact.getFixtureB().getBody();

                if (bodyA.getUserData() == player.groundedBody || bodyB.getUserData() == player.groundedBody)
                {
                    if (!(bodyA.getUserData() == player.playerBody || bodyB.getUserData() == player.playerBody))
                    {
                        player.setGrounded(false);
                    }
                }

                //check right wall
                if (bodyA.getUserData() == player.rightBody || bodyB.getUserData() == player.rightBody)
                {
                    if (!(bodyA.getUserData() == player.playerBody || bodyB.getUserData() == player.playerBody))
                    {
                        player.setRightCollision(false);
                    }
                }

                //check left wall
                if (bodyA.getUserData() == player.leftBody || bodyB.getUserData() == player.leftBody)
                {
                    if (!(bodyA.getUserData() == player.playerBody || bodyB.getUserData() == player.playerBody))
                    {
                        player.setLeftCollision(false);
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    public void createPlayer() {
        player = new Player(world, mainClass, this);
        System.out.println("worl");
//        TextureAtlas atlas = new TextureAtlas(Constants.SPRITE_SHEET);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        if (player != null) {
            mainClass.getBatch().begin();
            player.handleInput(delta);
            mainClass.getBatch().draw(player.handleAnimations(delta), 0, 0);
            player.draw(mainClass.getBatch());

            mainClass.getBatch().end();
        }
//        for (HashMap.Entry<String, Player> entry : friendlyopponents.entrySet()) {
//            entry.getValue().g

//            (mainClass.getBatch());
//        }

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

    public void update(float delta) {
        world.step(1f/120f,6,2);
        camera.update();
        if (player != null)
        {
            player.update(delta);
        }
        renderer.setView(camera);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void connectSocket() {
        System.out.println("[SocketIO] Connecting...");
        try {
            socket = IO.socket("http://localhost:8000");
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
                    System.out.println("[SocketIO] " + "Error getting ID");
                }
                // When connected
                createPlayer();
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[SocketIO] " + "New player connected with ID" + id);
//                    opponents.put(id, new Opponent(opponentTexture, world));
                }
                catch(JSONException e) {
                    System.out.println("[SocketIO] " + "Error getting new player ID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String id = data.getString("id");
                    System.out.println("[SocketIO] " + "New player connected with ID" + id);
                } catch (JSONException e) {
                    System.out.println("[SocketIO] " + "Error getting new player ID");
                }
            }
        });
    }
}