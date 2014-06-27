package com.soulscube.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.soulscube.game.main.Game;

public class HUD {
    private Player player;
    private BitmapFont font;
    private Array<Coin> coins;
    private Integer totalCoins;
    private TextureRegion candy;

    public HUD(Player player, Array<Coin> coins, Integer totalCoins) {
        this.player = player;
        this.coins = coins;
        this.totalCoins = totalCoins;

        font = new BitmapFont(Gdx.files.internal("font/visitor.fnt"), Gdx.files.internal("font/visitor.png"), false);

        Texture tex = Game.res.getTexture("hud");
        candy = new TextureRegion(tex, 1, 11, 6, 6);

    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(candy, 20, 220);
        font.draw(sb, totalCoins-coins.size + "/" + totalCoins, 30, 226.5f);
        sb.end();

    }
}
