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
import javafx.scene.input.MouseEvent;
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
    private Group all3DObjects;
    private Main main;
    private int area = 5;
    private double speed_value = 50;
    private double angle_value = 90;
    private int stroke = 0;
    private Label lbl_stroke = new Label();
    private Camera cam;
    private PuttingSimulator PS;
    private final int ball_radius = 10;
    private double xPositionDragStarted;
    private int level;

    private TriangleMesh field = new TriangleMesh();
    private TriangleMesh water = new TriangleMesh();
    private Group surface = new Group();
    private final int resolution = 100;


    public Game3D1(Main main, PuttingCourse PC, int level) {
        this.main = main;
        this.level = level;
        PS = new PuttingSimulator(PC, new EulerSolver());

        playMusic();
        createVisualization();
    }

    private void playMusic() {
        GameMusic gameMusic = new GameMusic();
        gameMusic.playBackgroundMusic();
        gameMusic.playIntroMusic(level);
    }


    public Sphere getBall() {
        return ball;
    }

    public void createVisualization() {
        setCam();
        doDescendingIntroTransition();
        setAll3DObjects();

        VBox control = getControl();
        translateAll3DObjects();

        HBox cubebox = new HBox();
        cubebox.getChildren().add(all3DObjects);

        VBox mainbox = new VBox();
        mainbox.getChildren().add(cubebox);
        mainbox.getChildren().add(control);

        doRotateIntroTransition();
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
        setSurface();

        all3DObjects.getChildren().add(surface);
        all3DObjects.getChildren().add(ball);
    }

    private void setSurface() {
        generateSurface();
        addTextures();
        addFaces();
        getMaterials();

        surface.setRotate(180);
    }

    private void addTextures() {
        addTextureMesh(field);
        addTextureMesh(water);
    }

    private void addTextureMesh(TriangleMesh mesh) {
        for (float x = 0; x < resolution - 1; x++) {
            for (float y = 0; y < resolution - 1; y++) {
                float x0 = x / (float) resolution;
                float y0 = y / (float) resolution;
                float x1 = (x + 1) / (float) resolution;
                float y1 = (y + 1) / (float) resolution;

                mesh.getTexCoords().addAll(
                        x1, y1,
                        x1, y0,
                        x0, y1,
                        x0, y0
                );
            }
        }
    }

    private void addFaces() {
        addFacesMesh(field);
        addFacesMesh(water);
    }

    private void addFacesMesh(TriangleMesh mesh) {
        for (int x = 0; x < resolution - 1; x++) {
            for (int z = 0; z < resolution - 1; z++) {
                int p0 = x * resolution + z;
                int p1 = x * resolution + z + 1;
                int p2 = (x + 1) * resolution + z;
                int p3 = (x + 1) * resolution + z + 1;

                mesh.getFaces().addAll(p2, 0, p1, 0, p0, 0);
                mesh.getFaces().addAll(p2, 0, p3, 0, p1, 0);
            }
        }
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

        Label lbl = createStandardLabel("Control", labelSize, prefWidth);
        lbl.setUnderline(true);
        Label lbl_speed = createStandardLabel("Speed", labelSize, prefWidth);
        Label lbl_angle = createStandardLabel("Angle", labelSize, prefWidth);
        String strokeString = "Stroke : " + stroke;
        lbl_stroke = createStandardLabel(strokeString, labelSize, prefWidth);

        Slider speed = getSpeedSlider();
        Slider angle = getAngleSlider();

        Button btn_shot = new Button("Shot");


        btn_shot.setMinWidth(prefWidth);
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
        control.setTranslateY(1300);
        return control;
    }

    private void getMaterials() {
        Color grassColor = Color.GREEN;
        Color waterColor = Color.BLUE;

        PhongMaterial fieldMaterial = getMaterialWithColor(grassColor);
        PhongMaterial waterMaterial = getMaterialWithColor(waterColor);

        MeshView waterView = getMeshView(water, waterMaterial);
        MeshView meshView = getMeshView(field, fieldMaterial);

        AmbientLight ambientLight = basicAmbientLight();

        surface.getChildren().add(ambientLight);
        surface.getChildren().add(meshView);
        surface.getChildren().add(waterView);
    }

    private AmbientLight basicAmbientLight() {
        AmbientLight ambientLight = new AmbientLight();
        ambientLight.setTranslateY(-1000);

        return ambientLight;
    }

    private PhongMaterial getMaterialWithColor(Color color) {
        PhongMaterial material = new PhongMaterial();
        material.setSpecularColor(color);
        material.setDiffuseColor(color);

        return material;
    }

    private MeshView getMeshView(TriangleMesh surface, PhongMaterial material) {
        MeshView meshView = new MeshView(surface);
        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);
        return meshView;
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


    private void generateSurface() {
        double stepSize = ((area * 2)) / ((float) (resolution));

        for (double x = -area; x <= area; x += stepSize) {
            for (double y = -area; y <= area; y += stepSize) {

                double z = PS.getCourse().get_height().evaluate(new Vector2d(x, y));
                addPointsToMeshes(x, y, z);
            }
        }
    }

    private void addPointsToMeshes(double x, double y, double z) {
        if (z > 0.01) {
            field.getPoints().addAll(
                    (int) (x * 100),
                    (int) (z * 200),
                    (int) (y * 100));
        } else {
            field.getPoints().addAll(
                    (int) (x * 100),
                    (int) (-0.01),
                    (int) (y * 100));
        }

        water.getPoints().addAll(
                (int) (x * 100),
                (int) (0),
                (int) (y * 100));
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
        keyboardControlListener();
        detectZoomWithScroll();
        teleportAfterDoubleClick();
        rotateWhenDragging();
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
                case ENTER:
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
        rotateTransition.setFromAngle(45);
        rotateTransition.setToAngle(90);
        rotateTransition.setDuration(Duration.seconds(5.5));
        rotateTransition.setAutoReverse(true);
        rotateTransition.setCycleCount(1);
        rotateTransition.setNode(all3DObjects);
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

    private void teleportAfterDoubleClick() {
        main.scene2.setOnMousePressed(event -> {
            xPositionDragStarted = event.getSceneX();
            boolean doubleClick = event.getClickCount() == 2;

            if (doubleClick){
                moveObjectsToClickPositionOnXAxis(event);
            }
        });
    }

    private void moveObjectsToClickPositionOnXAxis(MouseEvent click) {
        all3DObjects.setTranslateX(all3DObjects.getTranslateX() - ((click.getSceneX() - (main.scene2.getWidth() / 2)) * 0.5));
    }

    private void rotateWhenDragging() {
        main.scene2.setOnMouseDragged(this::rotateAroundYAxis);
    }

    private void rotateAroundYAxis(MouseEvent dragEvent) {
        rotateY.setAngle(rotateY.getAngle() + (xPositionDragStarted - dragEvent.getSceneX()) / 250);
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
        lbl_stroke.setText("Stroke : " + stroke);
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
