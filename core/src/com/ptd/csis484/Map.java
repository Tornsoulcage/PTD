package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Comparator;
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
    private Vector2 waypointStart = new Vector2();



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
                //P represents path tiles. The ending and starting tiles also get the path texture
                if (mapArray[y][x] == 'p' || mapArray[y][x] == 's' || mapArray[y][x] == 'e') {
                    Texture texture = manager.get("Tiles/grass_full.png");
                    game.batch.draw(texture, TILE_SIDE_LENGTH * x, TILE_SIDE_LENGTH * y);
                }
            }
        }
    }

    //Creates the array to represent each tile and it's attributes
    private void createMap(){
        mapArray[0] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 'p', 'p', 't', 't', 't'};
        mapArray[1] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 'p', 't', 'p', 'e'};
        mapArray[2] = new char[]{'x', 'x', 'x', 'x', 'x', 't', 't', 't', 't', 'p', 't', 'p', 't', 'p', 't'};
        mapArray[3] = new char[]{'t', 't', 't', 't', 't', 't', 'p', 'p', 'p', 'p', 't', 'p', 't', 'p', 't'};
        mapArray[4] = new char[]{'s', 'p', 'p', 'p', 'p', 'p', 'p', 't', 't', 't', 't', 'p', 't', 'p', 't'};
        mapArray[5] = new char[]{'t', 't', 't', 't', 't', 't', 't', 't', 'x', 'x', 't', 'p', 't', 'p', 't'};
        mapArray[6] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 'p', 't'};
        mapArray[7] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 'p', 't'};
        mapArray[8] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 'p', 'p', 't'};
        mapArray[9] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 't', 't', 't', 't'};

        //Once our map is made we call the function to set the waypoints
        setWaypoints();

    }

    //The idea here is to essentially walk the path through the array and mark each time we change
    //directions. Every time we change directions we mark that as a waypoint
    private void setWaypoints(){
        //Loops through the array to find the spots on the path where we change direction
        int xNeighbor;
        int yNeighbor;

        for(int y = 0; y < mapArray.length; y++) {
            for (int x = 0; x < mapArray[y].length; x++) {
                xNeighbor = 0;
                yNeighbor = 0;

                //If the index we are at is a path we check it's neighbors
                //If the spots above/below and right/left of it are also paths we increment the respective counter
                //For the purpose of neighbors we treat the end tile the same as a path
                //If our check would push us outside the bounds of the map we ignore it
                if (mapArray[y][x] == 'p') {
                    if(y-1 > 0) {
                        if (mapArray[y - 1][x] == 'p' || mapArray[y - 1][x] == 'e') {
                            yNeighbor++;
                        }
                    }
                    if(y+1 < mapArray.length -1) {
                        if (mapArray[y + 1][x] == 'p' || mapArray[y + 1][x] == 'e') {
                            yNeighbor++;
                        }
                    }
                    if(x-1 >0) {
                        if (mapArray[y][x - 1] == 'p' || mapArray[y][x - 1] == 'e') {
                            xNeighbor++;
                        }
                    }
                    if(x+1 < mapArray[y].length -1) {
                        if (mapArray[y][x + 1] == 'p' || mapArray[y][x + 1] == 'e') {
                            xNeighbor++;
                        }
                    }

                    //If we have one neighbor on x and one neighbor on y then this spot is a corner so we change direction here
                    if (xNeighbor == 1 && yNeighbor == 1) {
                        waypointBounds.add(new Rectangle(tileWidth * x + tileWidth / 3, tileHeight * y + tileHeight / 3, tileWidth / 3, tileHeight / 3));
                    }
                }

                //If our index is an s then that's our start tile
                if(mapArray[y][x] == 's'){
                    waypointStart.x = tileHeight * x;
                    waypointStart.y = tileHeight * y + tileHeight/3;
                    waypointBounds.add(new Rectangle(tileWidth * x + tileWidth / 3, tileHeight * y + tileHeight / 3, tileWidth / 3, tileHeight / 3));
                }

                //If our index is an e then that's our end tile
                //We add one to the x index to push the waypoint outside the game bounds, then
                //Then enemy will be removed while progressing towards that waypoint
                if(mapArray[y][x] == 'e'){
                    waypointBounds.add(new Rectangle(tileWidth * (x+1) + tileWidth / 3, tileHeight * y + tileHeight / 3, tileWidth / 3, tileHeight / 3));
                }
            }
        }
        boolean goAgain;
        do {
            goAgain = false;
            for (int i = 0; i < waypointBounds.size(); i++) {
                if (i != waypointBounds.size() - 1) {
                    if (waypointBounds.get(i).x > waypointBounds.get(i + 1).x) {
                        Rectangle temp = waypointBounds.get(i);
                        waypointBounds.set(i, waypointBounds.get(i + 1));
                        waypointBounds.set(i + 1, temp);
                        goAgain = true;
                    }
                }
            }
        } while (goAgain);
        Gdx.app.log("way", "" + waypointBounds);
        do {
            goAgain = false;
            for(int i = 0; i < waypointBounds.size(); i++){
                if (i != waypointBounds.size() - 1) {
                    if (i % 2 != 0) {
                        if (waypointBounds.get(i).y != waypointBounds.get(i - 1).y) {
                            Rectangle temp = waypointBounds.get(i);
                            waypointBounds.set(i, waypointBounds.get(i + 1));
                            waypointBounds.set(i + 1, temp);
                            goAgain = true;
                        }
                    }
                }
            }
        } while(goAgain);

    }

    //Getters and Setters for our variables
    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Vector2 getWaypointStart() {
        return waypointStart;
    }

    public void setWaypointStart(Vector2 waypointStart) {
        this.waypointStart = waypointStart;
    }

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
