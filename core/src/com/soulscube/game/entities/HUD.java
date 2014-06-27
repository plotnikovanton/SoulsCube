package com.soulscube.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.soulscube.game.main.Game;

public class HUD {
    private Player player;
    private BitmapFont font;
    private Array<Coin> coins;
    private Integer totalCoins;

    public HUD(Player player, Array<Coin> coins, Integer totalCoins) {
        this.player = player;
        this.coins = coins;
        this.totalCoins = totalCoins;

        font = new BitmapFont();

        Texture tex = Game.res.getTexture("hud");

    }

    public void render(SpriteBatch sb) {
        sb.begin();
            font.draw(sb, totalCoins-coins.size + " : " + totalCoins, 10, 20);
        sb.end();

    }
}
