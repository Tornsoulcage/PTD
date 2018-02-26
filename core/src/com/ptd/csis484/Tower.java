package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by Joseph Scott on 2/22/2018.
 */

//Represents the tower objects
public class Tower {
    //Damage each bullet does
    int damage;

    //Variables used to help calculate position
    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    //Sets a default enemy to be the target
    Enemy target = new Enemy(Enemy.enemyType.ROCK);

    //Variables to help find the closet enemy
    double targetXDist;
    double targetYDist;
    double targetDist;

    //Only three types of towers are allowed
    public enum towerType{ROCK, PAPER, SCISSORS};
    towerType type;

    //Towers position on map
    Vector2 position;

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
                //Gets the current distance to the target
                targetXDist = Math.abs((position.x - target.position.x));
                targetYDist = Math.abs((position.y - target.position.y));
                targetDist = Math.sqrt((targetXDist * targetXDist + targetYDist * targetYDist));
                
                //Gets the distance to the potential target
                targetXDist = Math.abs((position.x - enemyList.get(i).position.x));
                targetYDist = Math.abs((position.y - enemyList.get(i).position.y));
                newTargetDist = Math.sqrt((targetXDist * targetXDist + targetYDist * targetYDist));

                //If potential is less than actual we change targets
                if (newTargetDist < targetDist) {
                    target = enemyList.get(i);
                }
            }
        }
    }
}
