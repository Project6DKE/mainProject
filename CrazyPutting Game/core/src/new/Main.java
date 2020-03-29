import java.io.File;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

public class Main extends Application {
    public Stage primaryStage;
    public Scene scene, scene2;
    private MediaPlayer mediaPlayer;
    private HBox mainBox;
    private final int scene_width = 1500;
    private final int scene_height = 750;
    private static Main thismain;
    public Game3D1 dim3;
    private double massV;
    private double frictionV;
    private double holeDistV;
    private int startXV;
    private int startYV;
    private String fileName;
    private double ballSpeedV;
    private int goalXV;
    private int goalYV;
    private String functionV;
    private double gravityV;
    private boolean playGolf;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Golf");
        //String musicFile = "golf_music_full.mp3";     // For example
        //Media sound = new Media(new File(musicFile).toURI().toString());
        //mediaPlayer = new MediaPlayer(sound);
        //mediaPlayer.setVolume(0.5);
        //mediaPlayer.setAutoPlay(true);
        //mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        Group group = new Group();
        playGolf = false;
        scene = new Scene(group, scene_width, scene_height, true);
        scene.setFill(Color.LIGHTCORAL);
        group.getChildren().add(createView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createView(){
        mainBox = new HBox();
        mainBox.getChildren().add(createMenuView());
        mainBox.getChildren().add(introView());
        if(playGolf){
            dim3 = new Game3D1(this);
            primaryStage.setScene(scene2);
            return mainBox;
        }
        return mainBox;
    }

    private VBox createMenuView(){
        VBox box = new VBox();
        box.setPrefWidth(250);
        box.setPrefHeight(scene_height);
        //box.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
        BackgroundImage backgroundTexture= new BackgroundImage(new Image("/new/images/leaves.jpg",100,75,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        box.setBackground(new Background(backgroundTexture));

        Button addspace = new Button(" ");
        addspace.setPrefHeight(235);
        addspace.setVisible(false);


        Button play_btn = newImageButton("play", 50, 130, "000000");

        Button cg_btn = newImageButton("course", 40, 170, "000000");

        Button setting_btn = newImageButton("settings", 40, 185, "000000");

        Button exit_btn = newImageButton("exit", 50, 130, "000000");

        play_btn.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(playView());
            }
        });

        cg_btn.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(cgView());
            }
        });

        setting_btn.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(settingView());
            }
        });

        exit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event){
                primaryStage.hide();
            }
        });

        VBox main_btn = new VBox();
        main_btn.setAlignment(Pos.CENTER);
        main_btn.setSpacing(20);

        main_btn.getChildren().add(addspace);
        main_btn.getChildren().add(play_btn);
        main_btn.getChildren().add(cg_btn);
        main_btn.getChildren().add(setting_btn);
        main_btn.getChildren().add(exit_btn);
        box.getChildren().add(main_btn);


        return box;
    }

    private VBox introView(){
        VBox box = new VBox();

        Button empty = new Button(" ");
        empty.setMinHeight(100);
        empty.setVisible(false);

        Label iLabel = new Label("Welcome to group 6 project");
        iLabel.setFont(Font.font("Verdana", 50));

        Label cLabel = new Label("By Husam, Kristian, Vladislav, Tiphanie, Nicolás, Thibault");
        cLabel.setFont(Font.font("Verdana",FontPosture.ITALIC, 30));

        VBox box2 = new VBox();
        box2.setPrefWidth(scene_width-200);
        box2.getChildren().add(empty);
        box2.getChildren().add(iLabel);
        box2.getChildren().add(cLabel);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(30);

        box.getChildren().add(box2);

        return box;

    }

    private VBox playView(){
        VBox box1 = new VBox();
        box1.prefWidth(scene_width-200);
        HBox box = new HBox();
        box.setPrefWidth(scene_width-200);

        Label title = new Label("Select mode");
        title.setFont(Font.font("Helvetica", 50));

        Label empty2 = new Label("");
        empty2.setFont(Font.font("Helvetica", 50));

        Button human_btn = new Button("Human player");
        human_btn.setFont(Font.font("Helvetica", 40));
        human_btn.setPrefWidth(500);

        human_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event){
                playGolf = true;
                createView();
            }
        });

        Button empty = new Button(" ");
        empty.setFont(Font.font("Helvetica", 100));
        empty.setVisible(false);

        Button AI_btn = new Button("Bot Play");
        AI_btn.setFont(Font.font("Helvetica", 40));
        AI_btn.setPrefWidth(500);

        box.getChildren().add(human_btn);
        box.getChildren().add(empty);
        box.getChildren().add(AI_btn);
        box.setAlignment(Pos.CENTER);

        box1.getChildren().add(title);
        box1.getChildren().add(empty2);
        box1.getChildren().add(box);
        box1.setAlignment(Pos.CENTER);

        return box1;
    }

    //setprompttext
    private VBox cgView(){
        VBox mainbox1 = new VBox();
        HBox box = new HBox();
        box.setPrefWidth(scene_width-200);
        VBox emptybox = new VBox();
        emptybox.setPrefWidth(120);
        VBox box1 = new VBox();
        box1.setPrefWidth(200);
        VBox box2 = new VBox();
        box2.setPrefWidth(300);
        VBox box3 = new VBox();
        box3.setPrefWidth(200);
        VBox box4 = new VBox();
        box4.setPrefWidth(300);

        Label title = new Label("Course generator");
        title.setFont(Font.font("Helvetica", 50));
        Label empty2 = new Label(" ");
        empty2.setFont(Font.font("Helvetica", 50));

        Label mass = new Label("   Mass");
        mass.setFont(Font.font("Helvetica", 20));
        Label friction = new Label("   Friction");
        friction.setFont(Font.font("Helvetica", 20));
        Label hole_dist = new Label("   Hole distance");
        hole_dist.setFont(Font.font("Helvetica", 20));
        Label startX = new Label("   Starting X axis");
        startX.setFont(Font.font("Helvetica", 20));
        Label startY = new Label("   Starting Y axis");
        startY.setFont(Font.font("Helvetica", 20));
        Label file_name = new Label("   File name");
        file_name.setFont(Font.font("Helvetica", 20));
        box1.getChildren().add(mass);
        box1.getChildren().add(friction);
        box1.getChildren().add(hole_dist);
        box1.getChildren().add(startX);
        box1.getChildren().add(startY);
        box1.getChildren().add(file_name);
        box1.setAlignment(Pos.CENTER_LEFT);
        box1.setSpacing(30);

        TextField massT = new TextField();
        massT.setPromptText("Enter mass");
        TextField frictionT = new TextField();
        frictionT.setPromptText("Enter friction");
        TextField hole_distT = new TextField();
        hole_distT.setPromptText("Enter hole distance");
        TextField startXT = new TextField();
        startXT.setPromptText("Enter start X");
        TextField startYT = new TextField();
        startYT.setPromptText("Enter start Y");
        TextField filenameT = new TextField();
        filenameT.setPromptText("Enter file name");
        box2.getChildren().add(massT);
        box2.getChildren().add(frictionT);
        box2.getChildren().add(hole_distT);
        box2.getChildren().add(startXT);
        box2.getChildren().add(startYT);
        box2.getChildren().add(filenameT);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(30);

        Label ballspeed = new Label("   Ball speed");
        ballspeed.setFont(Font.font("Helvetica", 20));
        Label goalX = new Label("   Goal X axis");
        goalX.setFont(Font.font("Helvetica", 20));
        Label goalY = new Label("   Goal Y axis");
        goalY.setFont(Font.font("Helvetica", 20));
        Label function = new Label("   Function");
        function.setFont(Font.font("Helvetica", 20));
        Label gravity = new Label("   Gravity");
        gravity.setFont(Font.font("Helvetica", 20));
        Label empty = new Label(" ");
        empty.setFont(Font.font("Helvetica", 20));
        box3.getChildren().add(ballspeed);
        box3.getChildren().add(goalX);
        box3.getChildren().add(goalY);
        box3.getChildren().add(function);
        box3.getChildren().add(gravity);
        box3.getChildren().add(empty);
        box3.setAlignment(Pos.CENTER_LEFT);
        box3.setSpacing(30);

        TextField ballT = new TextField();
        ballT.setPromptText("Enter ball speed");
        TextField goalXT = new TextField();
        goalXT.setPromptText("Enter goal X");
        TextField goalYT = new TextField();
        goalYT.setPromptText("Enter goal Y");
        TextField functionT = new TextField();
        functionT.setPromptText("Enter function");
        TextField gravityT = new TextField();
        gravityT.setPromptText("Enter gravity");
        Button run = new Button("run");
        run.setFont(Font.font("Helvetica", 20));
        run.setPrefWidth(200);
        box4.getChildren().add(ballT);
        box4.getChildren().add(goalXT);
        box4.getChildren().add(goalYT);
        box4.getChildren().add(functionT);
        box4.getChildren().add(gravityT);
        box4.getChildren().add(run);
        box4.setAlignment(Pos.CENTER);
        box4.setSpacing(30);

        run.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                massV = Double.parseDouble(massT.getText());
                frictionV = Double.parseDouble(frictionT.getText());
                holeDistV = Double.parseDouble(hole_distT.getText());
                startXV = Integer.parseInt(startXT.getText());
                startYV = Integer.parseInt(startYT.getText());
                fileName = filenameT.getText();
                ballSpeedV = Double.parseDouble(ballT.getText());
                goalXV = Integer.parseInt(goalXT.getText());
                goalYV = Integer.parseInt(goalYT.getText());
                functionV = functionT.getText();
                gravityV = Double.parseDouble(gravityT.getText());
                System.out.println(massV);
                System.out.println(frictionV);
                System.out.println(holeDistV);
                System.out.println(startXV);
                System.out.println(startYV);
                System.out.println(fileName);
                System.out.println(ballSpeedV);
                System.out.println(goalXV);
                System.out.println(goalYV);
                System.out.println(functionV);
                System.out.println(gravityV);
            }
        });

        box.getChildren().add(emptybox);
        box.getChildren().add(box1);
        box.getChildren().add(box2);
        box.getChildren().add(box3);
        box.getChildren().add(box4);

        mainbox1.getChildren().add(title);
        mainbox1.getChildren().add(empty2);
        mainbox1.getChildren().add(box);
        mainbox1.setAlignment(Pos.CENTER);
        return mainbox1;
    }

    private Button newImageButton(String imageName, int h, int w, String backgroundColor) {
        Image image = new Image("/new/images/" + imageName + ".png");
        ImageView newImageView = new ImageView(image);
        newImageView.setFitHeight(h); newImageView.setFitWidth(w);
        Button newButton = new Button("", newImageView);
        newButton.setStyle("-fx-background-color: #" + backgroundColor + ";");

        return newButton;
    }

    private VBox settingView(){
        VBox box = new VBox();

        Button empty = new Button(" ");
        empty.setMinHeight(100);
        empty.setVisible(false);

        Button empty2 = new Button(" ");
        empty2.setMinHeight(100);
        empty2.setVisible(false);

        Label title = new Label("Settings");
        title.setFont(Font.font("Helvetica", 50));

        Label slider_title = new Label("Music volume");
        slider_title.setFont(Font.font("Helvetica", 20));

        Slider musicvol = new Slider(0.0, 1.0, 0.5);
        musicvol.setMaxWidth(200);
        musicvol.setShowTickLabels(true);
        musicvol.setShowTickMarks(true);
        musicvol.setMajorTickUnit(0.25);
        musicvol.setBlockIncrement(0.1);
        musicvol.valueProperty().addListener(
                new ChangeListener<Number>() {

                    public void changed(ObservableValue <? extends Number >
                                                observable, Number oldValue, Number newValue)
                    {

                        //System.out.println("value: " + newValue);
                        //mediaPlayer.setVolume((double) newValue);
                    }
                });


        VBox box2 = new VBox();
        box2.setPrefWidth(scene_width-200);
        box2.getChildren().add(empty);
        box2.getChildren().add(empty2);
        box2.getChildren().add(title);
        box2.getChildren().add(slider_title);
        box2.getChildren().add(musicvol);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(20);

        box.getChildren().add(box2);

        return box;
    }

    public HBox getmainBox(){
        return mainBox;
    }

    public static void main(String[] args){
        launch(args);
    }

}
