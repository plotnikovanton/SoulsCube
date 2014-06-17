package com.soulscube.game.handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    @Override
    public boolean keyDown(int key) {

        if (key == Keys.W || key == Keys.UP) {
            MyInput.setKey(MyInput.W, true);
        }
        if (key == Keys.A || key == Keys.LEFT) {
            MyInput.setKey(MyInput.A, true);
        }
        if (key == Keys.S || key == Keys.DOWN) {
            MyInput.setKey(MyInput.S, true);
        }
        if (key == Keys.D || key == Keys.RIGHT) {
            MyInput.setKey(MyInput.D, true);
        }
        if (key == Keys.SPACE) {
            MyInput.setKey(MyInput.SPACE, true);
        }
        return true;
    }

    @Override
    public boolean keyUp(int key) {
        if (key == Keys.W || key == Keys.UP) {
            MyInput.setKey(MyInput.W, false);
        }
        if (key == Keys.A  || key == Keys.LEFT) {
            MyInput.setKey(MyInput.A, false);
        }
        if (key == Keys.S  || key == Keys.DOWN) {
            MyInput.setKey(MyInput.S, false);
        }
        if (key == Keys.D  || key == Keys.RIGHT) {
            MyInput.setKey(MyInput.D, false);
        }
        if (key == Keys.SPACE) {
            MyInput.setKey(MyInput.SPACE, false);
        }
        return true;
    }
}
