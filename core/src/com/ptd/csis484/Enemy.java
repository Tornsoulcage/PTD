package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.Array;

import sun.rmi.runtime.Log;

/**
 * Created by scott on 2/19/2018.
 */

//Class representing the three types of enemies
public class Enemy {
    //Variables for the enemies
    int damage;
    int speed;
    int health;

    //Used to track the enemy's position and which waypoints have been passed
    Vector2 position;
    Boolean waypointPassed[] = new Boolean[7];
    Vector2 waypointTiles[] = new Vector2[7];

    //There are only three types of enemies
    //These determine what values go into the above variables
    public enum enemyType {ROCK, PAPER, SCISSORS};
    enemyType type;

    //Used to help determine where to place the enemy and move it around
    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    public Enemy(enemyType type){
        //Different values are passed depending on the enemy type
        if(type == enemyType.ROCK) {
            this.damage = 5;
            this.speed = 3;
            this.health = 5;
            this.type = type;
        }
        if(type == enemyType.SCISSORS){
            this.damage = 1;
            this.speed = 5;
            this.health = 3;
            this.type = type;
        }
        if(type == enemyType.PAPER){
            this.damage = 3;
            this.speed = 3;
            this.health = 3;
            this.type = type;
        }

        //This is where the start tile currently is
        position = new Vector2(0, tileHeight*3 - tileHeight/2);

        //Set every waypoint to false
        for(int i = 0; i < waypointPassed.length; i++){
            waypointPassed[i] = false;
        }

        //Sets the waypoints for the test map
        waypointTiles[0] = new Vector2(tileWidth*6 + tileWidth/2, tileHeight*3 - tileHeight/2);
        waypointTiles[1] = new Vector2(tileWidth*6 + tileWidth/2, tileHeight*7 - tileHeight/2);
        waypointTiles[2] = new Vector2(tileWidth*10 + tileWidth/2, tileHeight*7 - tileHeight/2);
        waypointTiles[3] = new Vector2(tileWidth*10 + tileWidth/2, tileHeight*2 - tileHeight/2);
        waypointTiles[4] = new Vector2(tileWidth*13 + tileWidth/2, tileHeight*2 - tileHeight/2);
        waypointTiles[5] = new Vector2(tileWidth*13 + tileWidth/2, tileHeight*9 - tileHeight/2);
        waypointTiles[6] = new Vector2(tileWidth*15 + tileWidth/2, tileHeight*9 - tileHeight/2);
    }

    //Creates the enemy and places it
    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 16,16);
    }

    //Moves the enemy around the map
    public void update(float delta) {
        //Each if statement checks if the enemy is between the next waypoint and the previous one
        //If they are we move the enemy in the proper direction until they hit the next waypoint
        //Then we change that point to passed/true and repeat.
        if(!waypointPassed[0])
            position.x++;

        if(position.x >= waypointTiles[0].x)
            waypointPassed[0] = true;

        if(waypointPassed[0] && !waypointPassed[1])
            position.y++;

        if(position.y >= waypointTiles[1].y)
            waypointPassed[1] = true;

        if(waypointPassed[1] && !waypointPassed[2])
            position.x++;

        if(position.x >= waypointTiles[2].x)
            waypointPassed[2] = true;

        if(waypointPassed[2] && !waypointPassed[3])
            position.y--;

        if(position.y <= waypointTiles[3].y)
            waypointPassed[3] = true;

        if(waypointPassed[3] && !waypointPassed[4])
            position.x++;

        if(position.x >= waypointTiles[4].x)
            waypointPassed[4] = true;

        if(waypointPassed[4] && !waypointPassed[5])
           position.y++;

        if(position.y >= waypointTiles[5].y)
            waypointPassed[5] = true;

        if(waypointPassed[5] && !waypointPassed[6])
            position.x++;

        if(position.x >= waypointTiles[6].x)
            waypointPassed[6] = true;
    }
}
