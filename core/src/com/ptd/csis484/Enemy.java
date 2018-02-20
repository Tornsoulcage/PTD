package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by scott on 2/19/2018.
 */

public class Enemy {
    int damage;
    int speed;
    public enum enemyType {ROCK, PAPER, SCISSORS};
    enemyType type;
    float deviceHeight = Gdx.graphics.getHeight();
    float deviceWidth = Gdx.graphics.getWidth();
    float tileHeight = deviceHeight/10;
    float tileWidth = deviceWidth/15;

    Vector2 position;

    public Enemy(enemyType type){

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

        position = new Vector2(0, tileHeight*3 - tileHeight/2);
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.BLACK);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 16,16);
    }

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
