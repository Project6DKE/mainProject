package WorkingGameCrazyPutting.src.MainProject;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Graph3D extends Application {
    private final Rotate rotateY = new Rotate(-145, Rotate.Y_AXIS);
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Sphere ball;
    private Group cube;
    private int area = 5;
    private double max_height = 2.5;
    private double speed_value = 50;
    private double angle_value = 90;
    private int stroke = 0;
    private Label lbl_stroke = new Label();

    // public Game3D(UI ui){
    //     this.ui = ui;
    // }


    public Sphere getBall() {
        return ball;
    }

    public void start(Stage primaryStage) {
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


        for (double x = -area; x <= area; x+=((area*2)-0.00001)/((float)(size-1))) {  
            for (double y = -area; y <= area; y+=((area*2)-0.00001)/((float)(size-1))) {
                double z = Math.pow(x, 2) + y;  //insert here the function (height)
                if(z < -max_height){
                    z = -max_height;}  //limit so the different of height in the field is not too big
                if(z > max_height){ 
                    z = max_height;}    //limit so the different of height in the field is not too big
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

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(fieldMaterial);
        meshView.setCullFace(CullFace.NONE);
        meshView.setDrawMode(DrawMode.FILL);

        MeshView waterView = new MeshView(water);
        waterView.setMaterial(waterMaterial);
        waterView.setCullFace(CullFace.NONE);
        waterView.setDrawMode(DrawMode.FILL);

        this.cube.getChildren().addAll(meshView);
        this.cube.getChildren().addAll(waterView);
        
        this.cube.getChildren().add(this.ball);

        makeZoomable(this.cube);
        
        VBox control = new VBox();
        control.setSpacing(20);
        
        Label lbl = new Label("Control");
        lbl.setFont(Font.font("Helvetica", 40));
        lbl.setPrefWidth(200);
        lbl.setAlignment(Pos.CENTER);
        lbl.setUnderline(true);
        
        Label lbl_speed = new Label("Speed");
        lbl_speed.setPrefWidth(200);
        lbl_speed.setAlignment(Pos.CENTER);
        lbl.setFont(Font.font("Helvetica", 20));
        
        Label lbl_angle = new Label("Angle");
        lbl_angle.setPrefWidth(200);
        lbl_angle.setAlignment(Pos.CENTER);
        lbl.setFont(Font.font("Helvetica", 20));
        
        lbl_stroke.setText("Stroke : " + stroke);
        lbl_stroke.setPrefWidth(200);
        lbl_stroke.setAlignment(Pos.CENTER);
        lbl_stroke.setFont(Font.font("Helvetica", 20));
        
        Slider speed = new Slider(0.0, 100.0, 50.0);
        speed.setMaxWidth(200);
        speed.setShowTickLabels(true);
        speed.setShowTickMarks(true);
        speed.setMajorTickUnit(10);
        speed.setBlockIncrement(2);
        speed.valueProperty().addListener( 
                new ChangeListener<Number>() { 
     
               public void changed(ObservableValue <? extends Number >  
                         observable, Number oldValue, Number newValue) 
               { 
            	   	speed_value = (Double) newValue;
               
               } 
           }); 
        
        
        Slider angle = new Slider(0.0, 360, 180);
        angle.setMaxWidth(200);
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
        btn_shot.setMinWidth(200);
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
        
        VBox cubebox = new VBox();
        cubebox.getChildren().add(this.cube);
        
        VBox mainbox = new VBox();
        mainbox.getChildren().add(cubebox);
        mainbox.getChildren().add(control);
        

        
        Scene scene = new Scene(mainbox, 1000, 800, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.WHITE);
        scene.setCamera(new PerspectiveCamera());

        scene.setOnKeyPressed(t -> {        //Listener
            switch (t.getCode()){
                case LEFT: 
                    this.rotateY.setAngle(this.rotateY.getAngle() - 10); 
                    break;
                case RIGHT: 
                    this.rotateY.setAngle(this.rotateY.getAngle() + 10); 
                    break;
                //it s weird to rotate the field up or down so for the moment i commented it
//                case UP: 
//                    this.rotateX.setAngle(this.rotateX.getAngle() + 10); 
//                    break;
//                case DOWN: 
//                    this.rotateX.setAngle(this.rotateX.getAngle() - 10); 
//                    break;
                case ENTER:
                	stroke+=1;
                	System.out.println("Speed : " + speed_value + " Angle : "+ angle_value);
                	System.out.println("Stroke : " + stroke);
                	updateStrokeLabel();
                	}
        });

      

        primaryStage.setResizable(true);   //we should probably put false so the field is in the middle of the screen 
        primaryStage.setScene(scene);       
        primaryStage.show();
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
    
    public void updateStrokeLabel() {
    	lbl_stroke.setText("Stroke : " + stroke);
    }

    public static void main(String[] args) {
        launch(args);
    }
}