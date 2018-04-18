package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
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
    private int towerLevel = 0;
    private double towerLevelCost = 30;

    //Damage each bullet does
    private int baseDamage;
    private double scaledDamage;

    //Variables associated with creating this tower
    private int goldCost;
    private int upgradeCost;
    private int switchCost;

    private float cellX = 0;
    private float cellY = 0;
    private float tileWidth;
    private float tileHeight;

    private long timeCreated;
    private long bulletFiredTime;

    //Sets a default enemy to be the target
    private Enemy target = new Enemy();

    //Variables to help find the closet enemy
    private double targetXDist;
    private double targetYDist;
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

    //Main constructor, passes the desired type and it's location on the map
    public Tower(String type, float cellX, float cellY, Map gameMap){
        this.type = type;
        this.timeCreated = System.currentTimeMillis();

        //Recording the cell the tower resides in
        this.cellX = cellX;
        this.cellY = cellY;
        this.tileHeight = gameMap.getTileHeight();
        this.tileWidth = gameMap.getTileWidth();

        //Formula's to convert cellX and cellY to the x/y position on the map
        //Places the pointer in the center of the tile
        float positionX = cellX*tileWidth + tileWidth/2;
        float positionY = gameMap.getDeviceHeight() - 1 - (cellY*tileHeight) - tileHeight/2;

        targetDist = gameMap.getDeviceHeight();

        //Setting the costs for all towers
        goldCost = 30;
        upgradeCost = 15;
        switchCost = 10;

        //Setting the position of the tower
        this.position = new Vector2(positionX, positionY);

        //Depending on the tower type we change it's variables
        this.baseDamage = 2;
        this.scaledDamage = 2;
    }

    //Just draws a red rectangle to represent the tower
    public void render(ShapeRenderer renderer){
        float towerHeight = (float) (tileHeight * (.25 + .1*towerLevel));
        float towerWidth = (float) (tileWidth * (.25 + .1*towerLevel));
        Vector2 adjustedPosition = new Vector2(position.x - towerWidth/2, position.y - towerHeight/2);

        if(type == "ROCK"){
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(adjustedPosition.x, adjustedPosition.y, towerWidth,towerHeight);
        }
        if(type == "PAPER"){
            renderer.setColor(Color.BLUE);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(adjustedPosition.x, adjustedPosition.y, towerWidth,towerHeight);
        }
        if(type == "SCISSORS"){
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

                if(enemy.getType().equals(this.type)) {
                    //Gets the current distance to the target
                    targetXDist = Math.abs((position.x - target.getPosition().x));
                    targetYDist = Math.abs((position.y - target.getPosition().y));
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

    public void setScaledDamage(double scaledDamage) {
        this.scaledDamage = scaledDamage;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public double getScaledDamage(){
        scaledDamage = scaledDamage * (1 + .5 * towerLevel);
        return scaledDamage;
    }

    public void setScaledDamage(int scaledDamage) {
        this.scaledDamage = scaledDamage;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public double getTargetXDist() {
        return targetXDist;
    }

    public void setTargetXDist(double targetXDist) {
        this.targetXDist = targetXDist;
    }

    public double getTargetYDist() {
        return targetYDist;
    }

    public void setTargetYDist(double targetYDist) {
        this.targetYDist = targetYDist;
    }

    public double getTargetDist() {
        return targetDist;
    }

    public void setTargetDist(double targetDist) {
        this.targetDist = targetDist;
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

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getCellX() {
        return cellX;
    }

    public void setCellX(float cellX) {
        this.cellX = cellX;
    }

    public float getCellY() {
        return cellY;
    }

    public void setCellY(float cellY) {
        this.cellY = cellY;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public int getSwitchCost() {
        return switchCost;
    }

    public void setSwitchCost(int switchCost) {
        this.switchCost = switchCost;
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
