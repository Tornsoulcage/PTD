package com.ptd.csis484;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Description: Represents the tower objects
 *
 * Created by Joseph A Scott on 2/22/2018.
 */

//Represents the tower objects
public class Tower {
    //Used to track the tower's current level and cost to upgrade
    private int towerLevel;
    private double towerLevelCost;

    //Damage each bullet does
    private double baseDamage;
    private double scaledDamage;

    //Variables associated with creating this tower
    private int goldCost;
    private int switchCost;

    //Variables to keep track of the tower's position on the map
    private float cellX = 0;
    private float cellY = 0;
    private float tileWidth;
    private float tileHeight;

    //Used to time events associated with the tower
    private long timeCreated;
    private long bulletFiredTime;

    //Sets a default enemy to be the target
    private Enemy target = new Enemy();

    //Variables to help find the closet enemy
    private double targetDist;

    //This towers type, used to determine which enemies it can hit and it's stats
    private String type;

    //Towers position on map
    private Vector2 position;

    //Default Constructor
    public Tower() {
        this.position = new Vector2(0,0);
        this.type = "NO_TOWER_TYPE";
    }

    //Main constructor, accepts the desired type and it's location on the map
    public Tower(String type, float cellX, float cellY, Map gameMap){
        this.type = type;
        this.timeCreated = System.currentTimeMillis();

        //Recording the cell the tower resides in
        this.cellX = cellX;
        this.cellY = cellY;

        //Getting the tile width/height. Used for scaling the size of the tower
        this.tileHeight = gameMap.getTileHeight();
        this.tileWidth = gameMap.getTileWidth();

        //Formula's to convert cellX and cellY to the x/y position on the map
        //Places the pointer in the center of the tile
        float positionX = cellX*tileWidth + tileWidth/2;
        float positionY = gameMap.getDeviceHeight() - 1 - (cellY*tileHeight) - tileHeight/2;

        //We use the device width because it's the longest distance feasible for the map
        targetDist = gameMap.getDeviceWidth();

        //Setting the costs for all towers
        goldCost = 30;
        switchCost = 10;

        //Setting the position of the tower
        this.position = new Vector2(positionX, positionY);

        //Used to help scale the towers
        towerLevel = 0;
        towerLevelCost = 75;

        //Depending on the tower type we change it's variables
        this.baseDamage = 2;
        this.scaledDamage = baseDamage;
    }

    //Draws a different colored square depending on the tower type
    public void render(ShapeRenderer renderer){
        //Used to help scale the size of the tower
        float towerHeight = (float) (tileHeight * (.25 + .1*towerLevel));
        float towerWidth = (float) (tileWidth * (.25 + .1*towerLevel));
        Vector2 adjustedPosition = new Vector2(position.x - towerWidth/2, position.y - towerHeight/2);

        if(type.equals("ROCK")){
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(adjustedPosition.x, adjustedPosition.y, towerWidth,towerHeight);
        }
        if(type.equals("PAPER")){
            renderer.setColor(Color.BLUE);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(adjustedPosition.x, adjustedPosition.y, towerWidth,towerHeight);
        }
        if(type.equals("SCISSORS")){
            renderer.setColor(Color.RED);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(adjustedPosition.x, adjustedPosition.y, towerWidth,towerHeight);
        }
    }

    //Updates the tower to get a new target
    public void update(List<Enemy> enemyList) {
        //Variable to hold temp distance for each enemy
        double newTargetDist;

        //Only runs if there is a enemy
        if (!enemyList.isEmpty()) {
            //Loops through the enemy list
            for (Enemy enemy : enemyList) {
                //If our target has been destroyed we switch to the default target
                if(target.isDestroyed()){
                    this.target = new Enemy();
                }

                //Towers will only shoot at enemies that share their type
                if(enemy.getType().equals(this.type)) {
                    //Gets the current distance to the target
                    double targetXDist = Math.abs((position.x - target.getPosition().x));
                    double targetYDist = Math.abs((position.y - target.getPosition().y));
                    targetDist = Math.sqrt((targetXDist * targetXDist + targetYDist * targetYDist));

                    //Gets the distance to the potential target
                    targetXDist = Math.abs((position.x - enemy.getPosition().x));
                    targetYDist = Math.abs((position.y - enemy.getPosition().y));
                    newTargetDist = Math.sqrt((targetXDist * targetXDist + targetYDist * targetYDist));

                    //If potential is less than actual we change targets
                    if (newTargetDist < targetDist) {
                        target = enemy;
                    }
                }
            }
        } else {
            this.target = new Enemy();
        }
    }

    public int getTowerLevel() {
        return towerLevel;
    }

    public void setTowerLevel(int towerLevel) {
        this.towerLevel = towerLevel;
    }

    public double getTowerLevelCost() {
        return towerLevelCost;
    }

    public void setTowerLevelCost(double towerLevelCost) {
        this.towerLevelCost = towerLevelCost;
    }

    public double getScaledDamage(){
        scaledDamage = scaledDamage * (1 + .20 * towerLevel);
        return scaledDamage;
    }

    public Enemy getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getCellX() {
        return cellX;
    }

    public float getCellY() {
        return cellY;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public int getSwitchCost() {
        return switchCost;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getBulletFiredTime() {
        return bulletFiredTime;
    }

    public void setBulletFiredTime(long bulletFiredTime) {
        this.bulletFiredTime = bulletFiredTime;
    }
}
