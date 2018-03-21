package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Rectangle;

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

    private TiledMap tiledMap;
    private TiledMapTileLayer tiledMapLayer;
    private TiledMapTileSet tiledMapTileSet;
    private AssetManager manager = new AssetManager();


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
    private Rectangle levelButton = new Rectangle();

    private char mapArray[][] = new char[TILE_Y_COUNT][];

    public Map(){
        createMap();
    }

    private void createMap(){
        mapArray[0] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 't', 't'};
        mapArray[1] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 'p', 'e'};
        mapArray[2] = new char[]{'x', 'x', 'x', 'x', 'x', 't', 't', 't', 't', 't', 't', 't', 't', 'p', 't'};
        mapArray[3] = new char[]{'x', 'x', 'x', 'x', 'x', 't', 'p', 'p', 'p', 'p', 'p', 't', 't', 'p', 't'};
        mapArray[4] = new char[]{'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 't', 't', 'p', 't', 't', 'p', 't'};
        mapArray[5] = new char[]{'x', 'x', 'x', 'x', 'x', 't', 'p', 't', 'x', 't', 'p', 't', 't', 'p', 't'};
        mapArray[6] = new char[]{'t', 't', 't', 't', 't', 't', 'p', 't', 'x', 't', 'p', 't', 't', 'p', 't'};
        mapArray[7] = new char[]{'s', 'p', 'p', 'p', 'p', 'p', 'p', 't', 'x', 't', 'p', 't', 't', 'p', 't'};
        mapArray[8] = new char[]{'t', 't', 't', 't', 't', 't', 't', 't', 'x', 't', 'p', 'p', 'p', 'p', 't'};
        mapArray[9] = new char[]{'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't', 't', 't', 't', 't', 't'};

        tiledMap = new TiledMap();
        tiledMapLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        tiledMapLayer = new TiledMapTileLayer(TILE_X_COUNT, TILE_Y_COUNT, TILE_SIDE_LENGTH, TILE_SIDE_LENGTH);
        manager.load("Tiles/grass0-dirt-mix1.png", Texture.class);
        manager.load("Tiles/grass_flowers_blue1.png", Texture.class);
        manager.load("Tiles/grass_full.png", Texture.class);



        for(int y = 0; y < mapArray.length; y++){
            for(int x = 0; x < mapArray[0].length; x++){
                if(mapArray[y][x] == 'x'){
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    Texture texture = manager.get("grass_flowers_blue1.png");
                    TextureRegion textureRegion = new TextureRegion(texture);
                    cell.setTile(new StaticTiledMapTile(textureRegion));
                    tiledMapLayer.setCell(x,y,cell);
                }
                if(mapArray[y][x] == 't'){
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    Texture texture = manager.get("grass_full.png");
                    TextureRegion textureRegion = new TextureRegion(texture);
                    cell.setTile(new StaticTiledMapTile(textureRegion));
                    cell.getTile().setId(1);
                    tiledMapLayer.setCell(x,y,cell);
                }
                if(mapArray[y][x] == 'p') {
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    Texture texture = manager.get("grass0-dirt-mix1.png");
                    TextureRegion textureRegion = new TextureRegion(texture);
                    cell.setTile(new StaticTiledMapTile(textureRegion));
                    tiledMapLayer.setCell(x, y, cell);
                }
            }
        }
    }
}
