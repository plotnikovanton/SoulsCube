package com.soulscube.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.soulscube.game.handlers.GameStateManager;

public class GameEnd extends GameState {
    private BitmapFont font;

    public GameEnd(GameStateManager gsm) {
        super(gsm);

        font = new BitmapFont(Gdx.files.internal("font/visitor.fnt"), Gdx.files.internal("font/visitor.png"), false);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        Gdx.graphics.getGL20().glClearColor( 0, 0, 0, 1 );
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sb.setProjectionMatrix(hudCam.combined);
        sb.begin();
        font.draw(sb, "gg wp!", 20, 20);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
