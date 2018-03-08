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
//TODO Add experience reward on enemy death
//TODO Add gold reward on enemy death
//TODO Add Gold cost to create a tower
//TODO Add ability to level towers up
//TODO Add ability to save user progress
//TODO Add procedural generated maps

/**
 * Description: Runs the actual game
 *
 * Created by Joseph A Scott on 2/18/2018.
 */

//Represents the Game Screen. Where we actually play the game
public class GameScreen implements Screen, InputProcessor {
    private final PTD game;
    private OrthographicCamera camera;

    //Stats on the tile sizes for the map
    private final int TILE_X_COUNT = 15;
    private final int TILE_Y_COUNT = 10;
    private final int TILE_SIDE_LENGTH = 32;

    //We use a tile map because it makes more sense with placing towers
    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private ShapeRenderer shapeRenderer;

    //Gets the dimensions of the tiles
    private float deviceHeight = Gdx.graphics.getHeight();
    private float deviceWidth = Gdx.graphics.getWidth();
    private float tileHeight = deviceHeight/TILE_Y_COUNT;
    private float tileWidth = deviceWidth/TILE_X_COUNT;

    //List to keep track of enemies and a count of how many have been spawned
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private int enemyCount = 0;

    //Keeps track of the wave number
    private int waveNumber = 1;

    //Lists to keep track of towers and bullets
    private List<Tower> towerList = new ArrayList<Tower>();
    private List<Bullet> bulletList = new ArrayList<Bullet>();

    //Counts the number of times render has been called
    //Used to space out the spawning of enemies and bullets
    private int renderCount = 0;

    //Creating the Screen and rendering a test map
    public GameScreen(final PTD game) {
        this.game = game;

        //Size of the map
        int viewportWidth = TILE_X_COUNT * TILE_SIDE_LENGTH;
        int viewportHeight = TILE_Y_COUNT * TILE_SIDE_LENGTH;

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
                    bulletList.add(new Bullet(towerList.get(i).getDamage(), towerList.get(i).getTarget(), towerList.get(i)));
                }
            }
        }

        //Spawns a new enemy every 40 renders
        if (renderCount % 40 == 0) {
            //Wave size is 20 enemies so we stop once we get there
            if (enemyCount != 20) {
                double enemyToSpawn = Math.random() * 3;
                switch ((int) enemyToSpawn) {
                    case 0:
                        enemyList.add(new Enemy(Enemy.enemyType.ROCK, waveNumber));
                        break;
                    case 1:
                        enemyList.add(new Enemy(Enemy.enemyType.PAPER, waveNumber));
                        break;
                    case 2:
                        enemyList.add(new Enemy(Enemy.enemyType.SCISSORS, waveNumber));
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
            if (enemyList.get(i).getPosition().x > deviceWidth || enemyList.get(i).getPosition().x < 0 ||
                    enemyList.get(i).getPosition().y > deviceHeight || enemyList.get(i).getPosition().y < 0) {
                enemyList.remove(i);
            } else if (enemyList.get(i).getHealth() <= 0) {
                enemyList.get(i).setDestroyed(true);
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
            if (bulletList.get(i).getTarget().isDestroyed()) {
                bulletList.get(i).changeTarget();
            }

            //If our bullet is sitting at the start position we remove it
            if (bulletList.get(i).getPosition().x == bulletList.get(i).getTarget().getWaypointStart().x &&
                    bulletList.get(i).getPosition().y == bulletList.get(i).getTarget().getWaypointStart().y) {
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
        //Figuring our which spot on the grid the user tapped
        float cellX = screenX/tileWidth;
        float cellY = screenY/tileHeight;
        cellX = MathUtils.floor(cellX);
        cellY = MathUtils.floor(cellY);

        //Getting the cell the user tapped and checking if it is a tower tile
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        TiledMapTileLayer.Cell cell = layer.getCell((int) cellX, (int) (10 - 1 - cellY));
        Object property = cell.getTile().getProperties().get("TowerTile");

        //If it is a tower tile we spawn a new tower
        //TODO add popup to allow user to pick a tower type
        if(property != null) {
            double towerToSpawn = Math.random() * 3;
            switch ((int) towerToSpawn) {
                case 0:
                    towerList.add(new Tower(Tower.towerType.ROCK, cellX, cellY));
                    break;
                case 1:
                    towerList.add(new Tower(Tower.towerType.PAPER, cellX, cellY));
                    break;
                case 2:
                    towerList.add(new Tower(Tower.towerType.SCISSORS, cellX, cellY));
                    break;
            }
        }
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
