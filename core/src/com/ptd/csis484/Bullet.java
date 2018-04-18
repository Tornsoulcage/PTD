package com.ptd.csis484;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Description: Represents the bullets fired from towers
 *
 * Created by Joseph A Scott on 2/22/2018.
 */

public class Bullet {
    //Objects to help calculate direction
    private Enemy target;
    private Tower source;

    //Variables for bullet stats
    private int speed;
    private double damage;

    //Vectors to handle bullet motion
    private Vector2 position = new Vector2();
    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();

    //The bounds of this bullet
    private Rectangle bounds;

    public Bullet(Enemy target, Tower source){
        //Setting the objects
        this.source = source;
        this.target = target;

        this.damage = source.getScaledDamage();

        //How many pixels the bullet passes each frame
        this.speed = 20;

        //Setting our movement vectors
        this.position = new Vector2(source.getPosition().x, source.getPosition().y);
        this.direction.set(target.getPosition()).sub(this.position);
        this.velocity.set(direction).scl(speed);

        //Setting our initial bounds
        this.bounds = new Rectangle(position.x, position.y, 10,10);
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.GREEN);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.circle(position.x, position.y, 5);
    }

    //Moves our bullet towards the enemy
    //Bullets are homing so they constantly adjust direction to ensure they hit the target
    public void update() {
        //If our target was destroyed we just continue going in the last known direction until the
        //bullet exits the map
        if(!target.isDestroyed()){
            //Finding the current direction to the target and scaling our movement vector to our speed
            direction.set(target.getPosition()).sub(position).nor();
            velocity.set(direction).scl(speed);
        }

        //Adding our velocity to our position
        position.add(velocity);

        //Updating the bounds for the bullet
        bounds = new Rectangle(position.x, position.y, 10,10);
    }

    public double getDamage() {
        return damage;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
