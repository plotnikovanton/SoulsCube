package com.soulscube.game.handlers;

import com.badlogic.gdx.graphics.Texture;
import com.soulscube.game.main.Game;
import com.soulscube.game.states.GameEnd;
import com.soulscube.game.states.GameState;
import com.soulscube.game.states.Play;

import java.util.Stack;

public class GameStateManager {
    private Game game;
    private Stack<GameState> gameStates;
    private String[] levels = {
            "levels/level1.tmx",
            "levels/level2.tmx"
    };

    private Texture[] bgs = {
            Game.res.getTexture("bg1"),
            Game.res.getTexture("bg2")
    };
    private int curLevel;

    public static enum States {
        PLAY,
        GAME_END
    }

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<>();
        pushState(States.PLAY);
        curLevel = 0;
    }

    public Game game() { return game; }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public GameState getState(States state) {
        if (state == States.PLAY) return loadLevel(curLevel);
        if (state == States.GAME_END) return new GameEnd(this);
        return null;
    }

    public void setState(States state) {
        popState();
        pushState(state);
    }

    public void pushState(States state) {
        gameStates.push(getState(state));
    }

    public void popState() {
        GameState g = gameStates.pop();
        g.dispose();
    }

    public void nextLevel() {
        GameState g = gameStates.pop();
        g.dispose();
        curLevel++;
        if (curLevel>=levels.length) {
            pushState(States.GAME_END);
        } else {
            pushState(States.PLAY);
        }
        //return loadLevel(curLevel + 1);
    }

    public GameState loadLevel(int n) {
        curLevel = n;
        return new Play(this, levels[n], bgs[n]);
    }
}
