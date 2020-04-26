package MainProject;

import javafx.animation.*;
import javafx.application.Application;

import java.awt.*;
import java.io.File;
import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.geometry.NodeOrientation;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
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
import javafx.util.Duration;

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
    private double startX;
    private int direction = -1;
    private int skip = 0;


    public Game3D1(Main main, PuttingCourse PC){
        this.main = main;
        PS = new PuttingSimulator(PC, new EulerSolver());

        AudioClip music = new AudioClip(getClass().getResource("Sound/2sec_silent_intro_horn.wav").toExternalForm());
        music.setVolume(main.volume);
        music.play();

        AudioClip music2 = new AudioClip(getClass().getResource("Sound/13sec_silent_golf_music.wav").toExternalForm());
        music2.setVolume(main.volume);
        music2.play();

        createVisualization();
    }


    public Sphere getBall() {
        return ball;
    }

    public void createVisualization() {

        Group trees = createObject("trees", 1, 11, 10, 10);
        Group chicken = createObject("chicken", 0, 0, 0, 10);

        double translateGrassX = -50;
        double translateGrassY = 75;
        double translateGrassZ = 50;

        Group grass = createObject("bunchOfGrass", -30 + translateGrassX, translateGrassY, 50 + translateGrassZ, 3);
        Group grass2 = createObject("bunchOfGrass", -60 + translateGrassX, translateGrassY, 50 + translateGrassZ, 3);
        Group grass3 = createObject("bunchOfGrass", -30 + translateGrassX, translateGrassY, 70 + translateGrassZ, 3);
        Group grass4 = createObject("bunchOfGrass", -45 + translateGrassX, translateGrassY, 40 + translateGrassZ, 3);
        Group grass5 = createObject("bunchOfGrass", -10 + translateGrassX, translateGrassY, 50 + translateGrassZ, 3);
        Group grass6 = createObject("bunchOfGrass", -40 + translateGrassX, translateGrassY, 50 + translateGrassZ, 3);
        Group grass7 = createObject("bunchOfGrass", -35 + translateGrassX, translateGrassY, 40 + translateGrassZ, 3);

        Group flag = createObject("flag", 0, 0, 0, 30);
        Group arrow = createObject("arrow", 0, -40, 3.3, 5);

        arrow.getTransforms().add(new Rotate(180, Rotate.X_AXIS));

        cam = new PerspectiveCamera();
        cam.setNearClip(0.1);
        cam.setFarClip(100000.0);

        TranslateTransition transition2 = new TranslateTransition();
        transition2.setDuration(Duration.seconds(3));
        transition2.setFromY(800);
        transition2.setToY(1600);
        transition2.setAutoReverse(true);
        transition2.setCycleCount(1);
        transition2.setNode(cam);
        transition2.play();

        Group blenderObjects = new Group();
        blenderObjects.getChildren().addAll(arrow);

        blenderObjects.getChildren().addAll(grass);  blenderObjects.getChildren().addAll(grass2);
        blenderObjects.getChildren().addAll(grass3); blenderObjects.getChildren().addAll(grass4);
        blenderObjects.getChildren().addAll(grass5); blenderObjects.getChildren().addAll(grass6);
        blenderObjects.getChildren().addAll(grass7);


        PointLight pointLight1 = new PointLight();
        pointLight1.setColor(Color.GRAY);
        pointLight1.setTranslateY(pointLight1.getTranslateY() - 100);
        pointLight1.setOpacity(0.4);

        blenderObjects.getChildren().add(pointLight1);

        this.cube = new Group();
        cube.getChildren().addAll(blenderObjects);
        
        //cube.getChildren().add(chicken);
        //cube.getChildren().add(trees);
        cube.getChildren().add(flag);
        
        Vector2d flagpos = PS.course.get_flag_position();
        flag.getTransforms().add(new Translate(flagpos.get_x(), flagpos.get_y()-5, PS.course.get_height().evaluate(flagpos)));


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
                if(z<0) {
                    z = max_height;
                }

                // Maybe there is a better constant than 0.5 to detect water
                if (z > 0.5) {
                    mesh.getPoints().addAll(
                            (int) (x * 100),
                            (int) (z * 100),
                            (int) (y * 100));
                } else {
                    // Move below water mesh
                    mesh.getPoints().addAll(
                            (int) (x * 100),
                            (int) (z * 60),
                            (int) (y * 100));
                }


                water.getPoints().addAll(
                    (int)(x * 100), 
                    (int)(20),
                    (int)(y * 100));
            }
        }


        // texture
        addTextureMesh(mesh, size);
        addTextureMesh(water, size);

        // faces
        addFacesMesh(mesh, size);
        addFacesMesh(water, size);

        this.cube.getTransforms().addAll(this.rotateY);
        this.cube.getTransforms().addAll(this.rotateX);

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

        Group surface = new Group();

        AmbientLight pointLight2 = new AmbientLight();
        pointLight2.setTranslateY(-1000);
        surface.getChildren().add(pointLight2);

        surface.getChildren().addAll(meshView);
        surface.getChildren().addAll(waterView);

        surface.setRotate(180);

        this.cube.getChildren().addAll(surface);

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

        Label lbl = createStandardLabel("Control", 20, 250);
        lbl.setUnderline(true);
        Label lbl_speed = createStandardLabel("Speed", 20, 250);
        Label lbl_angle = createStandardLabel("Angle", 20, 250);
        String strokeString = "Stroke : " + stroke;
        lbl_stroke = createStandardLabel(strokeString, 20, 250);
        
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
                System.out.println("Speed : " + speed_value + " Angle : " + angle_value);
            }
        });

        
        
        control.getChildren().add(lbl);
        control.getChildren().add(lbl_stroke);
        control.getChildren().add(lbl_speed);
        control.getChildren().add(speed);
        control.getChildren().add(lbl_angle);
        control.getChildren().add(angle);
        control.getChildren().add(btn_shot);
        control.setAlignment(Pos.TOP_LEFT);
        control.setTranslateX(0);
        control.setTranslateY(0);

        
        HBox cubebox = new HBox();

        cube.setTranslateY(1900);
        this.cube.setTranslateX(400);
        cube.setTranslateZ(-100);

        control.setTranslateY(1300);


        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(-40);
        rotateTransition.setToAngle(0);
        rotateTransition.setDuration(Duration.seconds(5.5));
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(1);
        rotateTransition.setNode(cube);
        rotateTransition.play();

        cubebox.getChildren().add(this.cube);
        
        VBox mainbox = new VBox();

        mainbox.getChildren().add(cubebox);

        mainbox.getChildren().add(control);


        //Scene scene = new Scene(this.cube, 800, 600, true, SceneAntialiasing.BALANCED);



        main.scene2 = new Scene(mainbox, 1200,800,true, SceneAntialiasing.BALANCED);

        Stop[] stops = new Stop[] { new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.LIGHTYELLOW)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        main.scene2.setFill(lg1);
        main.scene2.setCamera(cam);



        // To rotate controls around the X and Y axis and to launch the ball
        keyboardControlListener();

        // To scale all object, for a zoom effect
        scrollListener();

        // To teleport by double clicking
        mousePressedListener();

        // To rotate all objects in the scene around the Y axis
        mouseDraggedListener();

    }

    public void keyboardControlListener() {
        main.scene2.setOnKeyPressed(t -> {
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
    }

    public void scrollListener() {
        main.scene2.addEventHandler(ScrollEvent.SCROLL, event -> {
            final double delta = event.getDeltaY();
            final double translateZ = cube.getTranslateZ();
            final double minZoom = -900, maxZoom = -100;

            // Functions constrains the zoom

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

    public void mousePressedListener() {
        main.scene2.setOnMousePressed(event -> {
            // Find starting point
            // To measure distance when starting to drag
            startX = event.getSceneX();

            if(event.getClickCount() == 2){
                cube.setTranslateX(cube.getTranslateX() - ((event.getSceneX() - (main.scene2.getWidth() / 2)) * 0.5));
            }
        });
    }

    public void mouseDraggedListener() {
        main.scene2.setOnMouseDragged(event -> {
            rotateY.setAngle(rotateY.getAngle() + (startX - event.getSceneX()) / 250);
        });
    }

    public Group createObject(String pathName, double x, double y, double z, double scalingFactor) {
        Group object = loadModel(getClass().getResource("Objects/" + pathName + ".obj"));
        object.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        object.getTransforms().add(new Scale(scalingFactor, scalingFactor, scalingFactor));
        object.getTransforms().add(new Translate(x, y, z));

        return object;
    }

    public Label createStandardLabel(String text, int size, int prefWidth) {
        Label label = new Label(text);
        label.setPrefWidth(prefWidth);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Helvetica", size));

        return label;
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
