package com.soulscube.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soulscube.game.main.Game;

public class HUD {
    private Player player;

    public HUD(Player player) {
        this.player = player;

        Texture tex = Game.res.getTexture("hud");

    }

    public void render(SpriteBatch sb) {

    }
}
