package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

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

    //We use a tile map because it makes more sense with placing towers
    private ShapeRenderer shapeRenderer;
    private Map gameMap = new Map();

    //List to keep track of enemies and a count of how many have been spawned
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private int enemyCount = 0;

    //Keeps track of the wave number
    private int waveNumber = 1;

    //Scaling for the towers in the current wave
    private int towerLevel = 1;
    private int towerLevelCost = 100;

    //Lists to keep track of towers and bullets
    private List<Tower> towerList = new ArrayList<Tower>();
    private List<Vector2> occupiedCells = new ArrayList<Vector2>();
    private List<Bullet> bulletList = new ArrayList<Bullet>();

    //Counts the number of times render has been called
    //Used to space out the spawning of enemies and bullets
    private int renderCount = 0;

    private int gold = 100;
    private int remainingLife = 20;

    //Creating the Screen and rendering a test map
    public GameScreen(final PTD game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, gameMap.getViewportWidth(), gameMap.getViewportHeight());
        camera.update();

        //Shape Renderer is how we draw the enemies
        shapeRenderer = new ShapeRenderer();
    }

    //The "game loop"
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        gameMap.render(game);
        game.batch.end();

        //Displaying the tiled map for the user
        camera.update();

        //Starting the shape renderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(gameMap.getLevelButton().getX(), gameMap.getLevelButton().getY(), gameMap.getLevelButton().getWidth(), gameMap.getLevelButton().getHeight());

        //Looping through the towers and updating and drawing them
        for (Tower tower : towerList) {
            tower.update(enemyList, delta);
            tower.render(shapeRenderer);

            //Every 30 renders wo spawn a new bullet, about 2 times a second
            if (renderCount % 30 == 0) {
                if (!enemyList.isEmpty()) {
                    bulletList.add(new Bullet(tower.getScaledDamage(), tower.getTarget(), tower));
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
        List<Enemy> toEnemyRemove = new ArrayList<Enemy>();
        for (Enemy enemy : enemyList) {
            //Updates the enemies with the current list of bullets
            enemy.update(delta, bulletList, gameMap);

            //If our enemy leaves the bounds of the screen we remove it from the game
            if (!gameMap.getGameBounds().contains(enemy.getBounds())) {
               toEnemyRemove.add(enemy);
               enemy.setDestroyed(true);
               remainingLife--;

               //If the user's life total has reached zero we end the game
               if(remainingLife <= 0){
                   game.setScreen(new GameOverScreen(game));
                   dispose();
               }
            } else if (enemy.getHealth() <= 0) {
                enemy.setDestroyed(true);
                gold += enemy.getGoldValue();
                toEnemyRemove.add(enemy);
            }

            enemy.render(shapeRenderer);

        }
        enemyList.removeAll(toEnemyRemove);

        //Update loop for all of the bullets
        List<Bullet> toBulletRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletList) {
            bullet.update(delta);

            //If our bullet is out of bounds
            if (!gameMap.getGameBounds().contains(bullet.getBounds())) {
                toBulletRemove.add(bullet);
            }

            bullet.render(shapeRenderer);

        }
        bulletList.removeAll(toBulletRemove);

        //If all of our enemies have been destroyed and we've reached the end of the wave we remove all of our bullets
        if (enemyList.isEmpty() && enemyCount == 20) {
            waveNumber++;
            enemyCount = 0;
        }

        //Incrementing the render count and ending the shape renderer
        renderCount++;
        shapeRenderer.end();

        //Message strings for the user
        String goldString = "Gold = " + gold;
        String waveString = "Wave = " + waveNumber;
        String lifeString = "Life = " + remainingLife;
        String levelString = "Lvl = " + towerLevel;
        String costString = "Cost = " + towerLevelCost;

        //Displaying the strings in the top left corner of the screen for the user
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, goldString, 0, gameMap.getViewportHeight());
        game.font.draw(game.batch, waveString, 0, gameMap.getViewportHeight() - (gameMap.getTILE_SIDE_LENGTH() - game.font.getXHeight()));
        game.font.draw(game.batch, lifeString, 0, gameMap.getViewportHeight() - 2*(gameMap.getTILE_SIDE_LENGTH() - game.font.getXHeight()));
        game.font.draw(game.batch, levelString, 3*gameMap.getTILE_SIDE_LENGTH(), gameMap.getViewportHeight() - gameMap.getTILE_SIDE_LENGTH() + 2*game.font.getXHeight());
        game.font.draw(game.batch, costString, 3*gameMap.getTILE_SIDE_LENGTH(), gameMap.getViewportHeight() - gameMap.getTILE_SIDE_LENGTH() - game.font.getXHeight());
        game.batch.end();
    }

    //Spawns a tower on the requested tile
    public void spawnTower(float cellX, float cellY){
        double towerToSpawn = Math.random() * 3;
        Tower tower = new Tower();
        switch ((int) towerToSpawn) {
            case 0:
                tower = new Tower(Tower.towerType.ROCK, cellX, cellY);
                break;
            case 1:
                tower = new Tower(Tower.towerType.PAPER, cellX, cellY);
                break;
            case 2:
                tower = new Tower(Tower.towerType.SCISSORS, cellX, cellY);
                break;
        }

        if(gold >= tower.getGoldCost()){
            gold -= tower.getGoldCost();
            towerList.add(tower);
            occupiedCells.add(new Vector2(cellX, cellY));
        }
    }

    //Changes the type of the tower in question
    public void changeTowerType(Tower tower){
        //We just rotate through the three tower types
        if(tower.getType() == Tower.towerType.ROCK){
            tower.setType(Tower.towerType.PAPER);
        } else if(tower.getType() == Tower.towerType.PAPER){
            tower.setType(Tower.towerType.SCISSORS);
        } else if(tower.getType() == Tower.towerType.SCISSORS){
            tower.setType(Tower.towerType.ROCK);
        }
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
        float cellX = screenX/gameMap.getTileWidth();
        float cellY = screenY/gameMap.getTileHeight();
        cellX = MathUtils.floor(cellX);
        cellY = MathUtils.floor(cellY);

        //Checking if the user taps the level up squares
        Rectangle touch = new Rectangle(screenX, gameMap.getDeviceHeight() - screenY, 1, 1);
        if(touch.overlaps(gameMap.getGameBounds())){
            if(gold >= towerLevelCost) {
                towerLevel++;
                gold -= towerLevelCost;
                towerLevelCost = (int) Math.floor(towerLevelCost * 1.05);
            }

            for(Tower tower : towerList){
                tower.setWaveTowerLevel(towerLevel);
            }
        }
/*
        //If it is a tower tile we check other traits
        if (cell.getTile().getId() == 1) {
            //Shows whether or not the cell in question is already occupied
            boolean occupiedCell = false;

            //Loops through all of our occupied cells to check occupancy
            for (Vector2 vector : occupiedCells) {
                if (vector.x == cellX && vector.y == cellY) {
                    occupiedCell = true;
                }
            }

            //If the cell is not occupied we spawn a new tower
            if (!occupiedCell) {
                spawnTower(cellX, cellY);
            }
*/
           boolean occupiedCell = true;
            //If the cell is occupied we change the tower type
            if(occupiedCell){
                for(Tower tower : towerList){
                    if(tower.getCellX() == cellX && tower.getCellY() == cellY){
                        //If it's within 5 seconds of a tower change or creation the change is free
                        if((System.currentTimeMillis() - tower.getTimeCreated()) < 5000){
                            changeTowerType(tower);
                            tower.setTimeCreated(System.currentTimeMillis());
                        } else if(gold >= tower.getSwitchCost()){
                            changeTowerType(tower);
                            gold -= tower.getSwitchCost();
                            tower.setTimeCreated(System.currentTimeMillis());
                        }
                        break;
                    }
                }
            }
        //}

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
