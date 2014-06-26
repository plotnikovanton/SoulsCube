package com.soulscube.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.soulscube.game.handlers.B2DVars;

public class Cube extends B2DSprite {
    public static final int CONTROLLED = 0;
    public static final int FOLLOW = 1;
    public static final int WAIT = 2;

    public static final float SPEED = 1f;

    private int currentState;
    private float timer;
    public Vector2 velocity;
    private Vector2 target;
    private Player player;

    public Cube(Body body, Player player) {
        super(body);
        this.player = player;
        velocity = new Vector2(0,0);
        currentState = -1;
        setState(FOLLOW);
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setState(int state) {
        if (currentState != state){
            // preset
            if (currentState == WAIT) {
                Filter filter = body.getFixtureList().first().getFilterData();
                filter.maskBits = B2DVars.BIT_GROUND;
                body.getFixtureList().first().setFilterData(filter);
                //body.setType(BodyDef.BodyType.DynamicBody);
                player.getBody().setAwake(true);
            } else if (currentState == FOLLOW) {
                body.getFixtureList().first().setSensor(false);
            }
            // set
            if (state == WAIT) {
                timer = 0;
                Filter filter = body.getFixtureList().first().getFilterData();
                filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_PLAYER;
                body.getFixtureList().first().setFilterData(filter);
                //body.setType(BodyDef.BodyType.KinematicBody);
                //body.getFixtureList().first().setSensor(false);
            } else if (state == FOLLOW) {
                body.getFixtureList().first().setSensor(true);
            }
            // post set
            // System.out.println("cube status: " + state);
            this.currentState = state;
        }
    }

    public void toggle() {
        if (currentState == CONTROLLED) {
            setState(WAIT);
        } else {
            setState(CONTROLLED);
        }
    }

    @Override
    public void update (float dt) {
        super.update(dt);

        if (currentState == WAIT) {
            velocity.x = 0;
            velocity.y = 0;
            timer += dt;
            if (timer > 5f) setState(FOLLOW);
        }

        if (currentState == FOLLOW) {
            target = new Vector2();
            target.x = player.getPosition().x; //+
                    //Math.signum(Play.player.getPosition().x-getPosition().x)*13 / B2DVars.PPM;
            target.y = player.getPosition().y + 13/B2DVars.PPM;
            Vector2 vel = target.sub(getPosition()).scl(5);
            velocity = vel;
        }

        body.setLinearVelocity(velocity);
        //System.out.println(velocity.x + " " + velocity.y);

        velocity.x = 0;
        velocity.y = 0;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        BitmapFont font = new BitmapFont();
        font.setColor(Color.RED);

        if (currentState == WAIT) {
            sb.begin();
            font.draw(sb, Integer.toString((int) timer), body.getPosition().x * B2DVars.PPM, body.getPosition().y * B2DVars.PPM);
            font.draw(sb, "HELLO WORLD", 150, 150);
            sb.end();
        }
    }
}
