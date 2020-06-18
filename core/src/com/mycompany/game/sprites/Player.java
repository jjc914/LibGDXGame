package com.mycompany.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite {
    private World world;

    public Body playerBody;
    public Body groundedBody;
    public Body leftBody;
    public Body rightBody;


    private boolean isGrounded;
    private boolean isRight;
    private boolean isLeft;

    public Player(World world)
    {
        this.world = world;
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
        float jumpForce = 130;

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
            Vector2 force = new Vector2(0f, jumpForce);
            playerBody.setLinearVelocity(force);
        }

        if (playerBody.getLinearVelocity().y < 0) {
            Vector2 force = new Vector2(0f, -200);
            playerBody.applyLinearImpulse(force, playerBody.getWorldCenter(), true);
        }

        groundedBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y - 7.5f, 0f);
        leftBody.setTransform(playerBody.getPosition().x - 7.5f, playerBody.getPosition().y, 0f);
        rightBody.setTransform(playerBody.getPosition().x + 7.5f, playerBody.getPosition().y, 0f);

    }

    //TODO: fix bug where if you walk into wall, you stop y velocity movement

    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    public void rightCollision(boolean isRight) {
        this.isRight = isRight;
    }

    public void leftCollision(boolean isLeft) {
        this.isLeft = isLeft;
    }
}
