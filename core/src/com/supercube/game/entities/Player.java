package com.supercube.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.supercube.game.handlers.Animation;
import com.supercube.game.main.Game;
import com.supercube.game.states.Play;

import java.util.HashMap;

public class Player extends B2DSprite {
    public static final int RUN_RIGHT = 0;
    public static final int RUN_LEFT = 1;
    public static final int NORMAL = 2;
    public static final int JUMP_RIGHT = 3;
    public static final int JUMP_LEFT = 4;

    public static final float SPEED = 1f;

    private int currentState;
    private float speedX;

    private HashMap<Integer, Animation> animations;

    public Player(Body body) {
        super(body);
        speedX = 0;

        animations = new HashMap<>();

        Texture tex = Game.res.getTexture("death");
        TextureRegion split[][] = TextureRegion.split(tex, 20, 20);
        TextureRegion mirror[][] = TextureRegion.split(tex, 20, 20);
        for (TextureRegion[] i : mirror)
            for (TextureRegion j : i)
                j.flip(true, false);


        // run right
        TextureRegion tmp[] = split[0];
        animations.put(RUN_RIGHT, new Animation(tmp));
        // run left
        tmp = mirror[0];
        animations.put(RUN_LEFT, new Animation(tmp));
        // jump right
        tmp = split[1];
        animations.put(JUMP_RIGHT, new Animation(tmp));
        // jump left
        tmp = mirror[1];
        animations.put(JUMP_LEFT, new Animation(tmp));

        currentState = -1;

        setState(RUN_RIGHT);
        width = animations.get(RUN_LEFT).getFrame().getRegionWidth();

    }

    public void setState(int state) {
        if (state != currentState) {
            if (state == NORMAL) {
                if (currentState == RUN_RIGHT || currentState == JUMP_RIGHT) {
                    setAnimation(new Animation(
                            new TextureRegion[]{animations.get(RUN_RIGHT).getFrame()}), 0);
                } else if (currentState == RUN_LEFT || currentState == JUMP_LEFT) {
                    setAnimation(new Animation(
                            new TextureRegion[]{animations.get(RUN_LEFT).getFrame()}), 0);
                }
                currentState = NORMAL;
            } else {
                currentState = state;
                setAnimation(animations.get(state), 0);
            }
        }
    }

    public int getState() {
        return currentState;
    }

    public void jump() {
        body.applyForceToCenter(0, 200, true);
    }

    public void addSpeed(float speed) {
        speedX += speed;
    }

    public void update (float dt) {
        super.update(dt);

        // set speed
        if (speedX > 0) {
            if (Play.cl.isPlayerOnGround() ) setState(RUN_RIGHT);
            else setState(JUMP_RIGHT);
        }
        if (speedX < 0) {
            if (Play.cl.isPlayerOnGround() ) setState(RUN_LEFT);
            else setState(JUMP_LEFT);
        } if (speedX == 0) {
            setState(NORMAL);
        }
        Vector2 vel = body.getLinearVelocity();
        vel.x = speedX;
        body.setLinearVelocity(vel);
        speedX = 0;
    }
}

