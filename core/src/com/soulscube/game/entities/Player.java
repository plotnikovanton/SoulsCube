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

/**
 * Class for Player entity
 */
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
        animations.put(SPAWN, new Animation(tmp, 0.1f));
        // dead
        tmp = new TextureRegion[split[2].length];
        for (int i=0; i<tmp.length; i++) {
            tmp[tmp.length-1-i] = split[2][i];
        }
        animations.put(DEAD, new Animation(tmp, 0.1f));

        currentState = DEAD;

        setState(SPAWN);
        width = animations.get(RUN_LEFT).getFrame().getRegionWidth();

    }

    /**
     * change current state to new state
     * @param state state to set
     */
    public void setState(State state) {
        if (state != currentState) {
            // PRESET
            if (currentState == DEAD) {
                //body.getFixtureList().first().setSensor(false);
            } else if (currentState == SPAWN) {
                speedX = 0;
                //body.getFixtureList().first().setSensor(false);
            }

            // SET
            if (state == NORMAL) {
                if (currentState == RUN_RIGHT || currentState == JUMP_RIGHT || currentState == SPAWN) {
                    setAnimation(new Animation(
                            new TextureRegion[]{animations.get(RUN_RIGHT).getFrame()}), 0);
                } else if (currentState == RUN_LEFT || currentState == JUMP_LEFT) {
                    setAnimation(new Animation(
                            new TextureRegion[]{animations.get(RUN_LEFT).getFrame()}), 0);
                }
                body.setAwake(true);
                currentState = NORMAL;
            } else if (state == DEAD) {
                timer = 0;
                animation = animations.get(DEAD);
                //body.setActive(false);
                currentState = DEAD;
            } else if (state == SPAWN) {
//                Game.res.getSound("spawn").play();
                timer = 0;
                animation = animations.get(SPAWN);
                body.setTransform(checkpoint, 0f);
                body.setAwake(true);
                currentState = SPAWN;
            } else {
                currentState = state;
                setAnimation(animations.get(state), 0);
            }
            //System.out.println(currentState);
        }
    }

    /**
     *
     * @return current state
     */
    public State getState() {
        return currentState;
    }

    /**
     * add jump force to player body
     */
    public void jump() {
        Game.res.getSound("jump").play(0.2f);
        body.applyForceToCenter(0, 200, true);
    }

    /**
     * set linear velocity to player body
     * @param speed speed value
     */
    public void addSpeed(float speed) {
        speedX += speed;
    }

    /**
     * update player state
     * @param dt delta time
     */
    public void update (float dt) {
        super.update(dt);

        // Fall
        if (body.getPosition().y < -100 / B2DVars.PPM) {
            Game.res.getSound("fall").play();
            setState(DEAD);
        }

        if (currentState == SPAWN) {
            if (timer >= 0.5) {
                speedX = 0;
                setState(NORMAL);
            }
            timer+=dt;
        } else if (currentState == DEAD){
            if (timer >= 0.5) setState(SPAWN);
            //setState(SPAWN);
            //System.out.println(timer);
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

    /**
     * change player spawn point
     * @param checkpoint new spawn point
     */
    public void setCheckpoint(Vector2 checkpoint) {
        this.checkpoint = checkpoint;
    }

    /**
     *
     * @return current player spawn point
     */
    public Vector2 getCheckpoint() {
        return checkpoint;
    }
}

