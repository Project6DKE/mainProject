package MainProject;

import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class Game3D1 extends StackPane{
    private final Rotate rotateY = new Rotate(-145, Rotate.Y_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Sphere ball;
    private Group all3DObjects;
    private Main main;

    private double speedValue = 50;
    private double angleValue = 90;
    private int stroke = 0;

    private int speedInPercent = 0;

    private Label strokeLabel = new Label();
    private Label angleLabel = new Label();
    private Label speedLabel = new Label();

    private Camera cam;
    private PuttingSimulator PS;
    private final int ball_radius = 10;
    private double xPositionDragStarted;
    private double yPositionDragStarted;

    private int level;
    private int controlMode = 0;
    private Group surface = new Group();


    public Game3D1(Main main, PuttingCourse PC, int level) {
        this.main = main;
        this.level = level;
        PS = new PuttingSimulator(PC, new EulerSolver());

        playMusic();
        setCam();

        Course course = new Course(PS);
        surface = course.get();

        createVisualization();
        playIntroTransition();
    }

    private void playMusic() {
        GameMusic gameMusic = new GameMusic();
        gameMusic.playBackgroundMusic();
        gameMusic.playIntroMusic(level);
    }

    private void playIntroTransition() {
        IntroTransition introTransition = new IntroTransition(all3DObjects, cam);
        introTransition.play();
    }

    public Sphere getBall() {
        return ball;
    }

    public void createVisualization() {
        setAll3DObjects();

        VBox control = getControl();
        translateAll3DObjects();

        HBox cubebox = new HBox();
        cubebox.getChildren().add(all3DObjects);

        VBox mainbox = new VBox();
        mainbox.getChildren().add(cubebox);
        mainbox.getChildren().add(control);

        setScene(mainbox);
        addControlListeners();
    }

    private void setAll3DObjects() {
        Group flag = getObject("flag", 0, 2, 0, 30);

        Group blenderObjects = getBlenderObjects();

        all3DObjects = new Group();
        all3DObjects.getChildren().addAll(blenderObjects);
        all3DObjects.getChildren().add(flag);

        Vector2d flagpos = PS.getCourse().get_flag_position();
        flag.getTransforms().add(new Translate(flagpos.get_x(), flagpos.get_y() - 5, PS.getCourse().get_height().evaluate(flagpos)));


        Box obs = new Box(1, 1, 1);
        all3DObjects.getChildren().addAll(obs);
        all3DObjects.getTransforms().addAll(this.rotateY);
        all3DObjects.getTransforms().addAll(this.rotateX);

        ball = new Sphere();
        ball.setRadius(ball_radius);

        ballPosition();

        all3DObjects.getChildren().add(surface);
        all3DObjects.getChildren().add(ball);
    }



    private void translateAll3DObjects() {
        all3DObjects.setTranslateX(400);
        all3DObjects.setTranslateY(1800);
        all3DObjects.setTranslateZ(-100);
    }

    private void setCam() {
        cam = new PerspectiveCamera();
        cam.setNearClip(0.1);
        cam.setFarClip(100000.0);
    }

    private Group getBlenderObjects() {
        Group trees = getObject("trees", -18, 0.3, 13, 25);
        Group arrow = getObject("arrow", 0, -40, 3.3, 5);

        Group[] grassArray = getGrassArray();
        arrow.getTransforms().add(new Rotate(180, Rotate.X_AXIS));

        Group blenderObjects = new Group();
        blenderObjects.getChildren().addAll(arrow);
        blenderObjects.getChildren().addAll(trees);

        for (Group grassElement: grassArray) {
            blenderObjects.getChildren().addAll(grassElement);
        }

        PointLight pointLight = basicPointLight();

        blenderObjects.getChildren().add(pointLight);
        return blenderObjects;
    }

    private PointLight basicPointLight() {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.GRAY);
        pointLight.setTranslateY(pointLight.getTranslateY() - 100);
        pointLight.setOpacity(0.4);

        return pointLight;
    }

    private VBox getControl() {
        VBox control = new VBox();
        control.setSpacing(20);

        int labelSize = 20;
        int prefWidth = 250;

        speedLabel = createStandardLabel("Speed", labelSize, prefWidth);
        angleLabel = createStandardLabel("Angle", labelSize, prefWidth);
        String strokeString = "Stroke : " + stroke;
        strokeLabel = createStandardLabel(strokeString, labelSize, prefWidth);

        Node[] controlElements = {angleLabel, speedLabel, strokeLabel};

        for (Node node: controlElements) {
            control.getChildren().addAll(node);
        }

        control.setAlignment(Pos.TOP_LEFT);
        control.setTranslateX(0);
        control.setTranslateY(1300);
        return control;
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
        keyboardControlListener();
        detectZoomWithScroll();
        clickEvent();
        dragControl();
    }


    private void keyboardControlListener() {
        main.scene2.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case TAB:
                    controlMode += 1;

                    if (controlMode == 2) {
                        controlMode = 0;
                    }
                    break;
                case ENTER:
                    stroke += 1;
                    strokeLabel.setText("Stroke: " + stroke);

                    if (level == 0) {
                        System.out.println("Human move");
                    } else {
                        System.out.println("Bot move");
                    }
            }
        });
    }


    private void detectZoomWithScroll() {
        main.scene2.addEventHandler(ScrollEvent.SCROLL, event -> {
            final double scrollDirection = event.getDeltaY();
            final double translateZ = all3DObjects.getTranslateZ();
            final double minZoom = -900, maxZoom = -100;

            final double zoomingSpeed = 0.5;
            final double zoom = scrollDirection * zoomingSpeed;

            if (translateZ < minZoom) {
                if (scrollDirection > 0) {
                    all3DObjects.translateZProperty().set(translateZ + zoom);
                }
                return;
            }

            if (translateZ > maxZoom) {
                if (scrollDirection < 0) {
                    all3DObjects.translateZProperty().set(translateZ + zoom);
                }
                return;
            }

            all3DObjects.translateZProperty().set(translateZ + zoom);
        });
    }

    private void clickEvent() {
        main.scene2.setOnMousePressed(event -> {
            xPositionDragStarted = event.getSceneX();
            yPositionDragStarted = event.getSceneY();

            boolean doubleClick = event.getClickCount() == 2;

            if (doubleClick){
                moveObjectsToClickPositionOnXAxis(event);
            }
        });
    }

    private void moveObjectsToClickPositionOnXAxis(MouseEvent click) {
        all3DObjects.setTranslateX(all3DObjects.getTranslateX() - ((click.getSceneX() - (main.scene2.getWidth() / 2)) * 0.5));
    }

    private void dragControl() {
        main.scene2.setOnMouseDragged(dragEvent -> {
            switch (controlMode) {
                case 0:
                    rotateAroundYAxis(dragEvent);
                    double angle = formatAngle(rotateY.getAngle());
                    angleLabel.setText("Angle: " + angle + "deg");
                    break;
                case 1:
                    calculateSpeed(dragEvent);
                    speedLabel.setText("Speed: " + speedInPercent + "%");
                    break;
            }

        });
    }

    private double formatAngle(double _angle) {
        double angle = _angle;

        while (angle / 360 > 1) {
            angle -= 360;
        }

        while (angle / 360 < 0) {
            angle += 360;
        }

        return (int) angle;
    }

    private void rotateAroundYAxis(MouseEvent dragEvent) {
        rotateY.setAngle(rotateY.getAngle() + (xPositionDragStarted - dragEvent.getSceneX()) / 250);
    }

    private void calculateSpeed(MouseEvent dragEvent) {
        speedValue = speedValue + (yPositionDragStarted - dragEvent.getSceneY());

        if (speedValue > 30000) {
            speedValue = 30000;
        }

        if (speedValue < 0) {
            speedValue = 0;
        }

        speedInPercent = (int) ((speedValue / 30000) * 100);
    }

    private Group getGrass(double x, double y, double z) {
        double translateX = 60 + x;
        double translateY = 15 + y;
        double translateZ = 70 + z;

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
        strokeLabel.setText("Stroke : " + stroke);
    }

    private void ballPosition() {
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

    private void zoomOnObject(Group control) {  //control is the object we are zooming in
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
    private static double clamp(double zoomValue) {
        if (Double.compare(zoomValue, 0.1) < 0) {
            return 0.1;
        }
        if (Double.compare(zoomValue, 10.0) > 0) {
            return 10.0;
        }

        return zoomValue;
    }
}



