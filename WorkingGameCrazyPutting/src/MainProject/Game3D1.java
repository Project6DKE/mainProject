package MainProject;

import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
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
    private double xPositionDragStarted;
    private int level;



    public Game3D1(Main main, PuttingCourse PC, int level) {
        this.main = main;
        this.level = level;
        PS = new PuttingSimulator(PC, new EulerSolver());

        GameMusic gameMusic = new GameMusic();
        gameMusic.playBackgroundMusic();
        gameMusic.playIntroMusic(level);
        createVisualization();
    }


    public Sphere getBall() {
        return ball;
    }

    public void createVisualization() {
        Group flag = getObject("flag", 0, 0, 0, 30);

        Group blenderObjects = getBlenderObjects();

        this.cube = new Group();
        cube.getChildren().addAll(blenderObjects);
        cube.getChildren().add(flag);

        setCam();

        doDescendingIntroTransition();


        Vector2d flagpos = PS.getCourse().get_flag_position();
        flag.getTransforms().add(new Translate(flagpos.get_x(), flagpos.get_y() - 5, PS.getCourse().get_height().evaluate(flagpos)));


        Box obs = new Box(1, 1, 1);
        this.cube.getChildren().addAll(obs);
        this.cube.getTransforms().addAll(this.rotateY);
        this.cube.getTransforms().addAll(this.rotateX);

        this.ball = new Sphere();
        this.ball.setRadius(ball_radius);
//        this.ball.setTranslateZ(200);
//        this.ball.setTranslateX(200);
//        this.ball.setTranslateY(240);

        ballPosition();

        int scalingFactor = 5;

        TriangularSurface triangularSurface = generateSurface(scalingFactor);

        // texture
        addTextureMesh(triangularSurface.mesh, scalingFactor);
        addTextureMesh(triangularSurface.water, scalingFactor);

        // faces
        addFacesMesh(triangularSurface.mesh, scalingFactor);
        addFacesMesh(triangularSurface.water, scalingFactor);

        Group surface = getMaterials(triangularSurface);
        surface.setRotate(180);

        this.cube.getChildren().addAll(surface);
        this.cube.getChildren().add(this.ball);

        //makeZoomable(this.cube);
        this.cube.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case O:
                    double delta = 1.2;
                    double scale = this.cube.getScaleX();
                    scale /= delta;
                    scale = clamp(scale);
                    this.cube.setScaleX(scale);
                    this.cube.setScaleY(scale);
                    event.consume();
                    break;
            }
        });


        VBox control = getControl();

        cube.setTranslateX(400);
        cube.setTranslateY(1800);
        cube.setTranslateZ(-100);

        HBox cubebox = new HBox();
        cubebox.getChildren().add(this.cube);

        VBox mainbox = new VBox();
        mainbox.getChildren().addAll(cubebox, control);

        doRotateIntroTransition();
        //Scene scene = new Scene(this.cube, 800, 600, true, SceneAntialiasing.BALANCED);

        setScene(mainbox);
        addControlListeners();
    }

    private void setCam() {
        cam = new PerspectiveCamera();
        cam.setNearClip(0.1);
        cam.setFarClip(100000.0);
    }

    private Group getBlenderObjects() {
        Group trees = getObject("trees", 20, 1, 13, 25);
        Group arrow = getObject("arrow", 0, -40, 3.3, 5);

        Group[] grassArray = getGrassArray();
        arrow.getTransforms().add(new Rotate(180, Rotate.X_AXIS));

        Group blenderObjects = new Group();
        blenderObjects.getChildren().addAll(arrow);
        blenderObjects.getChildren().addAll(trees);

        for (Group grassElement: grassArray) {
            blenderObjects.getChildren().addAll(grassElement);
        }

        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.GRAY);
        pointLight.setTranslateY(pointLight.getTranslateY() - 100);
        pointLight.setOpacity(0.4);

        blenderObjects.getChildren().add(pointLight);
        return blenderObjects;
    }

    private VBox getControl() {
        VBox control = new VBox();
        control.setSpacing(20);

        Label lbl = createStandardLabel("Control", 20, 250);
        lbl.setUnderline(true);
        Label lbl_speed = createStandardLabel("Speed", 20, 250);
        Label lbl_angle = createStandardLabel("Angle", 20, 250);
        String strokeString = "Stroke : " + stroke;
        lbl_stroke = createStandardLabel(strokeString, 20, 250);

        Slider speed = getSpeedSlider();
        Slider angle = getAngleSlider();

        Button btn_shot = new Button("Shot");


        btn_shot.setMinWidth(250);
        btn_shot.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Speed : " + speed_value + " Angle : " + angle_value);
            }
        });

        Node[] controlElements = {lbl, lbl_stroke, lbl_speed, speed, lbl_angle,
                angle, btn_shot};

        for (Node node: controlElements) {
            control.getChildren().addAll(node);
        }

        control.setAlignment(Pos.TOP_LEFT);
        control.setTranslateX(0);
        control.setTranslateY(0);

        control.setTranslateY(1300);
        return control;
    }

    private Group getMaterials(TriangularSurface triangularSurface) {
        PhongMaterial fieldMaterial = new PhongMaterial();  //color
        fieldMaterial.setSpecularColor(Color.GREEN);
        fieldMaterial.setDiffuseColor(Color.GREEN);

        PhongMaterial waterMaterial = new PhongMaterial();  //color
        waterMaterial.setSpecularColor(Color.BLUE);
        waterMaterial.setDiffuseColor(Color.BLUE);

        MeshView waterView = new MeshView(triangularSurface.water);
        waterView.setMaterial(waterMaterial);
        waterView.setCullFace(CullFace.NONE);
        waterView.setDrawMode(DrawMode.FILL);

        MeshView meshView = new MeshView(triangularSurface.mesh);
        meshView.setMaterial(fieldMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        Group surface = new Group();

        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setTranslateY(-1000);
        surface.getChildren().add(ambientLight);

        surface.getChildren().addAll(meshView);
        surface.getChildren().addAll(waterView);

        return surface;
    }

    private Group[] getGrassArray() {
        Group grass = getGrass(-30, 0, 50);
        Group grass2 = getGrass(-60, 0, 50);
        Group grass3 = getGrass(-30, 0, 70);
        Group grass4 = getGrass(-45, 0, 40);
        Group grass5 = getGrass(-10, 0, 50);
        Group grass6 = getGrass(-40, 0, 50);
        Group grass7 = getGrass(-35, 0, 40);

        Group[] grassArray = {grass, grass2, grass3, grass4, grass5, grass6, grass7};

        return grassArray;
    }


    private TriangularSurface generateSurface(int size) {
        TriangleMesh mesh = new TriangleMesh();
        TriangleMesh water = new TriangleMesh();

        for (double x = -area; x <= area; x += ((area * 2) - 0.0001) / ((float) (size - 1))) {
            for (double y = -area; y <= area; y += ((area * 2) - 0.0001) / ((float) (size - 1))) {

                double z = PS.getCourse().get_height().evaluate(new Vector2d(x, y));
                if (z < -max_height) {
                    z = -max_height;     //limit so the different of height in the field is not too big
                }
                if (z > max_height) {
                    z = max_height;    //limit so the different of height in the field is not too big
                }
                if (z < 0) {
                    z = max_height;
                    System.out.println(z);
                }

                // Maybe there is a better constant than 0.5 to detect water
                if (z > 0.5) {
                    mesh.getPoints().addAll(
                            (int) (x * 100),
                            (int) (z * 100),
                            (int) (y * 100));
                } else {
                    mesh.getPoints().addAll(
                            (int) (x * 100),
                            (int) (-0.99),
                            (int) (y * 100));
                }


                water.getPoints().addAll(
                        (int) (x * 100),
                        (int) (0),
                        (int) (y * 100));
            }
        }

        TriangularSurface surface = new TriangularSurface();
        surface.water = water;
        surface.mesh = mesh;

        return surface;
    }

    private Slider getAngleSlider() {
        Slider angleSlider = new Slider(0.0, 360, 180);
        angleSlider.setMaxWidth(250);
        angleSlider.setShowTickLabels(true);
        angleSlider.setShowTickMarks(true);
        angleSlider.setMajorTickUnit(30);
        angleSlider.setBlockIncrement(5);
        angleSlider.valueProperty().addListener(
                new ChangeListener<Number>() {

                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        angle_value = (Double) newValue;

                    }
                });
        return angleSlider;
    }

    private Slider getSpeedSlider() {
        Slider speedSlider = new Slider(0.0, 100.0, 50.0);
        speedSlider.setMaxWidth(250);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(10);
        speedSlider.setBlockIncrement(2);
        speedSlider.valueProperty().addListener(
                new ChangeListener<Number>() {

                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        speed_value = (Double) newValue * PS.getCourse().get_maximum_velocity() / 100;

                    }
                });
        return speedSlider;
    }

    private void setScene(VBox mainbox) {
        main.scene2 = new Scene(mainbox, 1200,800,true, SceneAntialiasing.BALANCED);
        setBackground();
        main.scene2.setCamera(cam);
    }

    private void setBackground() {
        Stop[] stops = new Stop[] { new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.LIGHTYELLOW)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        main.scene2.setFill(lg1);
    }

    private void addControlListeners() {
        // To rotate controls around the X and Y axis and to launch the ball
        keyboardControlListener();

        // To scale all object, for a zoom effect
        scrollListener();

        // To teleport by double clicking
        mousePressedListener();

        // To rotate all objects in the scene around the Y axis
        mouseDraggedListener();
    }


    private void keyboardControlListener() {
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
                case P:
                    if (level == 0) {
                        System.out.println("Speed : " + speed_value + " Angle : " + angle_value);
                        System.out.println("Stroke : " + stroke);
                        PS.take_angle_shot(speed_value, angle_value * Math.PI / 180);
                        ballPosition();
                        stroke = PS.shot;
                        System.out.println("Stroke : " + stroke);
                        updateStrokeLabel();
                    } else {
                        System.out.println("Bot move");
                    }
            }
        });
    }

    private void doRotateIntroTransition() {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(-40);
        rotateTransition.setToAngle(0);
        rotateTransition.setDuration(Duration.seconds(5.5));
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(1);
        rotateTransition.setNode(cube);
        rotateTransition.play();
    }

    private void doDescendingIntroTransition() {
        TranslateTransition descendingIntroTransition = new TranslateTransition();
        descendingIntroTransition.setDuration(Duration.seconds(3));
        descendingIntroTransition.setFromY(800);
        descendingIntroTransition.setToY(1600);
        descendingIntroTransition.setAutoReverse(true);
        descendingIntroTransition.setCycleCount(1);
        descendingIntroTransition.setNode(cam);
        descendingIntroTransition.play();
    }

    private void scrollListener() {
        main.scene2.addEventHandler(ScrollEvent.SCROLL, event -> {
            final double scrollDirection = event.getDeltaY();
            final double translateZ = cube.getTranslateZ();
            final double minZoom = -900, maxZoom = -100;

            // Functions constrains the zoom

            if (translateZ < minZoom) {
                if (scrollDirection > 0) {
                    cube.translateZProperty().set(translateZ + scrollDirection * 0.5);
                }
                return;
            }

            if (translateZ > maxZoom) {
                if (scrollDirection < 0) {
                    cube.translateZProperty().set(translateZ + scrollDirection * 0.5);
                }
                return;
            }

            cube.translateZProperty().set(translateZ + scrollDirection * 0.5);
        });
    }

    private void mousePressedListener() {
        main.scene2.setOnMousePressed(event -> {
            // Find starting point
            // To measure distance when starting to drag
            xPositionDragStarted = event.getSceneX();

            if(event.getClickCount() == 2){
                cube.setTranslateX(cube.getTranslateX() - ((event.getSceneX() - (main.scene2.getWidth() / 2)) * 0.5));
            }
        });
    }

    private void mouseDraggedListener() {
        main.scene2.setOnMouseDragged(event -> {
            rotateY.setAngle(rotateY.getAngle() + (xPositionDragStarted - event.getSceneX()) / 250);
        });
    }

    private Group getGrass(double x, double y, double z) {
        double translateX = -50 + x;
        double translateY = 75 + y;
        double translateZ = 50 + z;

        int scalingFactor = 3;

        Group grassObject = getObject("bunchOfGrass", translateX, translateY, translateZ, scalingFactor);
        return grassObject;
    }

    private Group getObject(String pathName, double x, double y, double z, double scalingFactor) {
        Group object = loadModel(getClass().getResource("Objects/" + pathName + ".obj"));
        object.getTransforms().add(new Rotate(90, Rotate.Y_AXIS));
        object.getTransforms().add(new Scale(scalingFactor, scalingFactor, scalingFactor));
        object.getTransforms().add(new Translate(x, y, z));

        return object;
    }

    private Label createStandardLabel(String text, int size, int prefWidth) {
        Label label = new Label(text);
        label.setPrefWidth(prefWidth);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Helvetica", size));

        return label;
    }


    private void updateStrokeLabel() {
        lbl_stroke.setText("Stroke : " + stroke);
    }

    private static void addTextureMesh(TriangleMesh mesh, int size) {
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

    private static void addFacesMesh(TriangleMesh mesh, int size) {
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
        this.ball.setTranslateY(PS.getCourse().get_height().evaluate(ballpos));
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

class TriangularSurface {
    public TriangleMesh water;
    public TriangleMesh mesh;
}
