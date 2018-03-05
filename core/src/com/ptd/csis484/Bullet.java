package com.ptd.csis484;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Joseph Scott on 2/22/2018.
 */

public class Bullet {
    int damage;
    Enemy target;
    Tower source;
    int speed;

    //Vectors to handle bullet motion
    //Unused vectors are staying incase we change the way we handle movement
    Vector2 position = new Vector2();
    Vector2 direction = new Vector2();
    Vector2 velocity = new Vector2();
    Vector2 targetPosition = new Vector2();
    Vector2 movement = new Vector2();


    public Bullet(int dam, Enemy target, Tower source){
        this.source = source;
        this.damage = source.damage;
        this.target = target;

        //10 is the default speed for now
        this.speed = 10;

        //Updating our movement vectors
        this.position = new Vector2(source.position.x, source.position.y);
        this.targetPosition = new Vector2(target.position.x, target.position.y);
        direction.set(targetPosition).sub(position).nor();

    }

    //Updates the bullets target to it's source's current target
    public void changeTarget(){
        this.target = source.target;
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.GREEN);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 10,10);
    }

    public void update(float delta) {
        //Moves the bullet in the proper direction a number of times equal to it's speed
        //Looks better than just jumping ahead that many paces and helps with actually hitting
        //an enemy.
        for(int i = 0; i < speed; i++) {
            position.add(direction);
        }
    }
}
