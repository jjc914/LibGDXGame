/*
package com.mycompany.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mycompany.game.Constants;
import com.mycompany.game.MainClass;
import com.mycompany.game.screens.GameScreen;

import javax.swing.plaf.nimbus.State;

public class Player extends Sprite {
    private MainClass mainClass;
    private GameScreen gameScreen;
    private World world;

    public Body playerBody;
    public Body groundedBody;
    public Body leftBody;
    public Body rightBody;

    private Animation<TextureRegion> runningAnimation;

    public boolean isGrounded;
    public boolean isTouchingRight;
    public boolean isTouchingLeft;

    public Player(World world, MainClass mainClass, GameScreen gameScreen)
    {
        this.world = world;
        this.mainClass = mainClass;
        this.gameScreen = gameScreen;

        definePlayerBox2d();
    }

    public void definePlayerBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(50, 70);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(7, 7);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;

        playerBody.createFixture(fixtureDef);
        playerBody.setUserData(playerBody);

        defineGroundedBox2d();
        rightCollisionBox2d();
        leftCollisionBox2d();
    }

    public void defineGroundedBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        groundedBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(6.5f, 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        groundedBody.createFixture(fixtureDef);
        groundedBody.setUserData(groundedBody);
    }

    public void rightCollisionBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        rightBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1f, 6.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        rightBody.createFixture(fixtureDef);
        rightBody.setUserData(rightBody);
    }

    public void leftCollisionBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        leftBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1, 6.6f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        leftBody.createFixture(fixtureDef);
        leftBody.setUserData(leftBody);
    }

    public void handleInput(float delta)  {
        float moveForce = 200f;
        float jumpForce = 180f;

        // lock rotation
        playerBody.setFixedRotation(true);

        // left-right movement
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            Vector2 force = new Vector2(moveForce, playerBody.getLinearVelocity().y);
            playerBody.setLinearVelocity(force);
        }

        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            Vector2 force = new Vector2(-moveForce, playerBody.getLinearVelocity().y);
            playerBody.setLinearVelocity(force);
        }
        else
        {
            playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);
        }

        // jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && isGrounded)
        {
            Vector2 force = new Vector2(playerBody.getLinearVelocity().x, jumpForce);
            playerBody.setLinearVelocity(force);
        }

        if (playerBody.getLinearVelocity().y < 0) {
            Vector2 force = new Vector2(0f, -200);
            playerBody.applyLinearImpulse(force, playerBody.getWorldCenter(), true);
        }

        if (isTouchingLeft) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x < 0 ? 0 : playerBody.getLinearVelocity().x, playerBody.getLinearVelocity().y+4f);
        }



        if (isTouchingRight) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x > 0 ? 0 : playerBody.getLinearVelocity().x, playerBody.getLinearVelocity().y+4f);
        }


        groundedBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y - 7.5f, 0f);
        rightBody.setTransform(playerBody.getPosition().x + 7.5f, playerBody.getPosition().y, 0f);
        leftBody.setTransform(playerBody.getPosition().x - 7.5f, playerBody.getPosition().y, 0f);

    }

    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public void setRightCollision(boolean isTouchingRight)
    {
        this.isTouchingRight = isTouchingRight;
    }

    public void setLeftCollision(boolean isTouchingLeft)
    {
        this.isTouchingLeft = isTouchingLeft;
    }
}
*/
package com.mycompany.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mycompany.game.Constants;
import com.mycompany.game.MainClass;
import com.mycompany.game.screens.GameScreen;

import javax.swing.plaf.nimbus.State;

public class Player extends Sprite {
    private MainClass mainClass;
    private GameScreen gameScreen;
    private World world;

    public Body playerBody;
    public Body groundedBody;
    public Body leftBody;
    public Body rightBody;

    private boolean isGrounded;
    private boolean isTouchingRight;
    private boolean isTouchingLeft;

    public TextureRegion currentFrame;
    private float elapsed_time = 0f;

    //states
    public enum State {STANDING};
    public State currentState;
    private boolean runningToRight;

    public Player(World world, MainClass mainClass, GameScreen gameScreen)
    {
        this.world = world;
        this.mainClass = mainClass;
        this.gameScreen = gameScreen;

        definePlayerBox2d();
    }

    public void definePlayerBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(50, 80);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(7, 7);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;

        playerBody.createFixture(fixtureDef);
        playerBody.setUserData(playerBody);

        defineGroundedBox2d();
        rightCollisionBox2d();
        leftCollisionBox2d();
    }

    public void defineGroundedBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        groundedBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(6.5f, 1);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        groundedBody.createFixture(fixtureDef);
        groundedBody.setUserData(groundedBody);
    }

    public void rightCollisionBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        rightBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1f, 6.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        rightBody.createFixture(fixtureDef);
        rightBody.setUserData(rightBody);
    }

    public void leftCollisionBox2d() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        leftBody = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1, 6.6f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;

        leftBody.createFixture(fixtureDef);
        leftBody.setUserData(leftBody);
    }

    public void handleInput(float delta)  {
        float moveForce = 200f;
        float jumpForce = 130f;

        // lock rotation
        playerBody.setFixedRotation(true);

        // left-right movement
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            Vector2 force = new Vector2(moveForce, playerBody.getLinearVelocity().y);
            playerBody.setLinearVelocity(force);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            Vector2 force = new Vector2(-moveForce, playerBody.getLinearVelocity().y);
            playerBody.setLinearVelocity(force);
        }
        else
        {
            playerBody.setLinearVelocity(0f, playerBody.getLinearVelocity().y);
        }

        // jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && isGrounded)
        {
            Vector2 force = new Vector2(playerBody.getLinearVelocity().x, jumpForce);
            playerBody.setLinearVelocity(force);
        }

        if (playerBody.getLinearVelocity().y < 0) {
            Vector2 force = new Vector2(0f, -200);
            playerBody.applyLinearImpulse(force, playerBody.getWorldCenter(), true);
        }

        if (isTouchingLeft) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x < 0 ? 0 : playerBody.getLinearVelocity().x, playerBody.getLinearVelocity().y);
        }

        if (isTouchingRight) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x > 0 ? 0 : playerBody.getLinearVelocity().x, playerBody.getLinearVelocity().y);
        }

        groundedBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y - 7.5f, 0f);
        rightBody.setTransform(playerBody.getPosition().x + 7.5f, playerBody.getPosition().y, 0f);
        leftBody.setTransform(playerBody.getPosition().x - 7.5f, playerBody.getPosition().y, 0f);

        setPosition(playerBody.getPosition().x - getWidth() / 2, playerBody.getPosition().y - getHeight() / 2);
    }

    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public void setRightCollision(boolean isTouchingRight)
    {
        this.isTouchingRight = isTouchingRight;
    }

    public void setLeftCollision(boolean isTouchingLeft)
    {
        this.isTouchingLeft = isTouchingLeft;
    }

    public void update(float deltaTime)
    {

    }
}