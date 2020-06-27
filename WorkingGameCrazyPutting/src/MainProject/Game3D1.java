package MainProject;

import java.util.ArrayList;
import java.util.Iterator;

import botFolder.GA;
import javafx.animation.AnimationTimer;
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

    private double speedValue = 20000;
    private int speed = 66;

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
    private boolean ballMoving = false;

    private final PuttingCourse puttingCourse;


    public Game3D1(Main main, PuttingCourse PC, GameType gameType, Solver solver) {
        this.main = main;
        this.gameType = gameType;
        this.puttingCourse = PC;
        PS = new PuttingSimulator(PC, new RungeKutta());
        PS.setSolver(solver);

        playMusic();
        setCam();
        setSurface();

        createVisualization();
        playIntroTransition();
    }
    
    public void setWaterPenalty(boolean t) {
    	PS.set_water_penalty(t);
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

        HBox container3D = new HBox();
        container3D.getChildren().add(all3DObjects);

        VBox mainbox = new VBox();
        mainbox.getChildren().add(container3D);
        mainbox.getChildren().add(control);

        setScene(mainbox);
        addControlListeners();
    }

    private void setAll3DObjects() {
        ObjectType flagType = ObjectType.FLAG;
        flagType.setTranslate(0, 2, 0);

        flag = new BlenderModel(flagType).get();
        setFlagPosition();

        Group blenderObjects = new DefaultBlenderObjects().get();

        all3DObjects = new Group();
        
        all3DObjects.getChildren().addAll(blenderObjects);
        all3DObjects.getChildren().add(flag);

        all3DObjects.getTransforms().add(rotateY);
        all3DObjects.getTransforms().add(rotateX);

        setBall();

        all3DObjects.getChildren().add(surface);
        all3DObjects.getChildren().add(ball);
    }

    private void setWalls() {
        for (Wall wall: puttingCourse.get_walls()) {
            setWall(wall);
        }

        Wall wall = new Wall(new Vector2d(2, 3), new Vector2d(-100, 300));
        setWall(wall);
    }


    private void setWall(Wall wall) {
        Vector2d midpoint = wall.getMidpoint();
        double width = wall.getWidth();
        double rotation = wall.getRotationInDegrees();
        System.out.println(rotation);

        Box wallObject = new Box();
        double height = 50;
        wallObject.setHeight(height);
        wallObject.setDepth(50);
        wallObject.setWidth(width);

        double moveUpHalfHeight = -(height / 2);
        double courseHeight = PS.getCourse().get_height().evaluate(midpoint) ;
        double yTranslation = courseHeight + moveUpHalfHeight;

        Rotate rotationTransform = new Rotate(rotation * 0.5, Rotate.Y_AXIS);
        Translate moveUpTranslation = new Translate(midpoint.x, yTranslation, midpoint.y);

        wallObject.getTransforms().add(moveUpTranslation);
        wallObject.getTransforms().add(rotationTransform);

        all3DObjects.getChildren().add(wallObject);

    }

    private void setFlagPosition() {
        Vector2d flagPosition = PS.getCourse().get_flag_position();
        double positionX = flagPosition.get_x();
        double positionY = PS.getCourse().get_height().evaluate(flagPosition) - 4;
        double positionZ = flagPosition.get_y() - 3;

        Translate translate = new Translate(positionX, positionY, positionZ);

        flag.getTransforms().add(translate);
    }

    private void setBall() {
        ball = new Sphere();
        ball.setRadius(ball_radius);
        setBallPosition();
    }

    private void setBallPosition() {
        Vector2d ballPosition = PS.take_angle_shot_list(0, 0).get(0);
        this.ball.setTranslateX(ballPosition.get_x() * -50);
        this.ball.setTranslateZ(ballPosition.get_y() * -50);
        this.ball.setTranslateY(PS.getCourse().get_height().evaluate(ballPosition) - 9);
        System.out.println("Ball updated : " + ballPosition.toString());
    }


    private void translateAll3DObjects() {
        all3DObjects.setTranslateX(0);
        all3DObjects.setTranslateY(1800);
        all3DObjects.setTranslateZ(-720);
    }

    private void setCam() {
        double nearClip = 0.1;
        double farClip = 100000.0;

        cam = new PerspectiveCamera();
        cam.setNearClip(nearClip);
        cam.setFarClip(farClip);
    }

    private VBox getControl() {
        VBox control = new VBox();
        control.setSpacing(20);

        int labelSize = 20;
        int prefWidth = 250;

        String speedText = "Speed: " + speed + "%";
        speedLabel = createStandardLabel(speedText, labelSize, prefWidth);

        double angle = formatAngle(rotateY.getAngle());
        String angleText = "Angle: " + angle + "°";
        angleLabel = createStandardLabel(angleText, labelSize, prefWidth);

        String strokeString = "Stroke : " + stroke;
        strokeLabel = createStandardLabel(strokeString, labelSize, prefWidth);

        control.getChildren().addAll(angleLabel, speedLabel, strokeLabel);

        control.setAlignment(Pos.TOP_LEFT);
        control.setTranslateX(0);
        control.setTranslateY(1300);
        return control;
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
            if (!ballMoving) {
                switch (keyEvent.getCode()) {
                    case SHIFT:
                        controlMode = controlType.SPEED;
                        break;
                    case ENTER:
                        playGame();
                        setBallPosition();
                        break;
                }
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
                playHuman();
                break;
            case EASY_BOT:
                System.out.println("Easy bot");
                playGeneticAlgorithm();
                break;
            case MEDIUM_BOT:
                System.out.println("Medium bot");
                break;
            case HARD_BOT:
                System.out.println("Hard bot");
                break;
        }
    }

    private void playHuman() {
        double angle = rotateY.getAngle();
        setBallPosition();

        double maxVelocity = puttingCourse.get_maximum_velocity();
        double speedInPercents = ((double) speed) / 100;

        double speed = speedInPercents * maxVelocity;
        double formattedAngle = formatAngle(angle);

        ArrayList<Vector2d> arrayList = PS.take_angle_shot_list(speed, formattedAngle);

        animationTimer(arrayList);
    }

    public void animationTimer(ArrayList<Vector2d> array) {
        Iterator<Vector2d> iterator = array.iterator();

        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                if (iterator.hasNext()) {
                    Vector2d nextValue = iterator.next();
                    double x = nextValue.get_x() * 50;
                    double y = nextValue.get_y() * 50;
                    nextValue.get_scalar();

                    ball.setTranslateX(-x);
                    ball.setTranslateZ(-y);

                    ballMoving = true;
                } else {
                    ballMoving = false;
                }
            }
        };

        timer.start();
    }

    private void playGeneticAlgorithm() {
        GA genAlgo = new GA(PS);
        double[] shot;
        Vector2d ballP = PS.get_ball_position();

        shot = genAlgo.runGA(ballP);
        ArrayList<Vector2d> arrayList = PS.take_angle_shot_list(shot[0], shot[1]);
        animationTimer(arrayList);
        setBallPosition();
    }


    private void detectZoomWithScroll() {
        main.main3DGame.addEventHandler(ScrollEvent.SCROLL, scrollEvent -> {
            final double minZoom = -1100;
            final double maxZoom = -700;

            final double translateZ = all3DObjects.getTranslateZ();

            final double zoom = getZoom(scrollEvent);

            final boolean outOfMinZoomBound = translateZ < minZoom;

            final boolean returningBackFromMinZoomBound = zoom > 0;

            if (outOfMinZoomBound) {
                if (returningBackFromMinZoomBound) {
                    applyZoom(zoom);
                }
                return;
            }

            final boolean outOfMaxZoomBound = translateZ > maxZoom;
            final boolean returningBackFromMaxZoomBound = zoom < 0;

            if (outOfMaxZoomBound) {
                if (returningBackFromMaxZoomBound) {
                    applyZoom(zoom);
                }
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
                speedLabel.setText("Speed: " + speed + "%");
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

        speed = (int) ((speedValue / 30000) * 100);
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
}


