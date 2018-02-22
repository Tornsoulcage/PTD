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

    //This Towers target
    Enemy target;

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

    public void update(List<Enemy> enemyList, float delta){
        double newTargetDist;
        for(int i = 0; i < enemyList.size(); i++){
            targetXDist = Math.abs((position.x - enemyList.get(i).position.x));
            targetYDist = Math.abs((position.y - enemyList.get(i).position.y));
            newTargetDist = Math.sqrt((targetXDist*targetXDist + targetYDist*targetYDist));

            if(newTargetDist < targetDist) {
                target = enemyList.get(i);
                targetDist = newTargetDist;
            }
        }
    }
}
