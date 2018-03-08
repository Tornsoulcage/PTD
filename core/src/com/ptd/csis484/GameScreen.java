package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;


import java.util.ArrayList;
import java.util.List;

//TODO Add popup to ask for tower type when user taps on the map
//TODO Vary enemy types that spawn
//TODO Add experience reward on enemy death
//TODO Add gold reward on enemy death
//TODO Add Gold cost to create a tower
//TODO Add ability to level towers up
//TODO Add ability to save user progress
//TODO Add procedural generated maps

/**
 * Created by scott on 2/18/2018.
 */

//Represents the Game Screen. Where we actually play the game
public class GameScreen implements Screen, InputProcessor {
    final PTD game;
    OrthographicCamera camera;

    //We use a tile map because it makes more sense with placing towers
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    ShapeRenderer shapeRenderer;

    //Gets the dimensions of the tiles
    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    //List to keep track of enemies and a count of how many have been spawned
    List<Enemy> enemyList = new ArrayList<Enemy>();
    int enemyCount = 0;

    //Keeps track of the wave number
    int waveNumber = 1;

    //Lists to keep track of towers and bullets
    List<Tower> towerList = new ArrayList<Tower>();
    List<Bullet> bulletList = new ArrayList<Bullet>();

    //Counts the number of times render has been called
    //Used to space out the spawning of enemies and bullets
    int renderCount = 0;

    //Creating the Screen and rendering a test map
    public GameScreen(final PTD game) {
        this.game = game;
        int viewportWidth = 480;
        int viewportHeight = 320;


        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        tiledMap = new TmxMapLoader().load("PTDMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        //Shape Renderer is how we draw the enemies
        shapeRenderer = new ShapeRenderer();
    }

    //The "game loop"
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        //Starting the shape renderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Looping through the towers and updating and drawing them
        for (int i = 0; i < towerList.size(); i++) {
            towerList.get(i).update(enemyList, delta);
            towerList.get(i).render(shapeRenderer);

            //Every 30 renders wo spawn a new bullet, about 2 times a second
            if (renderCount % 30 == 0) {
                if (!enemyList.isEmpty()) {
                    bulletList.add(new Bullet(towerList.get(i).damage, towerList.get(i).target, towerList.get(i)));
                }
            }
        }

        //Spawns a new enemy every 40 renders
        if (renderCount % 40 == 0) {
            //Wave size is 20 enemies so we stop once we get there
            if (enemyCount != 20) {
                double enemyToSpawn = Math.random() * 3;
                switch ((int) enemyToSpawn) {
                    case 0: enemyList.add(new Enemy(Enemy.enemyType.ROCK, waveNumber));
                            break;
                    case 1: enemyList.add(new Enemy(Enemy.enemyType.PAPER, waveNumber));
                            break;
                    case 2: enemyList.add(new Enemy(Enemy.enemyType.SCISSORS, waveNumber));
                            break;
                }
                enemyCount++;
            }
        }

            //Update loop for all of the enemies
        for (int i = 0; i < enemyList.size(); i++) {
            //Updates the enemies with the current list of bullets
            //Checks if any bullets have hit this enemy and responds accordingly
            enemyList.get(i).update(delta, bulletList);

            //If our enemy leaves the bounds of the screen we remove it from the game
            //TODO add penalty for this occurring
            if(enemyList.get(i).position.x > deviceWidth || enemyList.get(i).position.x < 0 ||
                    enemyList.get(i).position.y > deviceHeight || enemyList.get(i).position.y < 0){
                enemyList.remove(i);
            } else if(enemyList.get(i).health <= 0) {
                enemyList.get(i).destroyed = true;
                enemyList.remove(i);
            } else {
                enemyList.get(i).render(shapeRenderer);
            }
            //If the enemy has taken enough damage we remove it and mark the enemy object has destroyed



        }

            //Update loop for all of the bullets
        for (int i = 0; i < bulletList.size(); i++) {
            bulletList.get(i).update(delta);

                //If our target has been destroyed we change targets
            if (bulletList.get(i).target.destroyed) {
                bulletList.get(i).changeTarget();
            }

            //If our bullet is sitting at the start position we remove it
            if (bulletList.get(i).position.x == bulletList.get(i).target.waypointStart.x &&
                    bulletList.get(i).position.y == bulletList.get(i).target.waypointStart.y) {
                bulletList.remove(i);
            } else {
                bulletList.get(i).render(shapeRenderer);
            }
        }

        //If all of our enemies have been destroyed and we've reached the end of the wave we remove all of our bullets
        if (enemyList.isEmpty() && enemyCount == 20) {
            bulletList = new ArrayList<Bullet>();
            waveNumber++;
            enemyCount = 0;
        }

        //Incrementing the render count and ending the shape renderer
        renderCount++;
        shapeRenderer.end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float cellX = screenX/tileWidth;
        float cellY = screenY/tileHeight;
        cellX = MathUtils.floor(cellX);
        cellY = MathUtils.floor(cellY);

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        TiledMapTileLayer.Cell cell = layer.getCell((int) cellX, (int) (10 - 1 - cellY));
        Object property = cell.getTile().getProperties().get("TowerTile");

        if(property != null)
            towerList.add(new Tower(Tower.towerType.ROCK, cellX, cellY));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
