package com.soulscube.game.handlers;

import com.soulscube.game.main.Game;
import com.soulscube.game.states.GameState;
import com.soulscube.game.states.Play;

import java.util.Stack;

public class GameStateManager {
    private Game game;
    private Stack<GameState> gameStates;

    public static enum States {
        PLAY
    }

    public GameStateManager(Game game) {
        this.game = game;
        gameStates = new Stack<>();
        pushState(States.PLAY);
    }

    public Game game() { return game; }

    public void update(float dt) {
        gameStates.peek().update(dt);
    }

    public void render() {
        gameStates.peek().render();
    }

    public GameState getState(States state) {
        if (state == States.PLAY) return new Play(this, "levels/test.tmx");
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
}
