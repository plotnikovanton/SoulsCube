package com.soulscube.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.soulscube.game.main.Game;

import static com.soulscube.game.entities.Spawner.State.ACTIVE;
import static com.soulscube.game.entities.Spawner.State.INACTIVE;

public class Spawner extends B2DSprite {
    public static enum State {
        ACTIVE,
        INACTIVE
    }

    private State currentState;
    private Vector2 pos;
    private TextureRegion[] split;

    /**
     *
     * @param body body of spawner
     */
    public Spawner(Body body) {
        super(body);
        pos = new Vector2();
        pos.x = body.getPosition().x;
        pos.y = body.getPosition().y+0.1f;
        Texture tex = Game.res.getTexture("spawner");
        split = TextureRegion.split(tex, 6, 6)[0];
        // set inactive as default
        setAnimation(new TextureRegion[]{split[0]}, 100);
        currentState = INACTIVE;
    }

    /**
     * swap state status
     */
    public void changeState() {
        if (currentState == ACTIVE) {
            setAnimation(new TextureRegion[]{split[0]}, 100);
            currentState = INACTIVE;
        } else {
            setAnimation(new TextureRegion[]{split[1]}, 100);
            currentState = ACTIVE;
        }
    }

    /**
     *
     * @return spawn position
     */
    public Vector2 getPosition() {
        return pos;
    }
}
