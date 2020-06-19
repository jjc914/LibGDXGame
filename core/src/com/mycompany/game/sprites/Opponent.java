package com.mycompany.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Opponent extends Sprite {
    private World world;

    Vector2 previousPosition;

    public Opponent(Texture texture, World world) {
        super(texture);
        this.world = world;
        previousPosition = new Vector2(getX(), getY());
    }
}
