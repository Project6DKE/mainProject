package MainProject;

import java.net.URL;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.transform.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.*;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.geometry.Pos;

public class Game3D1 extends StackPane{
    private final Rotate rotateY = new Rotate(-55, Rotate.Y_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);

    private Sphere ball;
    private Group flag;

    private Group all3DObjects;
    private final Main main;

    private double speedValue = 10000;
    private int speedInPercent = 33;

    private int stroke = 0;

    private Label strokeLabel = new Label();
    private Label angleLabel = new Label();
    private Label speedLabel = new Label();

    private Camera cam;
    private final PuttingSimulator PS;
    private final int ball_radius = 10;

    private double xPositionDragStarted;
    private double yPositionDragStarted;

    private final GameType gameType;
    private controlType controlMode = controlType.ANGLE;

    private enum controlType {ANGLE, SPEED};
    private Group surface;


    public Game3D1(Main main, PuttingCourse PC, GameType gameType) {
        this.main = main;
        this.gameType = gameType;
        PS = new PuttingSimulator(PC, new RungeKutta());

        playMusic();
        setCam();
        setSurface();

        createVisualization();
        playIntroTransition();
    }


    public Sphere getBall() {
        return ball;
    }

    private void playMusic() {
        GameMusic gameMusic = new GameMusic();
        gameMusic.playBackgroundMusic();
        gameMusic.playIntroMusic(gameType);
    }

    private void playIntroTransition() {
        IntroTransition introTransition = new IntroTransition(all3DObjects, cam);
        introTransition.play();
    }

    private void setSurface() {
        Course course = new Course(PS);
        surface = course.get();
    }

    private void createVisualization() {
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
        ObjectType flagType = ObjectType.FLAG;
        flagType.setTranslate(0, 2, 0);

        flag = getObject(flagType);
        setFlagPosition();

        Group blenderObjects = getBlenderObjects();

        all3DObjects = new Group();
        all3DObjects.getChildren().addAll(blenderObjects);
        all3DObjects.getChildren().add(flag);


        all3DObjects.getTransforms().add(rotateY);
        all3DObjects.getTransforms().add(rotateX);

        setBall();

        all3DObjects.getChildren().add(surface);
        all3DObjects.getChildren().add(ball);
    }

    private void setFlagPosition() {
        Vector2d flagPosition = PS.getCourse().get_flag_position();
        double positionX = flagPosition.get_x();
        double positionY = flagPosition.get_y() - 5;
        double positionZ = PS.getCourse().get_height().evaluate(flagPosition);

        Translate translate = new Translate(positionX, positionY, positionZ);

        flag.getTransforms().add(translate);
    }

    private void setBall() {
        ball = new Sphere();
        ball.setRadius(ball_radius);
        setBallPosition();
    }


    private void translateAll3DObjects() {
        all3DObjects.setTranslateX(400);
        all3DObjects.setTranslateY(1800);
        all3DObjects.setTranslateZ(-100);
    }

    private void setCam() {
        double nearClip = 0.1;
        double farClip = 100000.0;

        cam = new PerspectiveCamera();
        cam.setNearClip(nearClip);
        cam.setFarClip(farClip);
    }

    private Group getBlenderObjects() {
        Group trees = getTrees();
        Group arrow = getArrow();

        Group blenderObjects = new Group();
        blenderObjects.getChildren().add(arrow);
        blenderObjects.getChildren().add(trees);

        Group[] grassArray = getGrassArray();

        for (Group grassElement: grassArray) {
            blenderObjects.getChildren().add(grassElement);
        }

        PointLight pointLight = basicPointLight();

        blenderObjects.getChildren().add(pointLight);
        return blenderObjects;
    }

    private Group getTrees() {
        ObjectType treesType = ObjectType.TREES;
        treesType.setTranslate(-18, 1, 13);

        return getObject(treesType);
    }

    private Group getArrow() {
        ObjectType arrowType = ObjectType.ARROW;
        arrowType.setTranslate(0, -40, 3.3);
        Group arrow = getObject(arrowType);
        arrow.getTransforms().add(new Rotate(180, Rotate.X_AXIS));

        return arrow;
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

        String speedText = "Speed: " + speedInPercent + "%";
        speedLabel = createStandardLabel(speedText, labelSize, prefWidth);

        double angle = formatAngle(rotateY.getAngle());
        String angleText = "Angle: " + angle + "°";
        angleLabel = createStandardLabel(angleText, labelSize, prefWidth);

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
        main.main3DGame = new Scene(mainbox, 1200,800,true, SceneAntialiasing.BALANCED);
        setBackground();
        main.main3DGame.setCamera(cam);
    }

    private void setBackground() {
        Stop[] stops = new Stop[] { new Stop(0, Color.LIGHTBLUE), new Stop(1, Color.LIGHTYELLOW)};
        LinearGradient lg1 = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);

        main.main3DGame.setFill(lg1);
    }

    private void addControlListeners() {
        keyboardControlListener();
        detectZoomWithScroll();
        clickEvent();
        dragControl();
    }


    private void keyboardControlListener() {
        main.main3DGame.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case SHIFT:
                    controlMode = controlType.SPEED;
                    break;
                case ENTER:
                    playGame();
                    break;
            }
        });

        main.main3DGame.setOnKeyReleased(keyEvent -> {
            final boolean shiftReleased = !keyEvent.isShiftDown();

            if (shiftReleased) {
                controlMode = controlType.ANGLE;
            }
        });
    }

    private void playGame() {
        updateStrokeLabel();

        switch (gameType) {
            case HUMAN:
                System.out.println("Human game");
                break;
            case EASY_BOT:
                System.out.println("Easy bot");
                //Kristian : i guess i will put the GA here
                GA genAlgo = new GA(PS);
                double[] shot = new double [2];
                shot = genAlgo.runGA();
                PS.take_angle_shot(shot[0], shot[1]);
                break;
            case MEDIUM_BOT:
                System.out.println("Medium bot");
                break;
            case HARD_BOT:
                System.out.println("Hard bot");
                break;
        }
    }

    private void detectZoomWithScroll() {
        main.main3DGame.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            final double minZoom = -900;
            final double maxZoom = -100;

            final double translateZ = all3DObjects.getTranslateZ();
            final double zoom = getZoom(scrollEvent);

            final boolean outOfMinZoomBound = translateZ < minZoom;
            final boolean returningBackFromMinZoomBound = zoom > 0;

            if (outOfMinZoomBound && returningBackFromMinZoomBound) {
                applyZoom(zoom);
                return;
            }

            final boolean outOfMaxZoomBound = translateZ > maxZoom;
            final boolean returningBackFromMaxZoomBound = zoom > 0;

            if (outOfMaxZoomBound && returningBackFromMaxZoomBound) {
                applyZoom(zoom);
                return;
            }

            applyZoom(zoom);
        });
    }

    private double getZoom(ScrollEvent scrollEvent) {
        final double scrollDirection = scrollEvent.getDeltaY();
        final double zoomingSpeed = 0.5;

        return scrollDirection * zoomingSpeed;
    }

    private void applyZoom(double zoom) {
        final double translateZ = all3DObjects.getTranslateZ();
        all3DObjects.translateZProperty().set(translateZ + zoom);
    }

    private void clickEvent() {
        main.main3DGame.setOnMousePressed(event -> {
            xPositionDragStarted = event.getSceneX();
            yPositionDragStarted = event.getSceneY();

            boolean doubleClick = event.getClickCount() == 2;

            if (doubleClick){
                moveObjectsToClickPositionOnXAxis(event);
            }
        });
    }

    private void moveObjectsToClickPositionOnXAxis(MouseEvent click) {
        double centerOfScreen = main.main3DGame.getWidth() / 2;
        double differenceWithCenterOfScreen = click.getSceneX() - centerOfScreen;
        double motionNeutraliser = 0.5;

        double suggestedTranslation = differenceWithCenterOfScreen * motionNeutraliser;
        double currentPosition = all3DObjects.getTranslateX();

        double clickTransformation = currentPosition - suggestedTranslation;
        all3DObjects.setTranslateX(clickTransformation);
    }

    private void dragControl() {
        main.main3DGame.setOnMouseDragged(this::updateSpeedAndAngleLabels);
    }

    private void updateSpeedAndAngleLabels(MouseEvent dragEvent) {
        switch (controlMode) {
            case ANGLE:
                rotateAroundYAxis(dragEvent);
                double angle = formatAngle(rotateY.getAngle());
                angleLabel.setText("Angle: " + angle + "°");
                break;
            case SPEED:
                calculateSpeed(dragEvent);
                speedLabel.setText("Speed: " + speedInPercent + "%");
                break;
        }
    }

    private double formatAngle(double angle) {
        double numberOfFullRotations = angle / 360;
        double fullRotation = 360;

        if (numberOfFullRotations > 1) {
            angle -= fullRotation;
        }

        if (numberOfFullRotations < 0) {
            angle += fullRotation;
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

        ObjectType grassType = ObjectType.GRASS;
        grassType.setTranslate(translateX, translateY, translateZ);

        return getObject(grassType);
    }

    private Group getObject(ObjectType objectType) {
        String pathName = objectType.getPathName();
        Group object = loadModel(getClass().getResource("Objects/" + pathName + ".obj"));

        double scaling = objectType.getScalingFactor();
        object.getTransforms().add(new Scale(scaling, scaling, scaling));

        object.getTransforms().add(new Rotate(0, Rotate.Y_AXIS));
        object.getTransforms().add(objectType.getTranslate());

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
        stroke += 1;
        strokeLabel.setText("Stroke : " + stroke);
    }

    private void setBallPosition() {
        Vector2d ballPosition = PS.get_ball_position();
        this.ball.setTranslateX(ballPosition.get_x());
        this.ball.setTranslateZ(ballPosition.get_y() /*- (ball_radius)*/);
        this.ball.setTranslateY(PS.getCourse().get_height().evaluate(ballPosition));
        System.out.println("Ball updated : " + ballPosition.toString());
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



