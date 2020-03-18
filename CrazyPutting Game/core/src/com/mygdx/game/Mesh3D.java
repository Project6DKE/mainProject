package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Mesh3D extends ApplicationAdapter {
    SpriteBatch batch;
    Texture texture;
    Sprite sprite;
    Sprite[][] spritearr = new Sprite[10][10];
    Sprite[][] spritearr2 = new Sprite[10][10];
    Mesh mesh;
    ShaderProgram shaderProgram;


    OrthographicCamera cam;
    //final Matrix4 matrix = new Matrix4();	

    @Override
    public void create () {
        System.out.println("Created");

        batch = new SpriteBatch();
        texture = new Texture("grass.jpg");
        sprite = new Sprite(texture);
        sprite.setSize(100, 100);
        //sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //cam = new OrthographicCamera(10, 10 * (Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth()));
        cam = new OrthographicCamera(1920, 1080);			
		cam.position.set(1920/2, 1080/2, 10);
		// cam.direction.set(-1, -1, -1);
		// cam.near = 1;
        // cam.far = 100;
        //matrix.setToRotation(new Vector3(1, 0, 0), 00);

        float[] verts = new float[30];
        int i = 0;
        float x,y; // Mesh location in the world
        float width,height; // Mesh width and height

        x = y = 50f;
        width = height = 300f;

        //Top Left Vertex Triangle 1
        verts[i++] = x;   //X
        verts[i++] = y + height; //Y
        verts[i++] = 0;    //Z
        verts[i++] = 0f;   //U
        verts[i++] = 0f;   //V

        //Top Right Vertex Triangle 1
        verts[i++] = x + width;
        verts[i++] = y + height;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Left Vertex Triangle 1
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i++] = 1f;

        //Top Right Vertex Triangle 2
        verts[i++] = x + width;
        verts[i++] = y + height;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 0f;

        //Bottom Right Vertex Triangle 2
        verts[i++] = x + width;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 1f;
        verts[i++] = 1f;

        //Bottom Left Vertex Triangle 2
        verts[i++] = x;
        verts[i++] = y;
        verts[i++] = 0;
        verts[i++] = 0f;
        verts[i] = 1f;

        System.out.println(spritearr.length);
        System.out.println(spritearr[0].length);

        for(int k = 0; k < spritearr.length; k++){
            for(int j = 0; j < spritearr[k].length; j++){
                spritearr[k][j] = new Sprite(texture);
                spritearr[k][j].setSize(100, 100);
				spritearr[k][j].setPosition(k*100,j*100);
                // spritearr2[k][j] = new Sprite(texture);
                // spritearr2[k][j].setSize(100,100);
                // spritearr2[k][j].setPosition(100-k,100-j);
            }
        }

         //Create a mesh out of two triangles rendered clockwise without indices
        mesh = new Mesh( true, 6, 0,
                new VertexAttribute( VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE ),
                new VertexAttribute( VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+"0" ) );

        mesh.setVertices(verts);

        // shaderProgram = new ShaderProgram(
        //         Gdx.files.internal("vertex.glsl").readString(),
        //         Gdx.files.internal("fragment.glsl").readString()
        //         );
        batch = new SpriteBatch();
    }

    @Override
    public void render () {
        System.out.println("Rendered");
        // Gdx.gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Gdx.gl20.glEnable(GL20.GL_TEXTURE_2D);
        // Gdx.gl20.glEnable(GL20.GL_BLEND);
        // Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.begin();
        sprite.draw(batch);
        cam.update();		
        
        //this is important:
        batch.setProjectionMatrix(cam.combined);
        
        //batch.setTransformMatrix(matrix);
		for(int z = 0; z < spritearr.length; z++) {
			for(int x = 0; x < spritearr[z].length; x++) {
                spritearr[x][z].draw(batch);
                // spritearr2[x][z].draw(batch);
			}
		}

        //texture.bind();
        //shaderProgram.begin();
        //shaderProgram.setUniformMatrix("u_projTrans", batch.getProjectionMatrix());
        //shaderProgram.setUniformi("u_texture", 0);

        //mesh.render(shaderProgram, GL20.GL_TRIANGLES);
        batch.end();
        //shaderProgram.end();
    }
}