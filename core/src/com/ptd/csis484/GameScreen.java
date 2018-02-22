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

    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    //Just a test enemy list and counter
    List<Enemy> enemyList = new ArrayList<Enemy>();
    int enemyCount = 0;
    int renderCount = 0;

    List<Tower> towerList = new ArrayList<Tower>();
    List<Bullet> bulletList = new ArrayList<Bullet>();

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
        Gdx.input.setInputProcessor(this);
    }

    //The "game loop"
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        //Drawing the enemies and updating them
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(int i = 0; i < towerList.size(); i++){
            towerList.get(i).update(enemyList, delta);
            towerList.get(i).render(shapeRenderer);
            if(renderCount % 30 == 0)
                bulletList.add(new Bullet(towerList.get(i).damage, towerList.get(i).target, towerList.get(i)));
        }

        for(int i = 0; i < bulletList.size(); i++){
            bulletList.get(i).update(delta);
            bulletList.get(i).render(shapeRenderer);
        }

        //Spawns a new enemy every 40 renders
        if(renderCount % 40 == 0) {
            //Wave size is 20 enemies so we stop once we get there
            if(enemyCount != 20) {
                enemyList.add(new Enemy(Enemy.enemyType.ROCK));
                enemyCount++;
            }
        }
        renderCount++;

        //Update loop for all of the enemies
        for(int i = 0; i < enemyList.size(); i++){
            enemyList.get(i).update(delta);
            enemyList.get(i).render(shapeRenderer);
        }
        shapeRenderer.end();
    }

    @Override
    public void show() {

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
