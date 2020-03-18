package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Mesh1 extends ApplicationAdapter {

    int[][] test = new int[10][10];
    Mesh mesh;
    PerspectiveCamera cam;
    ShaderProgram shader;
    String vertexShader;
    String fragmentShader;
    final Matrix4 matrix = new Matrix4();	
    // Position attribute - (x, y, z)
    public static final int POSITION_COMPONENTS = 3;

    // Color attribute - (r, g, b, a)
    public static final int COLOR_COMPONENTS = 4;

    // Total number of components for all attributes
    public static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOR_COMPONENTS;

    // The "size" (total number of floats) for a single triangle
    public static final int PRIMITIVE_SIZE = 3 * NUM_COMPONENTS;

    // The maximum number of triangles our mesh will hold
    public static final int MAX_TRIS = 1;

    // The maximum number of vertices our mesh will hold
    public static final int MAX_VERTS = MAX_TRIS * 3;

    // The array which holds all the data, interleaved like so:
    // x, y, z, r, g, b, a
    // x, y, z, r, g, b, a,
    // x, y, z, r, g, b, a,
    // ... etc ...
    protected float[] verts = new float[MAX_VERTS * NUM_COMPONENTS];

    // The current index that we are pushing triangles into the array
    protected int idx = 0;

    public void create() {
        mesh = new Mesh(true, MAX_VERTS, 0, new VertexAttribute(Usage.Position, POSITION_COMPONENTS, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, COLOR_COMPONENTS, "a_color"));
        cam = new PerspectiveCamera();
        //cam.translate(-Gdx.graphics.getWidth()/2, -Gdx.graphics.getHeight()/2, 0);
        vertexShader = Gdx.files.internal("vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("fragment.glsl").readString();
        shader = new ShaderProgram(vertexShader, fragmentShader);
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test.length; j++) {
                test[i][j] = (int) (Math.random() * 5);
            }
        }
        cam.position.set(5, 5, 10);
		cam.direction.set(-1, -1, -1);
		cam.near = 0;
        cam.far = 1000;	
        matrix.setToRotation(new Vector3(1, 0, 0), 90);
    }

    public void drawTriangle(float x, float y, float z, float width, float height, Color color) {
        // we don't want to hit any index out of bounds exception...
        // so we need to flush the batch if we can't store any more verts
        if (idx == verts.length)
            flush();

        // now we push the vertex data into our array
        // we are assuming (0, 0) is lower left, and Y is up

        // bottom left vertex
        verts[idx++] = x; // Position(x, y, z)
        verts[idx++] = y;
        verts[idx++] = z;
        verts[idx++] = color.r; // Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;

        // top left vertex
        verts[idx++] = x; // Position(x, y, z)
        verts[idx++] = y + height;
        verts[idx++] = z;
        verts[idx++] = color.r; // Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;

        // bottom right vertex
        verts[idx++] = x + width; // Position(x, y, z)
        verts[idx++] = y;
        verts[idx++] = z;
        verts[idx++] = color.r; // Color(r, g, b, a)
        verts[idx++] = color.g;
        verts[idx++] = color.b;
        verts[idx++] = color.a;}

        // top left vertex
    //     verts[idx++] = x; // Position(x, y, z)
    //     verts[idx++] = y + height;
    //     verts[idx++] = z;
    //     verts[idx++] = color.r; // Color(r, g, b, a)
    //     verts[idx++] = color.g;
    //     verts[idx++] = color.b;
    //     verts[idx++] = color.a;

    //     // top right vertex
    //     verts[idx++] = x + width; // Position(x, y, z)
    //     verts[idx++] = y + height;
    //     verts[idx++] = z;
    //     verts[idx++] = color.r; // Color(r, g, b, a)
    //     verts[idx++] = color.g;
    //     verts[idx++] = color.b;
    //     verts[idx++] = color.a;

    //     // bottom right vertex
    //     verts[idx++] = x + width; // Position(x, y, z)
    //     verts[idx++] = y;
    //     verts[idx++] = z;
    //     verts[idx++] = color.r; // Color(r, g, b, a)
    //     verts[idx++] = color.g;
    //     verts[idx++] = color.b;
    //     verts[idx++] = color.a;
    // }

    public void flush() {
        // if we've already flushed
        if (idx == 0)
            return;

        // sends our vertex data to the mesh
        mesh.setVertices(verts);

        // no need for depth...
        Gdx.gl.glDepthMask(true);

        // enable blending, for alpha
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // number of vertices we need to render
        int vertexCount = (idx / NUM_COMPONENTS);

        // update the camera with our Y-up coordiantes
        //cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // start the shader before setting any uniforms
        shader.begin();

        // update the projection matrix so our triangles are rendered in 2D
        shader.setUniformMatrix("u_projTrans", cam.combined);

        // render the mesh
        mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);
        shader.end();

        //re-enable depth to reset states to their default
        Gdx.gl.glDepthMask(true);
        
        //reset index to zero
        idx = 0;
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //push a few triangles to the batch
        // drawTriangle(10, 10, 40, 40, Color.RED);
        // drawTriangle(50, 50, 70, 40, Color.BLUE);
        
        for(int i = 0; i<test.length; i++){
            for(int j = 0; j<test.length; j++){
                    drawTriangle(i*10, j*10,test[i][j], 10, 10, Color.GREEN);
            }
        }
        //this will render the above triangles to GL, using Mesh
        flush();
    }
}