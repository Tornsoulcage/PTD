package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


/**
 * Description: Represents the map the game is played on
 *
 * Created by scott on 3/16/2018.
 */

public class Map {
    //Stats on the tile sizes for the map
    private final int TILE_X_COUNT = 15;
    private final int TILE_Y_COUNT = 10;
    private final int TILE_SIDE_LENGTH = 32;

    private AssetManager manager = new AssetManager();

    private OrthographicCamera camera;

    //Size of the map
    private int viewportWidth = TILE_X_COUNT * TILE_SIDE_LENGTH;
    private int viewportHeight = TILE_Y_COUNT * TILE_SIDE_LENGTH;

    //Gets the dimensions of the tiles
    private float deviceHeight = Gdx.graphics.getHeight();
    private float deviceWidth = Gdx.graphics.getWidth();
    private float tileHeight = deviceHeight/TILE_Y_COUNT;
    private float tileWidth = deviceWidth/TILE_X_COUNT;

    //Bounds for map objects
    private Rectangle gameBounds = new Rectangle(0,0, deviceWidth, deviceHeight);
    private Rectangle levelButton = new Rectangle(3*tileWidth, 8*tileHeight, 2*tileWidth, 2*tileHeight);


    private char mapArray[][] = new char[TILE_Y_COUNT][];
    private List<Rectangle> waypointBounds = new ArrayList<Rectangle>();



    public Map(){


        createMap();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, this.getViewportWidth(), this.getViewportHeight());
        camera.update();
    }

    public void render(final PTD game){
        //Loading the assets to display the map
        manager.load("Tiles/grass0-dirt-mix1.png", Texture.class);
        manager.load("Tiles/grass_flowers_blue1.png", Texture.class);
        manager.load("Tiles/grass_full.png", Texture.class);

        manager.finishLoading();
        //Looping through the array and setting a cell with the appropriate texture
        for (int y = mapArray.length-1; y >= 0; y--) {
            for (int x = 0; x < mapArray[y].length; x++) {
                //X Represents background tiles
                if (mapArray[y][x] == 'x') {
                    Texture texture = manager.get("Tiles/grass_flowers_blue1.png");
                    game.batch.draw(texture, TILE_SIDE_LENGTH * x, TILE_SIDE_LENGTH * y);

                }
                //T represents tower tiles
                if (mapArray[y][x] == 't') {
                    Texture texture = manager.get("Tiles/grass0-dirt-mix1.png");
                    game.batch.draw(texture, TILE_SIDE_LENGTH * x, TILE_SIDE_LENGTH * y);
                }
                //P represents path tiles
                if (mapArray[y][x] == 'p') {
                    Texture texture = manager.get("Tiles/grass_full.png");
                    game.batch.draw(texture, TILE_SIDE_LENGTH * x, TILE_SIDE_LENGTH * y);
                }
            }
        }

    }

    //Creates the array to represent each tile and it's attributes
    private void createMap(){
        mapArray[0] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 't', 't'};
        mapArray[1] = new char[]{'t', 't', 't', 't', 't', 't', 't', 't', 'x', 'x', 'x', 'x', 't', 'p', 'e'};
        mapArray[2] = new char[]{'s', 'p', 'p', 'p', 'p', 'p', 'p', 't', 't', 't', 't', 't', 't', 'p', 't'};
        mapArray[3] = new char[]{'t', 't', 't', 't', 't', 't', 'p', 'p', 'p', 'p', 'p', 't', 't', 'p', 't'};
        mapArray[4] = new char[]{'x', 'x', 'x', 'x', 'x', 't', 't', 't', 't', 't', 'p', 't', 't', 'p', 't'};
        mapArray[5] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 't', 'p', 't'};
        mapArray[6] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 't', 'p', 't'};
        mapArray[7] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 't', 'p', 't'};
        mapArray[8] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 'p', 'p', 'p', 't'};
        mapArray[9] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 't', 't', 't', 't', 't'};

        List<Vector2>neighborPathTiles = new ArrayList<Vector2>();
        for(int y = 0; y < mapArray.length; y++){
            for(int x = 0; x < mapArray[y].length; x++){
                if(mapArray[y][x] == 'p'){
                    if(mapArray[y-1][x] == 'p'){
                        neighborPathTiles.add(new Vector2(x,y));
                    }
                    if(mapArray[y+1][x] == 'p'){
                        neighborPathTiles.add(new Vector2(x,y));
                    }
                    if(mapArray[y][x-1] == 'p'){
                        neighborPathTiles.add(new Vector2(x,y));
                    }
                    if(mapArray[y][x+1] == 'p'){
                        neighborPathTiles.add(new Vector2(x,y));
                    }
                    if(neighborPathTiles.size() == 2){
                        if(neighborPathTiles.get(0).x != neighborPathTiles.get(1).x && neighborPathTiles.get(0).y != neighborPathTiles.get(1).y) {
                            waypointBounds.add(new Rectangle(tileWidth * x + tileWidth / 3, tileHeight * y + tileHeight / 3, tileWidth / 3, tileHeight / 3));
                            neighborPathTiles = new ArrayList<Vector2>();
                        }
                    }
                }
            }
        }
    }

    //Getters and Setters for our variables


    public List<Rectangle> getWaypointBounds() {
        return waypointBounds;
    }

    public void setWaypointBounds(List<Rectangle> waypointBounds) {
        this.waypointBounds = waypointBounds;
    }

    public int getTILE_X_COUNT() {
        return TILE_X_COUNT;
    }

    public int getTILE_Y_COUNT() {
        return TILE_Y_COUNT;
    }

    public int getTILE_SIDE_LENGTH() {
        return TILE_SIDE_LENGTH;
    }

    public AssetManager getManager() {
        return manager;
    }

    public void setManager(AssetManager manager) {
        this.manager = manager;
    }

    public int getViewportWidth() {
        return viewportWidth;
    }

    public void setViewportWidth(int viewportWidth) {
        this.viewportWidth = viewportWidth;
    }

    public int getViewportHeight() {
        return viewportHeight;
    }

    public void setViewportHeight(int viewportHeight) {
        this.viewportHeight = viewportHeight;
    }

    public float getDeviceHeight() {
        return deviceHeight;
    }

    public void setDeviceHeight(float deviceHeight) {
        this.deviceHeight = deviceHeight;
    }

    public float getDeviceWidth() {
        return deviceWidth;
    }

    public void setDeviceWidth(float deviceWidth) {
        this.deviceWidth = deviceWidth;
    }

    public float getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(float tileHeight) {
        this.tileHeight = tileHeight;
    }

    public float getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(float tileWidth) {
        this.tileWidth = tileWidth;
    }

    public Rectangle getGameBounds() {
        return gameBounds;
    }

    public void setGameBounds(Rectangle gameBounds) {
        this.gameBounds = gameBounds;
    }

    public Rectangle getLevelButton() {
        return levelButton;
    }

    public void setLevelButton(Rectangle levelButton) {
        this.levelButton = levelButton;
    }

    public char[][] getMapArray() {
        return mapArray;
    }

    public void setMapArray(char[][] mapArray) {
        this.mapArray = mapArray;
    }
}
