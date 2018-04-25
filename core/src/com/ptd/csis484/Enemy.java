package com.ptd.csis484;

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
    //Variables for the enemies stats
    private float speed;
    private double health;
    private boolean destroyed;
    private float goldValue;

    //Variable to scale the enemy based on the wave we are on
    private float waveScale = 1;

    //This is the type of tower this enemy is weak against
    private String type;

    //Used to track the enemy's position and which waypoints have been passed
    private Vector2 position = new Vector2();
    private int currentWaypoint = 1;
    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();

    //If the map has more than one path this will decide which path to follow
    private int waypointSet;
    private List<Rectangle> waypointBounds;

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
        this.waypointSet = 0;
    }

    public Enemy(String type, int wave, Map gameMap){
        //Different values are passed depending on the enemy type
        //Enemies stats increase by five percent each wave
        waveScale = (float) (1 + wave * .20);

        this.speed = 3 * waveScale;
        this.health = 6 * waveScale;
        this.type = type;
        this.waypointSet = 0;

        this.waypointBounds = gameMap.getWaypointSets(waypointSet);

        //Setting position equal to the start tile
        position = new Vector2(waypointBounds.get(0).x, waypointBounds.get(0).y);

        //Setting our initial bounds
        Vector2 adjustedPosition = new Vector2(position.x - 16, position.y - 16);
        bounds = new Rectangle(adjustedPosition.x, adjustedPosition.y, 32, 32);

        //Marking the enemy undestroyed
        destroyed = false;

        //Gold value for destroying an enemy
        //We up the gold value by half what the other stats scale
        goldValue = 10 * waveScale/2;
    }

    //Creates the enemy and places it
    public void render(ShapeRenderer renderer){
        if(this.type.equals("ROCK")) {
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32, 32);
        }
        if(this.type.equals("PAPER")){
            renderer.setColor(Color.BLUE);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32, 32);
        }
        if(this.type.equals("SCISSORS")){
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
        Vector2 targetPosition = new Vector2(waypointBounds.get(currentWaypoint).getX(), waypointBounds.get(currentWaypoint).getY());
        this.direction.set(targetPosition).sub(position).nor();
        velocity.set(direction).scl(speed);

        //Adding our velocity to our position
        position.add(velocity);

        //Updating the bounds for the enemy
        bounds = new Rectangle(position.x, position.y, 32, 32);

        //If the enemy crosses over the waypoint we count it as passed
        if(this.bounds.overlaps(waypointBounds.get(currentWaypoint))){
            currentWaypoint++;
        }
    }

    public double getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getGoldValue() {
        return goldValue;
    }
}
