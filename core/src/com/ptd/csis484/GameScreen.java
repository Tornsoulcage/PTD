package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by scott on 2/18/2018.
 */

//Represents the Game Screen. Where we actually play the game
public class GameScreen implements Screen {
    final PTD game;
    OrthographicCamera camera;

    //We use a tile map because it makes more sense with placing towers
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    ShapeRenderer shapeRenderer;

    //Just a test enemy list and counter
    List<Enemy> enemyList = new ArrayList<Enemy>();
    int enemyCount = 0;
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
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        //Drawing the enemies and updating them
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

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
}
