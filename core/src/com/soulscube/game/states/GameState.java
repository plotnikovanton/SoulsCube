package com.soulscube.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soulscube.game.handlers.GameStateManager;
import com.soulscube.game.main.Game;

public abstract class GameState {
    protected GameStateManager gsm;
    protected Game game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.game = gsm.game();
        this.sb = game.getSpriteBatch();
        this.cam = game.getCamera();
        this.hudCam = game.getHudCamera();


    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();
}
