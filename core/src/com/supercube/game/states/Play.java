package com.supercube.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.supercube.game.entities.Cube;
import com.supercube.game.entities.Player;
import com.supercube.game.handlers.B2DVars;
import com.supercube.game.handlers.GameStateManager;
import com.supercube.game.handlers.MyContactListener;
import com.supercube.game.handlers.MyInput;
import com.supercube.game.main.Game;

import static com.supercube.game.handlers.B2DVars.PPM;

public class Play extends GameState {
    // set up b2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;
    public static MyContactListener cl;


    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;
    private float tileSize;

    public static Player player;
    public static Cube cube;
    private boolean debug = true;

    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();

        // create player
        createPlayer();

        // create cube
        createCube();

        // create walls
        createWalls();

        // create tiles
        //createTiles();

        // set cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);



    }

    @Override
    public void handleInput() {
        if (MyInput.isPressed(MyInput.SPACE)) {
            cube.toggle();
        }

        if (cube.getCurrentState() != Cube.CONTROLLED) {
            //IF PLAYER IS ACTIVE
            if (MyInput.isDown(MyInput.A)) {
                player.addSpeed(-Player.SPEED);
            }

            if (MyInput.isDown(MyInput.D)) {
                player.addSpeed(Player.SPEED);
            }

            if (MyInput.isPressed(MyInput.W)) {
                if (cl.isPlayerOnGround()) {
                    player.jump();
                }
            }
        } else {
            //IF CUBE IS ACTIVE
            if (MyInput.isDown(MyInput.A)) {
                cube.velocity.x = -Cube.SPEED;
            } else if (MyInput.isDown(MyInput.D)) {
                cube.velocity.x = Cube.SPEED;
            } else {
                cube.velocity.x = 0;
            }

            if (MyInput.isDown(MyInput.W)) {
                cube.velocity.y = Cube.SPEED;
            } else if (MyInput.isDown(MyInput.S)) {
                cube.velocity.y = -Cube.SPEED;
            } else {
                cube.velocity.y = 0;
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
        player.update(dt);
        cube.update(dt);

        // set camera follow
        if (cube.getCurrentState() != Cube.CONTROLLED) {
            cam.position.lerp(
                    new Vector3(player.getPosition().x * PPM, player.getPosition().y * PPM, 0), 3f*dt);
        } else {
            cam.position.lerp(
                    new Vector3(cube.getPosition().x * PPM, cube.getPosition().y * PPM, 0), 3f*dt);
        }
        cam.update();

        if (debug) {
            if (cube.getCurrentState() != Cube.CONTROLLED) {
                b2dCam.position.lerp(
                        new Vector3(player.getPosition().x, player.getPosition().y, 0), 3f*dt);
            } else {
                b2dCam.position.lerp(
                        new Vector3(cube.getPosition().x, cube.getPosition().y, 0), 3f*dt);
            }
            b2dCam.update();

        }

    }

    @Override
    public void render() {
        Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 );
        Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        // draw tile map
        tmr.setView(cam);
        tmr.render();

        // draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);
        //cube.render(sb);

        // draw box2d world
        if (debug) b2dr.render(world, b2dCam.combined);
    }


    @Override
    public void dispose() {

    }

    private void createCube() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.gravityScale = 0;
        bdef.position.set(150 / PPM, 210 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        fdef.filter.categoryBits = B2DVars.BIT_CUBE;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;
        shape.setAsBox(5 / PPM, 5 / PPM);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData("cube");
        cube = new Cube(body);
    }

    private void createPlayer() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        // create player
        bdef.position.set(160 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_CUBE;
        shape.setAsBox(5f / PPM, 9 / PPM);
        fdef.shape = shape;

        player = new Player(body);
        body.createFixture(fdef).setUserData(player);


        // create foot sensor
        shape.setAsBox(4f / PPM, 2 / PPM, new Vector2(0, -10 / PPM), 0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND | B2DVars.BIT_CUBE;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");
    }

    private void createWalls() {
        tileMap = new TmxMapLoader().load("levels/test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        MapObjects walls = tileMap.getLayers().get("walls").getObjects();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Shape shape = null;
        for (MapObject wall : walls) {
            if (wall instanceof TextureMapObject) continue;
            if (wall instanceof RectangleMapObject) shape = getRectangle((RectangleMapObject)wall);
            else if (wall instanceof PolygonMapObject) shape = getPolygon((PolygonMapObject)wall);

            bdef.type = BodyDef.BodyType.StaticBody;
            fdef.friction = 0;
            fdef.filter.categoryBits = B2DVars.BIT_GROUND;
            fdef.filter.maskBits = B2DVars.BIT_PLAYER | B2DVars.BIT_CUBE;
            fdef.shape = shape;
            fdef.isSensor = false;
            world.createBody(bdef).createFixture(fdef);

            shape.dispose();
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / PPM,
                (rectangle.y + rectangle.height * 0.5f ) / PPM);
        polygon.setAsBox(rectangle.width * 0.5f / PPM,
                rectangle.height * 0.5f / PPM,
                size,
                0.0f);
        return polygon;
    }

    private PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / PPM;
        }
        polygon.set(worldVertices);
        return polygon;
    }
}
