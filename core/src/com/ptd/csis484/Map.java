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
    private final int TOTAL_PATH_COUNT = 18;
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

    //Used to keep track of the map and where enemies should go
    private char mapArray[][] = new char[TILE_Y_COUNT][];
    private List<Rectangle> waypointBounds = new ArrayList<Rectangle>();
    private Vector2 waypointStart = new Vector2();
    private Vector2 waypointEnd = new Vector2();



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
        //First we finish creating the array
        for(int i = 0; i < mapArray.length; i++){
            mapArray[i] = new char[TILE_X_COUNT];
        }

        //Pick a random starting spot
        //We exclude the first 3 tiles of the map to make space for user information to be displayed
        int start = (int) (Math.random()*(TILE_Y_COUNT - 4));
        mapArray[start][0] = 's';

        //Setting our current position the distance variable
        int currentY = start;
        int currentX = 0;
        int distance;

        //Setting the info for the starting waypoint
        waypointStart.x = tileWidth * currentX;
        waypointStart.y = tileHeight * currentY + tileHeight/3;

        //Booleans to help control the loop
        boolean finished = false;
        boolean firstRoll = true;

        do {
            //Our map length is 15 tiles. We also need a minimum of two tiles for every path,
            //Which prevents paths from being next to each, there will always be a space between them.
            if(firstRoll){
                //The first distance has to go beyond the user info so push it to be above 3
                distance = (int) (Math.random() * (TILE_X_COUNT - 4));
                distance += 3;
                firstRoll = false;
            } else {
                //Otherwise we roll a minimum of two up to the tile limit
                distance = (int) (Math.random() * ((TILE_X_COUNT - 2) - currentX) + 2);
            }

            //First we loop through the x axis until we place a number of spots equal to the distance
            for(int i = 1; i <= distance; i++){
                //If we reach the end of the axis we want to place the end tile and end the loop
                if(currentX + i == TILE_X_COUNT - 1){
                    mapArray[currentY][currentX + i] = 'e';
                    finished = true;
                    break;
                } else {
                    mapArray[currentY][currentX + i] = 'p';
                }
            }

            //Then we add that distance to our x to shift our position to where we ended
            currentX += (distance);

            //We roll a random number to decide wether we will go up or down
            double direction = Math.random();

            //If we reach the end tile we don't want to go up or down
            if(!finished) {
                //These prevent us from picking down or up when we on the boundary of the map
                if(currentY == 0){
                    direction = .2;
                }
                if(currentY == 9){
                    direction = .7;
                }

                //Less than .5 is down
                if (direction < .5) {
                    //Same as the xaxis but with a smaller length for the y axis
                    distance = (int) (Math.random() * (TILE_Y_COUNT - currentY));

                    //Looping through the y axis to place a number of tiles equal to our distance
                    for (int i = 1; i <= distance; i++) {
                        mapArray[currentY + i][currentX] = 'p';
                    }

                    //Then we add that distance to our y count to shift our position
                    currentY += (distance);

                //Otherwise we go up
                } else {
                    //Same as the xaxis but with a smaller length for the y axis
                    //Going up means that we can place a tile anywhere from 0 to our current spot
                    distance = (int) (Math.random() * (currentY));

                    //Looping through the y axis to place a number of tiles equal to our distance
                    for (int i = 1; i <= distance; i++) {
                        mapArray[currentY - i][currentX] = 'p';
                    }

                    //Then we substract our distance to shift us to our new position
                    currentY -= distance;
                }
            }
        } while (!finished);

        //Loop to set our tower tiles
        for(int y = 0; y < mapArray.length; y++) {
            for (int x = 0; x < mapArray[y].length; x++) {
                //If we are on a path tile we check the 8 tiles around it, make sure they are within
                //the bounds of the map and if they are not a path tile we set it to a tower tile
                if (mapArray[y][x] == 's' || mapArray[y][x] == 'p' || mapArray[y][x] == 'e') {
                    if(y < TILE_Y_COUNT - 1){
                        if(mapArray[y+1][x] != 'p' && mapArray[y+1][x] != 'e' && mapArray[y+1][x] != 's'){
                            mapArray[y+1][x] = 't';
                        }
                        if(x < TILE_X_COUNT - 1){
                            if(mapArray[y+1][x+1] != 'p' && mapArray[y+1][x+1] != 'e' && mapArray[y+1][x+1] != 's'){
                                mapArray[y+1][x+1] = 't';
                            }
                        }
                        if(x > 0){
                            if(mapArray[y+1][x-1] != 'p' && mapArray[y+1][x-1] != 'e' && mapArray[y+1][x-1] != 's'){
                                mapArray[y+1][x-1] = 't';
                            }
                        }
                    }
                    if(y > 0) {
                        if (mapArray[y - 1][x] != 'p' && mapArray[y - 1][x] != 'e' && mapArray[y - 1][x] != 's') {
                            mapArray[y - 1][x] = 't';
                        }
                        if (x < TILE_X_COUNT - 1) {
                            if (mapArray[y - 1][x + 1] != 'p' && mapArray[y - 1][x + 1] != 'e' && mapArray[y - 1][x + 1] != 's') {
                                mapArray[y - 1][x + 1] = 't';
                            }
                        }
                        if (x > 0) {
                            if (mapArray[y - 1][x - 1] != 'p' && mapArray[y - 1][x - 1] != 'e' && mapArray[y - 1][x - 1] != 's') {
                                mapArray[y - 1][x - 1] = 't';
                            }
                        }
                    }
                }
            }
        }

        //Loop to set our background tiles
        for(int y = 0; y < mapArray.length; y++){
            for(int x = 0; x < mapArray[y].length; x++){
                //If the spot isn't already assigned a tile type we set it to be a background tile
                if(mapArray[y][x] != 's' && mapArray[y][x] != 'e' && mapArray[y][x] != 't' && mapArray[y][x] != 'p'){
                    mapArray[y][x] = 'x';
                }
            }
        }

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
                    if(y-1 >= 0) {
                        if (mapArray[y - 1][x] == 'p' || mapArray[y - 1][x] == 'e') {
                            yNeighbor++;
                        }
                    }
                    if(y+1 <= mapArray.length -1) {
                        if (mapArray[y + 1][x] == 'p' || mapArray[y + 1][x] == 'e') {
                            yNeighbor++;
                        }
                    }
                    if(x-1 >= 0) {
                        if (mapArray[y][x - 1] == 'p' || mapArray[y][x - 1] == 'e') {
                            xNeighbor++;
                        }
                    }
                    if(x+1 <= mapArray[y].length -1) {
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
                //We also a waypoint at the actual end tile to line the enemy up with the exit
                if(mapArray[y][x] == 'e'){
                    waypointEnd.x = tileWidth * (x+1) + tileWidth / 3;
                    waypointEnd.y = tileHeight * y + tileHeight / 3;
                    waypointBounds.add(new Rectangle(tileWidth * (x) + tileWidth / 3, tileHeight * y + tileHeight / 3, tileWidth / 3, tileHeight / 3 ));
                }
            }
        }

        //These loops sort the waypoints so they fall in the right order
        boolean goAgain;
        do {
            goAgain = false;

            //First we sort by the x location. We assume a left to right movement so sort ascending
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

        do {
            goAgain = false;

            //Every other waypoint changes our direction to up or down but since they are on the
            //same x spot we need to make sure the right one comes first.
            //We do this by making sure this waypoint's y is the same as the previous one, i.e.
            //They are in line with eachother. If they are not then we swap this waypoint with the one
            //in front of it, which will align it properly.
            for(int i = 0; i < waypointBounds.size(); i++){
                if (i % 2 != 0) {
                    if (waypointBounds.get(i).y != waypointBounds.get(i - 1).y) {
                        Rectangle temp = waypointBounds.get(i);
                        waypointBounds.set(i, waypointBounds.get(i + 1));
                        waypointBounds.set(i + 1, temp);
                        goAgain = true;
                    }
                }
            }
        } while(goAgain);

        //After the waypoints are sorted we add in the final waypoint, the exit one.
        waypointBounds.add(new Rectangle(waypointEnd.x, waypointEnd.y, tileWidth / 3, tileHeight / 3));
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
