package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by scott on 2/19/2018.
 */

//Class representing the three types of enemies
public class Enemy {
    //Variables for the enemies
    double damage;
    double speed;
    double health;
    boolean destroyed;
    double waveScale = 1;

    //Used to track the enemy's position and which waypoints have been passed
    Vector2 position;
    Boolean waypointPassed[] = new Boolean[7];
    Vector2 waypointTiles[] = new Vector2[7];
    Vector2 waypointStart = new Vector2();

    //There are only three types of enemies
    //These determine what values go into the above variables
    public enum enemyType {ROCK, PAPER, SCISSORS};
    enemyType type;

    //Used to help determine where to place the enemy and move it around
    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    public Enemy(enemyType type, int wave){
        //Different values are passed depending on the enemy type
        waveScale = wave * 1.15;

        if(type == enemyType.ROCK) {
            this.damage = 5 * waveScale;
            this.speed = 3* waveScale;
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

        //Marking the enemy undestroyed
        destroyed = false;

        //Set every waypoint to false
        for(int i = 0; i < waypointPassed.length; i++){
            waypointPassed[i] = false;
        }

        //TODO Control turns on waypoints so the enemy turns once they reach the interior/center point
        //Maybe use the same technique here as the bullets and do a loop of increments up to the speed value
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
        if(this.type == enemyType.ROCK) {
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x - tileHeight/2, position.y, 16, 16);
        }
        if(this.type == enemyType.PAPER){
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.rect(position.x, position.y, 16, tileHeight);
        }
        if(this.type == enemyType.SCISSORS){
            renderer.setColor(Color.BLACK);
            renderer.set(ShapeRenderer.ShapeType.Filled);
            renderer.circle(position.x, position.y, 16);
        }
    }

    //Moves the enemy around the map
    public void update(float delta, List<Bullet> bulletList) {
        //Checks to see if this enemy was hit by a bullet
        for(int i = 0; i < bulletList.size(); i++){
            if((bulletList.get(i).position.x <= this.position.x + 16) && bulletList.get(i).position.x >= this.position.x){
                if((bulletList.get(i).position.y <= this.position.y + 16) && (bulletList.get(i).position.y >= this.position.y)){
                    this.health -= bulletList.get(i).damage;
                    bulletList.remove(i);
                }
            }
        }

        //Each if statement checks if the enemy is between the next waypoint and the previous one
        //If they are we move the enemy in the proper direction until they hit the next waypoint
        //Then we change that point to passed/true and repeat.
        if(!waypointPassed[0])
            position.x += speed;

        if(position.x >= waypointTiles[0].x)
            waypointPassed[0] = true;

        if(waypointPassed[0] && !waypointPassed[1])
            position.y += speed;

        if(position.y >= waypointTiles[1].y)
            waypointPassed[1] = true;

        if(waypointPassed[1] && !waypointPassed[2])
            position.x += speed;

        if(position.x >= waypointTiles[2].x)
            waypointPassed[2] = true;

        if(waypointPassed[2] && !waypointPassed[3])
            position.y -= speed;

        if(position.y <= waypointTiles[3].y)
            waypointPassed[3] = true;

        if(waypointPassed[3] && !waypointPassed[4])
            position.x += speed;

        if(position.x >= waypointTiles[4].x)
            waypointPassed[4] = true;

        if(waypointPassed[4] && !waypointPassed[5])
           position.y += speed;

        if(position.y >= waypointTiles[5].y)
            waypointPassed[5] = true;

        if(waypointPassed[5] && !waypointPassed[6])
            position.x += speed;

        if(position.x >= waypointTiles[6].x) {
            waypointPassed[6] = true;
        }
    }
}
