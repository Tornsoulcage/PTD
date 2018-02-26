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

    public Bullet(int dam, Enemy target, Tower source){
        this.damage = source.damage;
        this.target = target;
        this.position = new Vector2(source.position.x, source.position.y);
    }

    public void render(ShapeRenderer renderer){
        renderer.setColor(Color.GREEN);
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(position.x, position.y, 10,10);
    }

    public void update(float delta){
        if(target.position.x > position.x)
            position.x = position.x + 10;
        if(target.position.x < position.x)
            position.x = position.x - 10;
        if(target.position.y > position.y)
            position.y = position.y + 10;
        if(target.position.y < position.y)
            position.y = position.y - 10;
    }
}
