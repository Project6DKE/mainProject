package MainProject;

import javafx.application.Application;

import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Font;

public class Game3D1 extends StackPane{
    private final Rotate rotateY = new Rotate(-145, Rotate.Y_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Sphere ball;
    private Group cube;
    private Main main;
    private int area = 5;
    private double max_height = 2.5;
    private double speed_value = 50;
    private double angle_value = 90;
    private int stroke = 0;
    private Label lbl_stroke = new Label();
    private Camera cam;
    private PuttingSimulator PS;
    private final int ball_radius = 10;
    private double anchorX;

    public Game3D1(Main main, PuttingCourse PC){
        this.main = main;
        PS = new PuttingSimulator(PC, new EulerSolver());
        createVisualization();
        
    }


    public Sphere getBall() {
        return ball;
    }

    public void createVisualization() {
    	Group trees = loadModel(getClass().getResource("Objects/trees.obj"));
        trees.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        trees.getTransforms().add(new Scale(3000, 3000, 3000));
        trees.getTransforms().add(new Translate(1, 11, 10));

        Group chicken = loadModel(getClass().getResource("Objects/chicken.obj"));
        chicken.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        chicken.getTransforms().add(new Scale(10, 10, 10));
        chicken.getTransforms().add(new Translate(0, 0, 0));
        
        Group grass = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass.getTransforms().add(new Scale(3, 3, 3));
        grass.getTransforms().add(new Translate(-30, 3, 50));

        Group grass2 = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass2.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass2.getTransforms().add(new Scale(3, 3, 3));
        grass2.getTransforms().add(new Translate(-60, 3, 50));

        Group grass3 = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass3.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass3.getTransforms().add(new Scale(3, 3, 3));
        grass3.getTransforms().add(new Translate(-30, 3, 70));

        Group grass4 = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass4.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass4.getTransforms().add(new Scale(3, 3, 3));
        grass4.getTransforms().add(new Translate(-45, 3, 40));

        Group grass5 = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass5.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass5.getTransforms().add(new Scale(3, 3, 3));
        grass5.getTransforms().add(new Translate(-10, 3, 50));

        Group grass6 = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass6.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass6.getTransforms().add(new Scale(3, 3, 3));
        grass6.getTransforms().add(new Translate(-40, 3, 50));

        Group grass7 = loadModel(getClass().getResource("Objects/bunchOfGrass.obj"));
        grass7.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        grass7.getTransforms().add(new Scale(3, 3, 3));
        grass7.getTransforms().add(new Translate(-35, 3, 40));
        

        Group flag = loadModel(getClass().getResource("Objects/flag.obj"));
        flag.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        flag.getTransforms().add(new Scale(30, 30, 30));
        //flag.getTransforms().add(new Translate(-10, 7, -10));

    	
        this.cube = new Group();
        
        cube.getChildren().add(grass);  cube.getChildren().add(grass2);
        cube.getChildren().add(grass3); cube.getChildren().add(grass4);
        cube.getChildren().add(grass5); cube.getChildren().add(grass6);
        cube.getChildren().add(grass7);
        
        //cube.getChildren().add(chicken);
        //cube.getChildren().add(trees);
        cube.getChildren().add(flag);
        
        Vector2d flagpos = PS.course.get_flag_position();
        flag.getTransforms().add(new Translate(flagpos.get_x(), flagpos.get_y()-5, PS.course.get_height().evaluate(flagpos)));

        this.cube.getTransforms().addAll(this.rotateY);
        this.cube.getTransforms().addAll(this.rotateX);

        cam = new PerspectiveCamera();
        cam.setNearClip(0.1);
        cam.setFarClip(100000.0);
        
        TriangleMesh mesh = new TriangleMesh();
        TriangleMesh water = new TriangleMesh();


        Box obs = new Box(1, 1, 1);
        this.cube.getChildren().addAll(obs);

        this.ball = new Sphere();
        this.ball.setRadius(ball_radius);
//        this.ball.setTranslateZ(200);
//        this.ball.setTranslateX(200);
//        this.ball.setTranslateY(240);
        
        ballPosition();
        
        int size = 5; //scale


        for (double x = -area; x <= area; x+=((area*2)-0.0001)/((float)(size-1))) {
            for (double y = -area; y <= area; y+=((area*2)-0.0001)/((float)(size-1))) {
                //double z = Math.pow(x, 2) + y;  //insert here the function (height)
            	//double z = 2.5;
            	double z = PS.course.get_height().evaluate(new Vector2d(x,y));
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
        //this.cube.getChildren().addAll(waterView);

        this.cube.getChildren().add(this.ball);
        //makeZoomable(this.cube);
        this.cube.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch(event.getCode()) {
            case O:double delta = 1.2;
            	double scale = this.cube.getScaleX();
            	scale/= delta;
            	scale = clamp(scale);
            	this.cube.setScaleX(scale);
            	this.cube.setScaleY(scale);
            	event.consume();
            	break;
            }
//        	double delta = 1.2;
//            double scale = this.cube.getScaleX();
//
//            /*if (event.getDeltaY() < 0)*/ scale /= delta;
//            else scale *= delta;
//
//            scale = clamp(scale);
//            this.cube.setScaleX(scale);
//            this.cube.setScaleY(scale);
//
//            event.consume();
        });
        
        VBox control = new VBox();
        control.setSpacing(20);
        
        Label lbl = new Label("Control");
        lbl.setFont(Font.font("Helvetica", 40));
        lbl.setPrefWidth(250);
        lbl.setAlignment(Pos.CENTER);
        lbl.setUnderline(true);
        
        Label lbl_speed = new Label("Speed");
        lbl_speed.setPrefWidth(250);
        lbl_speed.setAlignment(Pos.CENTER);
        lbl.setFont(Font.font("Helvetica", 20));
        
        Label lbl_angle = new Label("Angle");
        lbl_angle.setPrefWidth(250);
        lbl_angle.setAlignment(Pos.CENTER);
        lbl.setFont(Font.font("Helvetica", 20));
        
        lbl_stroke.setText("Stroke : " + stroke);
        lbl_stroke.setPrefWidth(250);
        lbl_stroke.setAlignment(Pos.CENTER);
        lbl_stroke.setFont(Font.font("Helvetica", 20));
        
        Slider speed = new Slider(0.0, 100.0, 50.0);
        speed.setMaxWidth(250);
        speed.setShowTickLabels(true);
        speed.setShowTickMarks(true);
        speed.setMajorTickUnit(10);
        speed.setBlockIncrement(2);
        speed.valueProperty().addListener( 
                new ChangeListener<Number>() { 
     
               public void changed(ObservableValue <? extends Number >  
                         observable, Number oldValue, Number newValue) 
               { 
            	   	speed_value = (Double) newValue * PS.course.get_maximum_velocity()/100;
               
               } 
           }); 
        
        
        Slider angle = new Slider(0.0, 360, 180);
        angle.setMaxWidth(250);
        angle.setShowTickLabels(true);
        angle.setShowTickLabels(true);
        angle.setShowTickMarks(true);
        angle.setMajorTickUnit(30);
        angle.setBlockIncrement(5);
        angle.valueProperty().addListener( 
                new ChangeListener<Number>() { 
     
               public void changed(ObservableValue <? extends Number >  
                         observable, Number oldValue, Number newValue) 
               { 
            	   	angle_value = (Double) newValue;
               
               } 
           }); 
        
        Button btn_shot = new Button("Shot");
        btn_shot.setMinWidth(250);
        btn_shot.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event){
                System.out.println("Speed : " + speed_value + " Angke : " + angle_value);
            }
        });
        
        
        control.getChildren().add(lbl);
        control.getChildren().add(lbl_stroke);
        control.getChildren().add(lbl_speed);
        control.getChildren().add(speed);
        control.getChildren().add(lbl_angle);
        control.getChildren().add(angle);
        control.getChildren().add(btn_shot);
        
        HBox cubebox = new HBox();
        cubebox.getChildren().add(this.cube);
        
        VBox mainbox = new VBox();
        mainbox.getChildren().add(cubebox);
        mainbox.getChildren().add(control);
        
        //Scene scene = new Scene(this.cube, 800, 600, true, SceneAntialiasing.BALANCED);
        main.scene2 = new Scene(mainbox, 1200,800,true, SceneAntialiasing.BALANCED);
        main.scene2.setFill(Color.WHITE);
        main.scene2.setCamera(cam);

        main.scene2.setOnKeyPressed(t -> {        //Listener
            switch (t.getCode()){
                case LEFT: 
                    this.rotateY.setAngle(this.rotateY.getAngle() - 10); 
                    break;
                case RIGHT: 
                    this.rotateY.setAngle(this.rotateY.getAngle() + 10); 
                    break;
                case W: 
                    this.rotateX.setAngle(this.rotateX.getAngle() - 10); 
                    break;
                case S: 
                    this.rotateX.setAngle(this.rotateX.getAngle() + 10); 
                    break;
                case R:
                	System.out.println("Reset the angle");
                	this.rotateX.setAngle(0);
                	this.rotateY.setAngle(0);
                case ENTER:
                	System.out.println("Speed : " + speed_value + " Angle : "+ angle_value);
                	System.out.println("Stroke : " + stroke);
                	PS.take_angle_shot(speed_value, angle_value*Math.PI/180);
                	ballPosition();
                	stroke = PS.shot;
                	System.out.println("Stroke : " + stroke);
                	updateStrokeLabel();
            }
        });


        main.scene2.setOnMousePressed(event -> {
            anchorX = event.getSceneX();

            if(event.getClickCount() == 2){
                cube.setTranslateX(cube.getTranslateX() - ((event.getSceneX() - (main.scene2.getWidth() / 2)) * 0.5));
            }
        });

        main.scene2.setOnMouseDragged(event -> {
            rotateY.setAngle(rotateY.getAngle() + (anchorX - event.getSceneX()) / 250);
        });
        
        
        main.scene2.addEventHandler(ScrollEvent.SCROLL, event -> {
            final double delta = event.getDeltaY();
            final double translateZ = cube.getTranslateZ();
            final double minZoom = -900;
            final double maxZoom = -100;

            if (translateZ < minZoom) {
                if (delta > 0) {
                    cube.translateZProperty().set(translateZ + delta * 0.5);
                }
                return;
            }

            if (translateZ > maxZoom) {
                if (delta < 0) {
                    cube.translateZProperty().set(translateZ + delta * 0.5);
                }
                return;
            }

            cube.translateZProperty().set(translateZ + delta * 0.5);
        });
    }
    
    
    public void updateStrokeLabel() {
    	lbl_stroke.setText("Stroke : " + stroke);
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
    
    public void ballPosition() {
    	Vector2d ballpos = PS.get_ball_position();
        this.ball.setTranslateX(ballpos.get_x());
        this.ball.setTranslateZ(ballpos.get_y() /*- (ball_radius)*/);
        this.ball.setTranslateY(PS.course.get_height().evaluate(ballpos));
        System.out.println("ball updated : " + ballpos.toString());
    }
    private Group loadModel(URL url) {
        Group modelRoot = new Group();

        ObjModelImporter importer = new ObjModelImporter();
        importer.read(url);

        for (MeshView view : importer.getImport()) {
            modelRoot.getChildren().add(view);
        }

        return modelRoot;
    }

    //scale the zoom value
    private static double clamp(double value) { //value is the the current zoom value
        if (Double.compare(value, 0.1) < 0) return 0.1;
        if (Double.compare(value, 10.0) > 0) return 10.0;
        return value;
    }

}
