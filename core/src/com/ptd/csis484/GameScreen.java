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



/**
 * Created by scott on 2/18/2018.
 */

public class GameScreen implements Screen {
    final PTD game;
    OrthographicCamera camera;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    ShapeRenderer shapeRenderer;
    Enemy enemy1;

    public GameScreen(final PTD game) {
        this.game = game;
        int viewportWidth = 480;
        int viewportHeight = 320;
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        camera.update();

        tiledMap = new TmxMapLoader().load("PTDMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        enemy1 = new Enemy(Enemy.enemyType.ROCK);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        enemy1.update(delta);
        enemy1.render(shapeRenderer);
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
    }
}
