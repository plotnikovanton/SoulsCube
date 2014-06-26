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

import static com.soulscube.game.entities.Player.State.*;

public class Player extends B2DSprite {
    public static enum State{
        RUN_RIGHT,
        RUN_LEFT,
        NORMAL,
        JUMP_RIGHT,
        JUMP_LEFT,
        DEAD,
        SPAWN
    }

    public static final float SPEED = 1f;

    private State currentState;
    private float timer;
    private float speedX;
    private Vector2 checkpoint;

    private HashMap<State, Animation> animations;

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
        tmp = split[2];
        animations.put(SPAWN, new Animation(tmp, 1.0f));
        // dead
        tmp = split[2];
        animations.put(DEAD, new Animation(tmp, 1.0f));

        currentState = DEAD;

        setState(SPAWN);
        width = animations.get(RUN_LEFT).getFrame().getRegionWidth();

    }

    public void setState(State state) {
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
                timer = 0;
                animation = animations.get(DEAD);
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

    public State getState() {
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
        System.out.println(currentState);

        if (currentState == DEAD){
            if (timer >= 5) setState(SPAWN);
            System.out.println(timer);
            timer+=dt;
        } else {


            // set speed
            if (speedX > 0) {
                if (Play.cl.isPlayerOnGround()) setState(RUN_RIGHT);
                else setState(JUMP_RIGHT);
            }
            if (speedX < 0) {
                if (Play.cl.isPlayerOnGround()) setState(RUN_LEFT);
                else setState(JUMP_LEFT);
            }
            if (speedX == 0) {
                setState(NORMAL);
            }
            Vector2 vel = body.getLinearVelocity();
            vel.x = speedX;
            body.setLinearVelocity(vel);
            speedX = 0;
        }
    }

    public void setCheckpoint(Vector2 checkpoint) {
        this.checkpoint = checkpoint;
    }
    public Vector2 getCheckpoint() {
        return checkpoint;
    }
}

