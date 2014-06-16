package com.supercube.game.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.supercube.game.handlers.Context;
import com.supercube.game.handlers.GameStateManager;
import com.supercube.game.handlers.MyInput;
import com.supercube.game.handlers.MyInputProcessor;

public class Game implements ApplicationListener{
    public static final String TITLE = "Super Block";
    public static int V_WIDTH = 320;
    public static int V_HEIGHT = 240;
    public static int SCALE = 2;

    public static final float STEP = 1/60f;
    private float accum;

    private SpriteBatch sb;
    private OrthographicCamera cam;
    private OrthographicCamera hudCam;

    private GameStateManager gsm;
    public static Context res;

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getCamera() { return cam; }
    public OrthographicCamera getHudCamera() { return hudCam; }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MyInputProcessor());

        res = new Context();
        res.loadTexture("death.png", "death");

        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        hudCam = new OrthographicCamera();
        hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        gsm = new GameStateManager(this);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        accum += Gdx.graphics.getDeltaTime();
        while (accum >= STEP) {
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();

            MyInput.update();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
