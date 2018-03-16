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
    //Damage each bullet does
    private int baseDamage;
    private int scaledDamage;

    //Variables used to help calculate position
    private float deviceHeight = Gdx.graphics.getHeight();
    private float deviceWidth = Gdx.graphics.getWidth();
    private float tileHeight = deviceHeight/10;
    private float tileWidth = deviceWidth/15;
    private float cellX = 0;
    private float cellY = 0;

    //Variables associated with creating this tower
    private int goldCost;
    private int upgradeCost;
    private int switchCost;

    private int towerLevel;
    private long timeCreated;

    private int waveTowerLevel = 1;

    //Sets a default enemy to be the target
    private Enemy target = new Enemy(Enemy.enemyType.ROCK, 1);

    //Variables to help find the closet enemy
    private double targetXDist;
    private double targetYDist;
    private double targetDist;

    //Only three types of towers are allowed
    public enum towerType{ROCK, PAPER, SCISSORS};

    private towerType type;

    //Towers position on map
    private Vector2 position;

    //Default Constructor
    public Tower() {
        this.position = new Vector2(0,0);
    }

    //Main constructor, passes the desired type and it's location on the map
    public Tower(towerType type, float cellX, float cellY){
        this.type = type;
        this.timeCreated = System.currentTimeMillis();

        //Recording the cell the tower resides in
        this.cellX = cellX;
        this.cellY = cellY;

        //Formula's to convert cellX and cellY to the x/y position on the map
        float positionX = cellX*tileWidth + tileWidth/2;
        float positionY = deviceHeight - 1 - (cellY*tileHeight) - tileHeight/2;

        targetDist = deviceWidth;

        //Setting the costs for all towers
        goldCost = 30;
        upgradeCost = 15;
        switchCost = 10;

        //Setting the position of the tower
        this.position = new Vector2(positionX, positionY);

        //Depending on the tower type we change it's variables
        if (this.type == towerType.ROCK) {
            baseDamage = 3;
        }
        if (this.type == towerType.PAPER) {
            baseDamage = 5;
        }
        if (this.type == towerType.SCISSORS) {
            baseDamage = 1;
        }
    }

    //Just draws a red rectangle to represent the tower
    public void render(ShapeRenderer renderer){
        if(type == towerType.ROCK){
            renderer.setColor(Color.RED);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 20,20);
        }
        if(type == towerType.PAPER){
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 20,20);
        }
        if(type == towerType.SCISSORS){
            renderer.setColor(Color.BLUE);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 20,20);
        }
    }

    //Updates the tower to get a new target
    public void update(List<Enemy> enemyList, float delta) {
        //Updating our damage to the current waveTowerLevel value
        this.scaledDamage = this.baseDamage * waveTowerLevel;
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
        } else {
            this.target = new Enemy();
        }
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public int getScaledDamage() {
        return scaledDamage;
    }

    public void setScaledDamage(int scaledDamage) {
        this.scaledDamage = scaledDamage;
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

    public towerType getType() {
        return type;
    }

    public void setType(towerType type) {
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

    public int getTowerLevel() {
        return towerLevel;
    }

    public void setTowerLevel(int towerLevel) {
        this.towerLevel = towerLevel;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getWaveTowerLevel() {
        return waveTowerLevel;
    }

    public void setWaveTowerLevel(int waveTowerLevel) {
        this.waveTowerLevel = waveTowerLevel;
    }
}
