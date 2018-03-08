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
    private int damage;

    //Variables used to help calculate position
    private float deviceHeight = Gdx.graphics.getHeight();
    private float deviceWidth = Gdx.graphics.getWidth();
    private float tileHeight = deviceHeight/10;
    private float tileWidth = deviceWidth/15;

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

    //Main constructor, passes the desired type and it's location on the map
    public Tower(towerType type, float cellX, float cellY){
        this.type = type;

        //Formula's to convert cellX and cellY to the x/y position on the map
        float positionX = cellX*tileWidth + tileWidth/2;
        float positionY = deviceHeight - 1 - (cellY*tileHeight) - tileHeight/2;

        targetDist = deviceWidth;

        //Setting the position of the tower
        this.position = new Vector2(positionX, positionY);

        //Depending on the tower type we change it's variables
        if (this.type == towerType.ROCK)
            damage = 3;

        if (this.type == towerType.PAPER)
            damage = 5;

        if (this.type == towerType.SCISSORS)
            damage = 1;
    }

    //Just draws a red rectangle to represent the tower
    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.RED);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 20,20);
    }

    //Updates the tower to get a new target
    public void update(List<Enemy> enemyList, float delta) {
        //Variable to hold temp distance for each enemy
        double newTargetDist;
        //Only runs if there is a enemy
        if (!enemyList.isEmpty()) {
            //Loops through the enemy list
            for (int i = 0; i < enemyList.size(); i++) {
                //If our target has been destroyed we switch to the default target
                if(target.isDestroyed()){
                    this.target = new Enemy(Enemy.enemyType.ROCK, 1);
                }

                //Gets the current distance to the target
                targetXDist = Math.abs((position.x - target.getPosition().x));
                targetYDist = Math.abs((position.y - target.getPosition().y));
                targetDist = Math.sqrt((targetXDist * targetXDist + targetYDist * targetYDist));

                //Gets the distance to the potential target
                targetXDist = Math.abs((position.x - enemyList.get(i).getPosition().x));
                targetYDist = Math.abs((position.y - enemyList.get(i).getPosition().y));
                newTargetDist = Math.sqrt((targetXDist * targetXDist + targetYDist * targetYDist));

                //If potential is less than actual we change targets
                if (newTargetDist < targetDist) {
                    target = enemyList.get(i);
                }
            }
        } else {
            this.target = new Enemy(Enemy.enemyType.ROCK, 1);
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
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
}
