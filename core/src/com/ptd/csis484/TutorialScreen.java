package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


/**
 * Description: Displays a main menu screen so the user isnt thrown
 * into the game upon launching.
 *
 * Created by Joseph A Scott on 2/18/2018.
 */

//Represents the Main Menu/Start Screen
public class TutorialScreen implements Screen, InputProcessor {
    private final PTD game;

    private OrthographicCamera camera;
    private int viewportWidth = 480;
    private int viewportHeight = 320;

    private float deviceHeight = Gdx.graphics.getHeight();
    private float deviceWidth = Gdx.graphics.getWidth();

    private ShapeRenderer shapeRenderer;
    private GlyphLayout glyphLayout;
    
    private float scaleHPercent = viewportHeight/deviceHeight;
    private float scaleWPercent = viewportWidth/deviceWidth;

    //Represents the tutorial screen
    public TutorialScreen(final PTD game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportWidth, viewportHeight);
        glyphLayout = new GlyphLayout();
    }

    //Just draws a simple tutorial screen
    @Override
    public void render(float delta){
        //Setting the background to a dark blue color
        Gdx.gl.glClearColor(0,0,0.4f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //We use SCISSORS Enemy as our default text because it's the longest string we have
        //We use this to get the length of the string, which is used while placing the strings and
        //tile shapes
        glyphLayout.setText(game.font, "SCISSORS Enemy");

        shapeRenderer = new ShapeRenderer();
        camera.update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //Drawing the various squares to represent the enemies and towers we use
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(45/scaleWPercent, (viewportHeight - 14*game.font.getLineHeight() + 9)/scaleHPercent, 30,30);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(45/scaleWPercent,  (viewportHeight - 13*game.font.getLineHeight() + 9)/scaleHPercent, 30,30);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(45/scaleWPercent, (viewportHeight - 12*game.font.getLineHeight() + 9)/scaleHPercent, 30,30);

        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect((105 + glyphLayout.width)/scaleWPercent, (viewportHeight - 12*game.font.getLineHeight() + 9)/scaleHPercent, 30,30);

        shapeRenderer.setColor(Color.LIME);
        shapeRenderer.rect((105 + glyphLayout.width)/scaleWPercent,  (viewportHeight - 13*game.font.getLineHeight() + 9)/scaleHPercent, 30,30);

        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect((105 + glyphLayout.width)/scaleWPercent, (viewportHeight - 14*game.font.getLineHeight() + 9)/scaleHPercent, 30,30);

        shapeRenderer.setColor(Color.valueOf("#91fc3a"));
        shapeRenderer.circle(49/scaleWPercent, (viewportHeight - 15*game.font.getLineHeight() + 12)/scaleHPercent, 15);

        shapeRenderer.end();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        //Drawing our tutorial messages
        game.font.draw(game.batch, "Welcome to PTD.", 0, viewportHeight);
        game.font.draw(game.batch, "PTD is a tower defense game with the same basic goals of the genre.", 0, viewportHeight - 2*game.font.getLineHeight());
        game.font.draw(game.batch, "Enemies will spawn at random and move towards the end of the path.", 0, viewportHeight - 3*game.font.getLineHeight());
        game.font.draw(game.batch, "The players goal is to spawn towers on the proper tiles,", 0, viewportHeight - 4*game.font.getLineHeight());
        game.font.draw(game.batch, "the towers will then target and shoot enemies to destroy them.", 0, viewportHeight - 5*game.font.getLineHeight());
        game.font.draw(game.batch, "To spawn a tower the player should tap on a tower tile.", 0, viewportHeight - 6*game.font.getLineHeight());
        game.font.draw(game.batch, "To change a tower's type the user should tap a tower.", 0, viewportHeight - 7*game.font.getLineHeight());
        game.font.draw(game.batch, "Type changes are free within 5 seconds of creating or changing types.", 0, viewportHeight - 8*game.font.getLineHeight());
        game.font.draw(game.batch, "Damage is done on a ROCK-PAPER-SCISSORS basis.", 0, viewportHeight - 9*game.font.getLineHeight());
        game.font.draw(game.batch, "The tower color and enemy color will correspond.", 0, viewportHeight - 10*game.font.getLineHeight());

        game.font.draw(game.batch, "ROCK Enemy", 60, viewportHeight - 11*game.font.getLineHeight());
        game.font.draw(game.batch, "PAPER Enemy", 60, viewportHeight - 12*game.font.getLineHeight());
        game.font.draw(game.batch, "SCISSORS Enemy", 60, viewportHeight - 13*game.font.getLineHeight());
        game.font.draw(game.batch, "PATH Tile", 120 + glyphLayout.width, viewportHeight - 11*game.font.getLineHeight());
        game.font.draw(game.batch, "TOWER Tile", 120 + glyphLayout.width, viewportHeight - 12*game.font.getLineHeight());
        game.font.draw(game.batch, "BACKGROUND Tile", 120 + glyphLayout.width, viewportHeight - 13*game.font.getLineHeight());
        game.font.draw(game.batch, "BULLET", 60, viewportHeight - 14*game.font.getLineHeight());
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
