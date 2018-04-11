package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: Represents the enemies
 *
 * Created by scott on 2/19/2018.
 */

//Class representing the three types of enemies
public class Enemy {
    //Variables for the enemies
    private float speed;
    private double health;
    private boolean destroyed;
    private float waveScale = 1;
    private int goldValue = 0;

    //Used to track the enemy's position and which waypoints have been passed
    private Vector2 position = new Vector2();
    private int currentWaypoint = 0;
    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();
    private Vector2 targetPosition = new Vector2();

    //This is the type of tower this enemy is weak against
    private String type;

    //Used to help determine where to place the enemy and move it around
    private float deviceHeight = Gdx.graphics.getHeight();
    private float deviceWidth = Gdx.graphics.getWidth();
    private float tileHeight = deviceHeight/10;
    private float tileWidth = deviceWidth/15;

    //Bounds of the enemy's rectangle
    private Rectangle bounds;

    //Default Constructor
    public Enemy() {
        this.speed = 0;
        this.health = 1;
        this.destroyed = false;
        this.goldValue = 0;
        this.position = new Vector2(0,0);
        this.type = "NO_ENEMY_TYPE";
    }

    public Enemy(String type, int wave, Map gameMap){
        //Different values are passed depending on the enemy type
        //Enemies stats increase by one percent each wave
        waveScale = (float) (1 + wave * .05);

        if(type == "ROCK") {
            this.speed = 2 * waveScale;
            this.health = 5* waveScale;
            this.type = type;
        }
        if(type == "SCISSORS"){
            this.speed = 5* waveScale;
            this.health = 3* waveScale;
            this.type = type;
        }
        if(type == "PAPER"){
            this.speed = 3* waveScale;
            this.health = 1* waveScale;
            this.type = type;
        }

        //Setting position equal to the start tile
        position = new Vector2(gameMap.getWaypointStart().x, gameMap.getWaypointStart().y);

        //Setting our initial bounds
        bounds = new Rectangle(position.x, position.y, 32, 32);

        //Marking the enemy undestroyed
        destroyed = false;

        //Gold value for destroying an enemy
        goldValue = 10;
    }

    //Creates the enemy and places it
    public void render(ShapeRenderer renderer){
        if(this.type == "ROCK") {
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32, 32);
        }
        if(this.type == "PAPER"){
            renderer.setColor(Color.BLUE);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32, 32);
        }
        if(this.type == "SCISSORS"){
            renderer.setColor(Color.RED);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32,32);
        }
    }

    //Moves the enemy around the map
    public void update(List<Bullet> bulletList, Map gameMap) {
        //Iterating over the bullets to check for collisions
        List<Bullet> toRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletList) {
            if (this.bounds.overlaps(bullet.getBounds())) {
                this.health -= bullet.getDamage();
                toRemove.add(bullet);
            }
        }
        //Removing all bullets that did collide
        bulletList.removeAll(toRemove);

        //Setting our movement vectors
        this.targetPosition = new Vector2(gameMap.getWaypointBounds().get(currentWaypoint).getX(), gameMap.getWaypointBounds().get(currentWaypoint).getY());
        this.direction.set(targetPosition).sub(position).nor();
        velocity.set(direction).scl(speed);

        //Adding our velocity to our position
        position.add(velocity);

        //Updating the bounds for the enemy
        bounds = new Rectangle(position.x, position.y, 32, 32);

        //If the enemy crosses over the waypoint we count it as passed
        if(this.bounds.overlaps(gameMap.getWaypointBounds().get(currentWaypoint))){
            currentWaypoint++;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public float getWaveScale() {
        return waveScale;
    }

    public void setWaveScale(float waveScale) {
        this.waveScale = waveScale;
    }

    public int getCurrentWaypoint() {
        return currentWaypoint;
    }

    public void setCurrentWaypoint(int currentWaypoint) {
        this.currentWaypoint = currentWaypoint;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public int getGoldValue() {
        return goldValue;
    }

    public void setGoldValue(int goldValue) {
        this.goldValue = goldValue;
    }
}
