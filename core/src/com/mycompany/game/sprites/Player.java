package com.mycompany.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Sprite implements InputProcessor {
    private World world;

    public Body playerBody;
    public Body groundedBody;

    private boolean isGrounded;
    private boolean jumpReleased;

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

    public void handleInput(float delta)  {
        float moveForce = 200f;
        float jumpForce = 90f;

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



        if (!Gdx.input.isKeyPressed(Input.Keys.UP)) {
            Vector2 force = new Vector2(0f, -1);
            playerBody.applyForce(force, playerBody.getWorldCenter(), true);
        }

        groundedBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y - 7.5f, 0f);
    }

    public void setGrounded(boolean isGrounded) {
        this.isGrounded = isGrounded;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
