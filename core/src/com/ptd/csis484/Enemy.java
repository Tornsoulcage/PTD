package com.ptd.csis484;

import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by scott on 2/19/2018.
 */

public class Enemy {
    int damage;
    int speed;
    public enum enemyType {ROCK, PAPER, SCISSORS};
    enemyType type;

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
    }

}
