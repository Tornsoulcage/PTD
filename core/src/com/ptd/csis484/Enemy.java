package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by scott on 2/19/2018.
 */

//Class representing the three types of enemies
public class Enemy {
    //Variables for the enemies
    int damage;
    int speed;
    int health;
    Vector2 position;

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
            this.type = type;
        }
        if(type == enemyType.SCISSORS){
            this.damage = 1;
            this.speed = 5;
            this.type = type;
        }
        if(type == enemyType.PAPER){
            this.damage = 3;
            this.speed = 3;
            this.type = type;
        }

        //This is where the start tile currently is
        position = new Vector2(0, tileHeight*3 - tileHeight/2);
    }

    //Creates the enemy and places it
    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 16,16);
    }

    //Moves the enemy around the map
    public void update(float delta){
        if(position.x < (tileWidth*6 + tileWidth/2)) {
            this.position.x++;
        }
        if((position.x == (tileWidth*6 + tileWidth/2)) && (position.y < (tileHeight*7 - tileHeight/2))){
            this.position.y++;
        }
        if((position.x < (tileWidth*10 + tileWidth/2)) && (position.x >= (tileWidth*6 + tileWidth/2))) {
            this.position.x++;
        }
        if((position.x == (tileWidth*10 + tileWidth/2)) && (position.y > ((tileHeight * 2) - tileHeight/2))){
            this.position.y--;
        }
        if((position.x < (tileWidth*13 + tileWidth/2)) && (position.x >= ((tileWidth*10) + tileWidth/2))) {
            this.position.x++;
        }
        if((position.x == (tileWidth*13 + tileWidth/2)) && position.y < ((tileHeight * 9) - tileHeight/2)){
            this.position.y++;
        }
        if((position.x < (tileWidth*15 + tileWidth/2)) && (position.x >= ((tileWidth*13) + tileWidth/2))) {
            this.position.x++;
        }
    }
}
