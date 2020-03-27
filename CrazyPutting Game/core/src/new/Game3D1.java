import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Game3D1 extends StackPane{
    private final Rotate rotateY = new Rotate(-145, Rotate.Y_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Sphere ball;
    private Group cube;
    private Main main;
    private int area = 5;
    private double max_height = 2.5;

    public Game3D1(Main main){
        this.main = main;
        createVisualization();
    }


    public Sphere getBall() {
        return ball;
    }

    public void createVisualization() {
        this.cube = new Group();

        this.cube.getTransforms().addAll(this.rotateY);
        this.cube.getTransforms().addAll(this.rotateX);


        TriangleMesh mesh = new TriangleMesh();
        TriangleMesh water = new TriangleMesh();


        Box obs = new Box(1, 1, 1);
        this.cube.getChildren().addAll(obs);

        this.ball = new Sphere();
        this.ball.setRadius(10);
        this.ball.setTranslateZ(200);
        this.ball.setTranslateX(200);
        this.ball.setTranslateY(240);
        int size = 100; //scale


        for (double x = -area; x <= area; x+=((area*2)-0.0001)/((float)(size-1))) {
            for (double y = -area; y <= area; y+=((area*2)-0.0001)/((float)(size-1))) {
                double z = Math.pow(x, 2) + y;  //insert here the function (height)
                if(z < -max_height){
                    z = -max_height;     //limit so the different of height in the field is not too big
                } 
                if(z > max_height) {
                    z = max_height;    //limit so the different of height in the field is not too big
                }   
                mesh.getPoints().addAll(
                    (int)(x * 100), 
                    (int)(z * 100),
                    (int)(y * 100));
                water.getPoints().addAll(
                    (int)(x * 100), 
                    (int)(251),
                    (int)(y * 100));
            }
        }

        // texture
        addTextureMesh(mesh, size);
        addTextureMesh(water, size);

        // faces
        addFacesMesh(mesh, size);
        addFacesMesh(water, size);

        this.cube.setTranslateY(400);   //where we rotate
        this.cube.setTranslateX(400);   //where we rotate

        PhongMaterial fieldMaterial = new PhongMaterial();  //color
        fieldMaterial.setSpecularColor(Color.GREEN);
        fieldMaterial.setDiffuseColor(Color.GREEN);

        PhongMaterial waterMaterial = new PhongMaterial();  //color
        waterMaterial.setSpecularColor(Color.BLUE);
        waterMaterial.setDiffuseColor(Color.BLUE);

        MeshView waterView = new MeshView(water);
        waterView.setMaterial(waterMaterial);
        waterView.setCullFace(CullFace.NONE);
        waterView.setDrawMode(DrawMode.FILL);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(fieldMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        this.cube.getChildren().addAll(meshView);
        this.cube.getChildren().addAll(waterView);

        //Scene scene = new Scene(this.cube, 800, 600, true, SceneAntialiasing.BALANCED);
        main.scene2 = new Scene(this.cube, 800,600,true, SceneAntialiasing.BALANCED);
        main.scene2.setFill(Color.WHITE);
        main.scene2.setCamera(new PerspectiveCamera());

        main.scene2.setOnKeyPressed(t -> {        //Listener
            switch (t.getCode()){
                case LEFT: 
                    this.rotateY.setAngle(this.rotateY.getAngle() - 10); 
                    break;
                case RIGHT: 
                    this.rotateY.setAngle(this.rotateY.getAngle() + 10); 
                    break;
                //it s weird to rotate the field up or down so for the moment i commented it
                case UP: 
                    this.rotateX.setAngle(this.rotateX.getAngle() + 10); 
                    break;
                case DOWN: 
                    this.rotateX.setAngle(this.rotateX.getAngle() - 10); 
                    break;
            }
        });

        this.cube.getChildren().add(this.ball);

        makeZoomable(this.cube);    //we zoom on the cube (that i made invisible, supposed to be closed to the middle of the field)

    }

    public static void addTextureMesh(TriangleMesh mesh, int size) {
        for (float x = 0; x < size - 1; x++) {
            for (float y = 0; y < size - 1; y++) {
                float x0 = x / (float) size;
                float y0 = y / (float) size;
                float x1 = (x + 1) / (float) size;
                float y1 = (y + 1) / (float) size;

                mesh.getTexCoords().addAll(
                        x1, y1,
                        x1, y0,
                        x0, y1,
                        x0, y0
                );
            }
        }
    }

    public static void addFacesMesh(TriangleMesh mesh, int size) {
        for (int x = 0; x < size - 1; x++) {
            for (int z = 0; z < size - 1; z++) {
                int p0 = x * size + z;
                int p1 = x * size + z + 1;
                int p2 = (x + 1) * size + z;
                int p3 = (x + 1) * size + z + 1;

                mesh.getFaces().addAll(p2, 0, p1, 0, p0, 0);
                mesh.getFaces().addAll(p2, 0, p3, 0, p1, 0);
            }
        }
    }



    // zoom on a particular object
    private void makeZoomable(Group control) {  //control is the object we are zooming in
        control.addEventFilter(ScrollEvent.ANY, event -> {
            double delta = 1.2;
            double scale = control.getScaleX();

            if (event.getDeltaY() < 0) scale /= delta;
            else scale *= delta;

            scale = clamp(scale);
            control.setScaleX(scale);
            control.setScaleY(scale);

            event.consume();
        });
    }

    //scale the zoom value
    private static double clamp(double value) { //value is the the current zoom value
        if (Double.compare(value, 0.1) < 0) return 0.1;
        if (Double.compare(value, 10.0) > 0) return 10.0;
        return value;
    }

}