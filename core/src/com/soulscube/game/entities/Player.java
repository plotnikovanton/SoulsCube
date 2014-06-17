package com.soulscube.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.soulscube.game.handlers.Animation;
import com.soulscube.game.handlers.B2DVars;
import com.soulscube.game.main.Game;
import com.soulscube.game.states.Play;

import java.util.HashMap;

public class Player extends B2DSprite {
    public static final int RUN_RIGHT = 0;
    public static final int RUN_LEFT = 1;
    public static final int NORMAL = 2;
    public static final int JUMP_RIGHT = 3;
    public static final int JUMP_LEFT = 4;
    public static final int DEAD = 5;
    public static final int SPAWN = 6;

    public static final float SPEED = 1f;

    private int currentState;
    private float speedX;
    private Vector2 checkpoint;

    private HashMap<Integer, Animation> animations;

    public Player(Body body, Vector2 checkpoint) {
        super(body);
        this.checkpoint = checkpoint;
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
        // spawn
        tmp = split[0];
        animations.put(SPAWN, new Animation(tmp));
        // dead
        tmp = split[0];
        animations.put(DEAD, new Animation(tmp));

        currentState = -1;

        setState(SPAWN);
        width = animations.get(RUN_LEFT).getFrame().getRegionWidth();

    }

    public void setState(int state) {
        if (state != currentState) {
            // PRESET
            if (currentState == DEAD) {
                body.getFixtureList().first().setSensor(false);
            } else if (currentState == SPAWN) {
                body.getFixtureList().first().setSensor(false);
            }

            // SET
            if (state == NORMAL) {
                if (currentState == RUN_RIGHT || currentState == JUMP_RIGHT) {
                    setAnimation(new Animation(
                            new TextureRegion[]{animations.get(RUN_RIGHT).getFrame()}), 0);
                } else if (currentState == RUN_LEFT || currentState == JUMP_LEFT) {
                    setAnimation(new Animation(
                            new TextureRegion[]{animations.get(RUN_LEFT).getFrame()}), 0);
                }
                currentState = NORMAL;
            } else if (state == DEAD) {
                animation = animations.get(DEAD);
                setState(SPAWN);

            } else if (state == SPAWN) {
                animation = animations.get(SPAWN);
                body.setTransform(checkpoint, 0f);
                body.setActive(true);
                setState(RUN_RIGHT);
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
        if (body.getPosition().y < -100 / B2DVars.PPM) {
            setState(DEAD);
        }


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

    public void setCheckpoint(Vector2 checkpoint) {
        this.checkpoint = checkpoint;
    }
    public Vector2 getCheckpoint() {
        return checkpoint;
    }
}

