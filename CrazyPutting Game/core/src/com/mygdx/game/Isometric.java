package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.ApplicationAdapter;

public class Isometric extends ApplicationAdapter {	
	Texture texture;
	OrthographicCamera cam;
	SpriteBatch batch;	
	final Sprite[][] sprites = new Sprite[10][10];
	final Matrix4 matrix = new Matrix4();	
 
	public void create() {
		texture = new Texture(Gdx.files.internal("grass100x100.png"));		
		//cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));		
		cam = new OrthographicCamera(10, 10);	
		cam.position.set(5, 5, 10);
		cam.direction.set(-1, -1, -1);
		cam.near = 0;
		cam.far = 1000;	
		cam.translate(5,5);
		//cam = new OrthographicCamera(1920,1080);
		//cam.position.set(1920/2, 1080/2,10);	
		matrix.setToRotation(new Vector3(1, 0, 0), 90);
		
		for(int z = 0; z < 5; z++) {
			for(int x = 0; x < 5; x++) {
				sprites[x][z] = new Sprite(texture);
				sprites[x][z].setPosition(x,z);
				sprites[x][z].setSize(1, 1);
			}
		}
			
		batch = new SpriteBatch();
		
}
        
    public void render() {
    	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	cam.update();		
                    
    	batch.setProjectionMatrix(cam.combined);
    	batch.setTransformMatrix(matrix);
    	batch.begin();
    	for(int z = 0; z < 5; z++) {
            for(int x = 0; x < 5; x++) {
                sprites[x][z].draw(batch);
            }
		}
		handleInput();
		batch.end();
		
	}   
	protected void handleInput(){
		// if(Gdx.input.isTouched()){
		// 	cam.translate(1, 1);
		// }
	}
		
	
}
