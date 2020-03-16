package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MyGdxGame extends ApplicationAdapter {
	// As an alternative we can grass or water textures to generate 2D maps

	private static final Object GL10 = 1;
	SpriteBatch batch;
	Texture grass;
	Texture water;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		grass = new Texture("grass.jpg");
		water = new Texture("water.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(grass, 0, 1);
		batch.draw(water, 0, 100);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		grass.dispose();
		water.dispose();
	}
}



