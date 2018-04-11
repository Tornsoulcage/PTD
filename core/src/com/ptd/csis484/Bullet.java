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
    private int damage;
    private Enemy target;
    private Tower source;
    private int speed;

    //Vectors to handle bullet motion
    //Unused vectors are staying in case we change the way we handle movement
    private Vector2 position = new Vector2();
    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();
    private Vector2 interceptPosition = new Vector2();
    private Vector2 movement = new Vector2();

    private Rectangle bounds;

    public Bullet(int dam, Enemy target, Tower source){
        this.source = source;
        this.damage = source.getScaledDamage();
        this.target = target;

        //How many pixels the bullet passes each frame
        this.speed = 20;

        //Updating our movement vectors
        this.position = new Vector2(source.getPosition().x, source.getPosition().y);

        leadTarget();

        direction.set(interceptPosition).sub(position).nor();

        //Setting our initial bounds
        bounds = new Rectangle(position.x, position.y, 10,10);
    }

    //Finds the point the tower should aim towards inorder to actually hit a target
    private void leadTarget(){
        float a = (target.getVelocity().x)*(target.getVelocity().x) + (target.getVelocity().y)*(target.getVelocity().y) - speed*speed;
        float b = 2 * (target.getVelocity().x) + (target.getPosition().x - source.getPosition().x) + (target.getVelocity().y + (target.getPosition().y - source.getPosition().y));
        float c = (target.getPosition().x - source.getPosition().x)*(target.getPosition().x - source.getPosition().x) + (target.getPosition().y - source.getPosition().y)*(target.getPosition().y - source.getPosition().y);

        float disc = b*b - 4*a*c;
        if(!(disc < 0 )){
            float t1 = (float) ((-b + Math.sqrt(disc)) / (2*a));
            float t2 = (float) ((-b - Math.sqrt(disc)) / (2*a));

            if(t1 > 0 && t1 >= t2){
                interceptPosition.x = t1 * target.getVelocity().x + target.getPosition().x;
                interceptPosition.y = t1 * target.getVelocity().y + target.getPosition().y;
            }

            if(t2 > 0 && t2 > t1){
                interceptPosition.x = t2 * target.getVelocity().x + target.getPosition().x;
                interceptPosition.y = t2 * target.getVelocity().y + target.getPosition().y;
            }
        }
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.GREEN);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.circle(position.x, position.y, 5);
    }

    public void update(float delta) {
        //Moves the bullet in the proper direction a number of times equal to it's speed
        //Looks better than just jumping ahead that many paces and helps with actually hitting
        //an enemy.
        for(int i = 0; i < speed; i++) {
            position.add(direction);
        }

        //Updating the bounds for the bullet
        bounds = new Rectangle(position.x, position.y, 10,10);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Enemy getTarget() {
        return target;
    }

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public Tower getSource() {
        return source;
    }

    public void setSource(Tower source) {
        this.source = source;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public Vector2 getMovement() {
        return movement;
    }

    public void setMovement(Vector2 movement) {
        this.movement = movement;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
