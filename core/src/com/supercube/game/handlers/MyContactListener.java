package com.supercube.game.handlers;

import com.badlogic.gdx.physics.box2d.*;

public class MyContactListener implements ContactListener {
    private int numFootContacts;
    private boolean oldCube;
    private boolean newCube;

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();


        if (logic(fa, fb)) {
            numFootContacts++;
        }

        if (logic(fb, fa)) {
            numFootContacts++;
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (logic(fa, fb)) {
            numFootContacts--;
        }

        if (logic(fb, fa)) {
            numFootContacts--;
        }
    }

    private boolean logic(Fixture fa, Fixture fb) {
        return fa.getUserData() != null && fa.getUserData().equals("foot") ;//&& (fb.getUserData() == null || !(fb.getUserData().equals("cube")));
    }

    public boolean isPlayerOnGround() {
        return numFootContacts>0;
    }







    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
