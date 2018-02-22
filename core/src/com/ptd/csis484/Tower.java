package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Joseph Scott on 2/22/2018.
 */

public class Tower {
    int damage;

    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    public enum towerType{ROCK, PAPER, SCISSORS};
    towerType type;
    Vector2 position;

    public Tower(towerType type, int screenX, int screenY){
        this.type = type;

        float cellX = screenX/tileWidth;
        float cellY = screenY/tileHeight;

        cellX = MathUtils.floor(cellX);
        cellY = MathUtils.floor(cellY);

        this.position = new Vector2(cellX*tileWidth, cellY*tileHeight);

        if(this.type == towerType.ROCK)
            damage = 3;

        if(this.type == towerType.PAPER)
            damage = 5;

        if(this.type == towerType.SCISSORS)
            damage = 1;
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.RED);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 20,20);
    }
}
