package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

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

    //Time in milliseconds that the last enemy was spawned
    private long enemySpawnedTime = 0;

    //Keeps track of the wave number
    private int waveNumber = 1;

    //Counts to keep track of the towers
    private int rockCount = 0;
    private int paperCount = 0;
    private int scissorsCount = 0;

    //Lists to keep track of towers and bullets
    private List<Tower> towerList = new ArrayList<Tower>();
    private List<Vector2> occupiedCells = new ArrayList<Vector2>();
    private List<Bullet> bulletList = new ArrayList<Bullet>();
    private boolean occupiedCell;

    //Stats to keep track of the player's information
    private int gold = 100;
    private int remainingLife = 20;

    //Used to track whether or not the user is doing a long press
    private long touchTime;

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

        //These functions spawn and update the necessary elements for the game
        spawnEnemy();
        updateTowers();
        updateEnemies();
        updateBullets();

        //If all of our enemies have been destroyed and we've reached the end of the wave so
        //we reset our enemy count and restart
        if (enemyList.isEmpty() && enemyCount == 20) {
            waveNumber++;
            enemyCount = 0;

            //If we get to wave 15 we start a new map
            if(waveNumber > 15){
                game.setScreen(new WaveOverScreen(game));
                dispose();
            }
        }

        shapeRenderer.end();

        //Message strings for the user
        String goldString = "Gold = " + gold;
        String waveString = "Wave = " + waveNumber;
        String lifeString = "Life = " + remainingLife;

        //Displaying the strings in the top left corner of the screen for the user
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, goldString, 0, gameMap.getViewportHeight());
        game.font.draw(game.batch, waveString, 0, gameMap.getViewportHeight() - (gameMap.getTILE_SIDE_LENGTH() - game.font.getXHeight()));
        game.font.draw(game.batch, lifeString, 0, gameMap.getViewportHeight() - 2*(gameMap.getTILE_SIDE_LENGTH() - game.font.getXHeight()));
        game.batch.end();
    }

    //Function to spawn a new enemy
    private void spawnEnemy(){
        //Spawns a new enemy every second
        if (System.currentTimeMillis() - enemySpawnedTime >= 750) {
            //If we haven't reached the max number of enemies for the wave yet
            if (enemyCount != 20) {
                //Setting the default rate each enemy is spawned
                double rockRate = .33;
                double paperRate = .66;
                double scissorsRate = 1;

                //If the player is favoring a certain tower we make it's opposing enemy type more
                //likely to spawn.
                if (rockCount > paperCount && rockCount > scissorsCount) {
                    rockRate = .25;
                    paperRate = .75;
                    scissorsRate = 1;
                } else if(paperCount > rockCount && paperCount > scissorsCount){
                    rockRate = .25;
                    paperRate = .50;
                    scissorsRate = 1;
                } else if(scissorsCount > rockCount && scissorsCount > paperCount){
                    rockRate = .50;
                    paperRate = .75;
                    scissorsRate = 1;
                }

                //Rolling a random number to decide which enemy to spawn
                double enemyToSpawn = Math.random();
                if (enemyToSpawn <= rockRate) {
                    enemyList.add(new Enemy("ROCK", waveNumber, gameMap));

                    //Incrementing our count and changing the last spawned time
                    enemyCount++;
                    enemySpawnedTime = System.currentTimeMillis();
                }
                if (enemyToSpawn > rockRate && enemyToSpawn <= paperRate) {
                    enemyList.add(new Enemy("PAPER", waveNumber, gameMap));

                    //Incrementing our count and changing the last spawned time
                    enemyCount++;
                    enemySpawnedTime = System.currentTimeMillis();
                }
                if (enemyToSpawn > paperRate && enemyToSpawn <= scissorsRate) {
                    enemyList.add(new Enemy("SCISSORS", waveNumber, gameMap));

                    //Incrementing our count and changing the last spawned time
                    enemyCount++;
                    enemySpawnedTime = System.currentTimeMillis();
                }
            }
        }
    }

    //Function to update the enemies
    private void updateEnemies(){
        //Update loop for all of the enemies
        List<Enemy> toEnemyRemove = new ArrayList<Enemy>();
        for (Enemy enemy : enemyList) {
            //Updates the enemies with the current list of bullets
            enemy.update(bulletList, gameMap);

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
    }

    //Function to update the towers
    private void updateTowers(){
        //Looping through the towers and updating and drawing them
        for (Tower tower : towerList) {
            tower.update(enemyList);
            tower.render(shapeRenderer);

            //Spawns a new bullet every half second.
            if (System.currentTimeMillis() - tower.getBulletFiredTime() >= 500) {
                if (!enemyList.isEmpty()) {
                    if(tower.getTarget().getType().equals(tower.getType())) {
                        bulletList.add(new Bullet(tower.getTarget(), tower));
                        tower.setBulletFiredTime(System.currentTimeMillis());
                    }
                }
            }
        }
    }

    //Function to update the bullets
    private void updateBullets(){
        //Update loop for all of the bullets
        List<Bullet> toBulletRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletList) {
            bullet.update();

            //If our bullet is out of bounds
            if (!gameMap.getGameBounds().contains(bullet.getBounds())) {
                toBulletRemove.add(bullet);
            }

            bullet.render(shapeRenderer);

        }
        bulletList.removeAll(toBulletRemove);
    }

    //Spawns a tower on the requested tile
    private void spawnTower(float cellX, float cellY){
        //Pick a random number to pick which type of tower to spawn
        double towerToSpawn = Math.random() * 3;
        Tower tower = new Tower();
        switch ((int) towerToSpawn) {
            case 0:
                tower = new Tower("ROCK", cellX, cellY, gameMap);
                rockCount++;
                break;
            case 1:
                tower = new Tower("PAPER", cellX, cellY, gameMap);
                paperCount++;
                break;
            case 2:
                tower = new Tower("SCISSORS", cellX, cellY, gameMap);
                scissorsCount++;
                break;
        }

        //If the user has enough gold we can spawn a new tower
        if(gold >= tower.getGoldCost()){
            gold -= tower.getGoldCost();
            towerList.add(tower);
            occupiedCells.add(new Vector2(cellX, cellY));
        }
    }

    //Changes the type of the tower in question
    private void changeTowerType(Tower tower){
        //We just rotate through the three tower types
        if(tower.getType().equals("ROCK")){
            tower.setType("PAPER");
            rockCount--;
            paperCount++;
        } else if(tower.getType().equals("PAPER")){
            tower.setType("SCISSORS");
            paperCount--;
            scissorsCount++;
        } else if(tower.getType().equals("SCISSORS")){
            tower.setType("ROCK");
            rockCount--;
            scissorsCount++;
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
        //We register the time the user tap to see if they are doing a long press
        touchTime = System.currentTimeMillis();

        //Figuring our which spot on the grid the user tapped
        float cellX = screenX/gameMap.getTileWidth();
        float cellY = screenY/gameMap.getTileHeight();
        cellX = MathUtils.floor(cellX);
        cellY = MathUtils.floor(cellY);

        //If it is a tower tile we check other traits
        if((gameMap.getMapArray()[(int)(gameMap.getTILE_Y_COUNT() - 1 - cellY)][(int)cellX]) == 't'){
            //Shows whether or not the cell in question is already occupied
            occupiedCell = false;

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
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Figuring our which spot on the grid the user tapped
        float cellX = screenX / gameMap.getTileWidth();
        float cellY = screenY / gameMap.getTileHeight();
        cellX = MathUtils.floor(cellX);
        cellY = MathUtils.floor(cellY);

        //If the cell is occupied we can either level up or change the tower type
        if (occupiedCell) {
            //500 milliseconds is the amount for a long press
            if (System.currentTimeMillis() - touchTime > 500) {
                //If the cell is occupied we change the tower type
                for (Tower tower : towerList) {
                    if (tower.getCellX() == cellX && tower.getCellY() == cellY) {
                        if (tower.getTowerLevel() < 5) {
                            //If the user has enough gold to level up the tower than we do so
                            //and scale the attributes for leveling
                            if (gold >= tower.getTowerLevelCost()) {
                                gold -= tower.getTowerLevelCost();
                                tower.setTowerLevelCost(tower.getTowerLevelCost() * 1.05);
                                tower.setTowerLevel(tower.getTowerLevel() + 1);
                            }
                        }
                        //If we find the right tower then we quit looping through the list
                        break;
                    }
                }
            } else {
                for (Tower tower : towerList) {
                    //If this tower is in the same cell as we want
                    if (tower.getCellX() == cellX && tower.getCellY() == cellY) {
                        //If it's within 5 seconds of a tower change or creation the change is free
                        if ((System.currentTimeMillis() - tower.getTimeCreated()) < 5000) {
                            changeTowerType(tower);
                            tower.setTimeCreated(System.currentTimeMillis());
                        } else if (gold >= tower.getSwitchCost()) {
                            changeTowerType(tower);
                            gold -= tower.getSwitchCost();
                            tower.setTimeCreated(System.currentTimeMillis());
                        }
                        //If we find the right tower then we finish looping through the list
                        break;
                    }
                }
            }
        }
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
