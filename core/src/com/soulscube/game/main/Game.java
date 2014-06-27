package com.soulscube.game.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.soulscube.game.handlers.Context;
import com.soulscube.game.handlers.GameStateManager;
import com.soulscube.game.handlers.MyInput;
import com.soulscube.game.handlers.MyInputProcessor;

/**
 * game class
 */
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

    /**
     * create main objects and load resources
     */
    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MyInputProcessor());

        res = new Context();
        res.loadTexture("death.png", "death");
        res.loadTexture("hud.png", "hud");
        res.loadTexture("coin.png", "coin");
        res.loadTexture("spawner.png", "spawner");
        res.loadTexture("cube.png", "cube");
        res.loadTexture("levels/summer_bg.png", "bg");

        res.loadMusic("game.mp3", "main_music");
        res.getMusic("main_music").setLooping(true);
        res.getMusic("main_music").setVolume(0.9f);
        res.getMusic("main_music").play();

        res.loadSound("sfx/candy.wav", "earn_coin");
        res.loadSound("sfx/laugh.wav", "laugh");
        res.loadSound("sfx/jump.wav", "jump");
        res.loadSound("sfx/fall.ogg", "fall");
        res.loadSound("sfx/blade.ogg", "spike");
        res.loadSound("sfx/swap.wav", "swap");
        res.loadSound("sfx/timer.wav", "timer");

        //res.loadSound("sfx/spawn.wav", "spawn");

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
