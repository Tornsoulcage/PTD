package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputProcessor;


/**
 * Description: Displays a game over screen once the user loses the game
 *
 * Created by Joseph A Scott on 3/15/2018.
 */

//Represents the Game Over Screen
public class WaveOverScreen implements Screen, InputProcessor {
    private final PTD game;
    private OrthographicCamera camera;

    //Setting the view to our map size
    public WaveOverScreen(final PTD game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 480, 320);
    }

    //Just draws a simple welcome screen
    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0,0,0.2f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        //Drawing our welcome messages
        game.font.draw(game.batch, "Wave Complete!", 100, 200);
        game.font.draw(game.batch, "Tap anywhere to play again.", 100, 100);

        game.batch.end();
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Changes the screen to the actual game upon taping
        game.setScreen(new GameScreen(game));
        dispose();
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
