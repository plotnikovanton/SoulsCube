package com.soulscube.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.soulscube.game.handlers.Animation;
import com.soulscube.game.main.Game;

/**
 * candy object
 */
public class Coin extends B2DSprite {
    public Coin(Body body) {
        super(body);
        Texture tex = Game.res.getTexture("coin");
        TextureRegion[] texReg = TextureRegion.split(tex, 6, 6)[0];
        setAnimation(new Animation(texReg), 0);
    }
}
