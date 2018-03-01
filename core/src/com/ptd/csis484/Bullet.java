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
    Vector2 position;
    Tower source;

    public Bullet(int dam, Enemy target, Tower source){
        this.source = source;
        this.damage = source.damage;
        this.target = target;

        this.position = new Vector2(source.position.x, source.position.y);
    }

    //Updates the bullets target to it's source's current target
    public void changeTarget(){
        this.target = source.target;
        Gdx.app.log("target: ", "" + this.target);
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.GREEN);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 10,10);
    }

    public void update(float delta) {
        for (int i = 0; i < 10; i++) {
            if (target.position.x > position.x)
                position.x++;
            if (target.position.x < position.x)
                position.x--;
            if (target.position.y > position.y)
                position.y++;
            if (target.position.y < position.y)
                position.y--;
        }
    }
}
