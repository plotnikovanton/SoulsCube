package com.soulscube.game.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener {
    private int numFootContacts;
    private int numSpikesContacts;
    private Array<Body> bodyToRemove;
    private Vector2 checkpoint;

    public MyContactListener(Vector2 startPoint) {
        super();
        bodyToRemove = new Array<>();
        checkpoint = startPoint;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot") || fb.getUserData() != null && fb.getUserData().equals("foot")) {
            startFootContact(fa, fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("spike") || fb.getUserData() != null && fb.getUserData().equals("spike")) {
            startSpikeContact(fa, fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("candy") || fb.getUserData() != null && fb.getUserData().equals("candy")) {
            startCandyContact(fa, fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("checkpoint") || fb.getUserData() != null && fb.getUserData().equals("checkpoint")) {
            startCheckpointContact(fa, fb);
        }

    }
    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;


        if (fa.getUserData() != null && fa.getUserData().equals("foot") || fb.getUserData() != null && fb.getUserData().equals("foot")) {
            endFootContact(fa, fb);
        }

        if (fa.getUserData() != null && fa.getUserData().equals("spike") || fb.getUserData() != null && fb.getUserData().equals("spike")) {
            endSpikeContact(fa, fb);
        }
    }

    //Contacts
    private void startCheckpointContact(Fixture fa, Fixture fb) {
        if (fb.getUserData() != null && fb.getUserData().equals("checkpoint")) {
            Fixture tmp = fb;
            fb = fa;
            fa = tmp;
        }

        checkpoint = fa.getBody().getPosition();
    }
    private void startCandyContact(Fixture fa, Fixture fb) {
        if (fb.getUserData() != null && fb.getUserData().equals("candy")) {
            Fixture tmp = fb;
            fb = fa;
            fa = tmp;
        }

        bodyToRemove.add(fa.getBody());
    }
    private void startSpikeContact(Fixture fa, Fixture fb) {
        if (fb.getUserData() != null && fb.getUserData().equals("spike")) {
            Fixture tmp = fb;
            fb = fa;
            fa = tmp;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("player")) {
            numSpikesContacts++;
        }
    }
    private void endSpikeContact(Fixture fa, Fixture fb) {
        if (fb.getUserData() != null && fb.getUserData().equals("spike")) {
            Fixture tmp = fb;
            fb = fa;
            fa = tmp;
        }

        if (fb.getUserData() != null && fb.getUserData().equals("player")) {
            numSpikesContacts--;
        }
    }
    private void startFootContact(Fixture fa, Fixture fb) {
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            Fixture tmp = fb;
            fb = fa;
            fa = tmp;
        }
        numFootContacts++;
    }
    private void endFootContact(Fixture fa, Fixture fb) {
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            Fixture tmp = fb;
            fb = fa;
            fa = tmp;
        }
        numFootContacts--;
    }
    public boolean isPlayerOnGround() {
        return numFootContacts>0;
    }
    public boolean isPlayerOnSpike() {
        return numSpikesContacts>0;
    }

    // Getters
    public Array<Body> getBodyToRemove() {
        return bodyToRemove;
    }
    public Vector2 getCheckpoint() {
        return checkpoint;
    }

    // Useless
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
