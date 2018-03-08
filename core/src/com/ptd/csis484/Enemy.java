package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2/19/2018.
 */

//Class representing the three types of enemies
public class Enemy {
    //Variables for the enemies
    double damage;
    float speed;
    double health;
    boolean destroyed;
    float waveScale = 1;
    int currentWaypoint = 0;

    //Used to track the enemy's position and which waypoints have been passed
    Vector2 position = new Vector2();
    Vector2 waypointStart = new Vector2();

    List<Rectangle> waypointBounds = new ArrayList<Rectangle>();

    Vector2 direction = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 targetPosition = new Vector2();

    //There are only three types of enemies
    //These determine what values go into the above variables
    public enum enemyType {ROCK, PAPER, SCISSORS};
    enemyType type;

    //Used to help determine where to place the enemy and move it around
    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    //Bounds of the enemy's rectangle
    Rectangle bounds;

    public Enemy(enemyType type, int wave){
        //Different values are passed depending on the enemy type
        //Enemies stats increase by one percent each wave
        waveScale = (float)(wave * 1.01);

        if(type == enemyType.ROCK) {
            this.damage = 5 * waveScale;
            this.speed = 3 * waveScale;
            this.health = 5* waveScale;
            this.type = type;
        }
        if(type == enemyType.SCISSORS){
            this.damage = 1* waveScale;
            this.speed = 5* waveScale;
            this.health = 3* waveScale;
            this.type = type;
        }
        if(type == enemyType.PAPER){
            this.damage = 3* waveScale;
            this.speed = 3* waveScale;
            this.health = 3* waveScale;
            this.type = type;
        }

        //This is where the start tile currently is
        waypointStart.x = 0;
        waypointStart.y = tileHeight*3 - tileHeight/2;

        //Setting position equal to the start tile
        position = new Vector2(waypointStart.x, waypointStart.y);

        //Setting our initial bounds
        bounds = new Rectangle(position.x, position.y, 32, 32);


        //Marking the enemy undestroyed
        destroyed = false;

        //Setting our waypoints for the test map
        waypointBounds.add(new Rectangle(tileWidth*6 + tileWidth/3, tileHeight*2 + tileHeight/3, tileWidth/3 , tileHeight/3));
        waypointBounds.add(new Rectangle(tileWidth*6 + tileWidth/3, tileHeight*6 + tileHeight/3, tileWidth/3, tileHeight/3));
        waypointBounds.add(new Rectangle(tileWidth*10 + tileWidth/3, tileHeight*6 + tileHeight/3, tileWidth/3, tileHeight/3));
        waypointBounds.add(new Rectangle(tileWidth*10 + tileWidth/3, tileHeight*1 + tileHeight/3, tileWidth/3, tileHeight/3));
        waypointBounds.add(new Rectangle(tileWidth*13 + tileWidth/3, tileHeight*1 + tileHeight/3, tileWidth/3, tileHeight/3));
        waypointBounds.add(new Rectangle(tileWidth*13 + tileWidth/3, tileHeight*8 + tileHeight/3, tileWidth/3, tileHeight/3));
        waypointBounds.add(new Rectangle(tileWidth*15 + tileWidth/3, tileHeight*8 + tileHeight/3, tileWidth/3, tileHeight/3));
    }

    //Creates the enemy and places it
    public void render(ShapeRenderer renderer){
        if(this.type == enemyType.ROCK) {
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32, 32);
        }
        if(this.type == enemyType.PAPER){
            renderer.setColor(Color.BLUE);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32, 32);
        }
        if(this.type == enemyType.SCISSORS){
            renderer.setColor(Color.RED);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 32,32);
        }
    }

    //Moves the enemy around the map
    public void update(float delta, List<Bullet> bulletList) {
        //Iterating over the bullets to check for collisions
        List<Bullet> toRemove = new ArrayList<Bullet>();
        for (Bullet bullet : bulletList) {
            if (this.bounds.overlaps(bullet.getBounds())) {
                this.health -= bullet.damage;
                toRemove.add(bullet);
            }
        }
        //Removing all bullets that did collide
        bulletList.removeAll(toRemove);

        //Setting our movement vectors
        this.targetPosition = new Vector2(waypointBounds.get(currentWaypoint).getX(), waypointBounds.get(currentWaypoint).getY());
        this.direction.set(targetPosition).sub(position).nor();
        velocity.set(direction).scl(this.speed);

        position.add(velocity);

        //Updating the bounds for the enemy
        bounds = new Rectangle(position.x, position.y, 32, 32);

        //If the enemey crosses over the waypoint we count it as passed
        if(this.bounds.overlaps(waypointBounds.get(currentWaypoint))){
            currentWaypoint++;
        }
    }
}
