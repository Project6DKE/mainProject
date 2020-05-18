package MainProject;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.value.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.scene.*;

import javafx.application.Application;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import readingOfFunctions.*;

public class Main extends Application {
    public Stage primaryStage;
    public Scene gameMenu, main3DGame;
    public Game3D1 dim3;
    public double volume = 0.5;

    private MediaPlayer mediaPlayer;
    private HBox mainBox;
    private final int scene_width = 1500;
    private final int scene_height = 750;
    private GameType gameType;

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
    private MenuMusic musicPlayer;
    private PuttingCourse courseNew;
    private boolean newCourse;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Golf");
        
        newCourse = false;
        
        musicPlayer = new MenuMusic();
        musicPlayer.playBackgroundMusic();

        Group group = new Group();
        playGolf = false;

        gameMenu = new Scene(group, scene_width, scene_height, true);
        gameMenu.setFill(Color.LIGHTCORAL);
        group.getChildren().add(createView());

        primaryStage.setScene(gameMenu);
        primaryStage.show();
    }


    private HBox createView() throws Exception {
        mainBox = new HBox();
        mainBox.getChildren().add(createMenuView());
        mainBox.getChildren().add(introView());
        if (playGolf) {
            musicPlayer.stopBackgroundMusic();
            //Function2d height = new FunctionH("-0.01 * x + 0.003 * x ^ 2 + 0.04 * y");
            Function2d height= new FunctionH("0.02");

            Vector2d flag = new Vector2d(0, 3);
            Vector2d start = new Vector2d(0, 0);

            double g, m, mu, vmax, tol;
            g = 9.81;
            m = 45.93 / 1000;
            mu = 0.131;
            vmax = 3;
            tol = 0.2;

            PuttingCourse course = new PuttingCourse(height, flag, start, mu, vmax, tol, g, m);
            
            if(newCourse) {
            	try {
            	course = courseNew;
            	}
            	catch(NullPointerException e) {
            		System.out.println("Null pointer Exception caught. Went on the wrong place");
            	}
            }
            
            dim3 = new Game3D1(this, course, gameType);

            primaryStage.setScene(main3DGame);
            return mainBox;
        }


        return mainBox;
    }


    private VBox createMenuView(){
        VBox box = new VBox();
        box.setPrefWidth(200);
        box.setPrefHeight(scene_height);
        box.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
        
        Button addspace = new Button(" ");
        addspace.setPrefHeight(235);
        addspace.setVisible(false);

        Button play_btn = new Button("Play");
        play_btn.setFont(Font.font("Helvetica", 15));
        play_btn.setMinWidth(150);

        Button cg_btn = new Button("Course Generator");
        cg_btn.setFont(Font.font("Helvetica", 15));
        cg_btn.setMinWidth(150);

        Button setting_btn = new Button("Settings");
        setting_btn.setFont(Font.font("Helvetica", 15));
        setting_btn.setMinWidth(150);

        Button exit_btn = new Button("Exit");
        exit_btn.setFont(Font.font("Helvetica",15));
        exit_btn.setMinWidth(150);

        play_btn.setOnAction(new EventHandler<ActionEvent>(){
        
            public void handle(ActionEvent event) {
                musicPlayer.playClickSound();
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(playView());
            }
        });

        cg_btn.setOnAction(new EventHandler<ActionEvent>(){
        
            public void handle(ActionEvent event) {
                musicPlayer.playClickSound();
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(cgView());
            }
        });

        setting_btn.setOnAction(new EventHandler<ActionEvent>(){
        
            public void handle(ActionEvent event) {
                musicPlayer.playClickSound();
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
        iLabel.setUnderline(true);
        
        Label cLabel = new Label("By Husam, Kristian, Vladislav, Tiphanie, Nicolas, Thibault");
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

    private VBox botCreationView(){
        VBox mainBox = new VBox();
        mainBox.prefWidth(scene_width-200);

        HBox box1 = new HBox();
        box1.setPrefWidth(scene_width-200);
        HBox box2 = new HBox();
        box2.setPrefWidth(scene_width-200);
        HBox box3 = new HBox();
        box3.setPrefWidth(scene_width-200);

        Label title = new Label("Select difficulty");
        title.setFont(Font.font("Helvetica", 50));

        Label empty2 = new Label("");
        empty2.setFont(Font.font("Helvetica", 50));

        Button easyButton = new Button("Easy");
        easyButton.setFont(Font.font("Helvetica", 40));
        easyButton.setPrefWidth(500);

        Button mediumButton = new Button("Expert");
        mediumButton.setFont(Font.font("Helvetica", 40));
        mediumButton.setPrefWidth(500);

        Button hardButton = new Button("Impossible");
        hardButton.setFont(Font.font("Helvetica", 40));
        hardButton.setPrefWidth(500);

        box1.getChildren().add(easyButton);
        box1.setAlignment(Pos.CENTER);

        box2.getChildren().add(mediumButton);
        box2.setAlignment(Pos.CENTER);

        box3.getChildren().add(hardButton);
        box3.setAlignment(Pos.CENTER);

        mainBox.getChildren().add(title);
        mainBox.getChildren().add(empty2);
        mainBox.getChildren().add(box1);
        mainBox.getChildren().add(box2);
        mainBox.getChildren().add(box3);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(20);


        easyButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event){
                musicPlayer.playClickSound();
                gameType = GameType.EASY_BOT;
                playGolf = true;
                try {
					createView();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        mediumButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event){
                musicPlayer.playClickSound();
                gameType = GameType.MEDIUM_BOT;
                playGolf = true;
                try {
					createView();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        hardButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event){
                musicPlayer.playClickSound();
                gameType = GameType.HARD_BOT;
                playGolf = true;
                try {
					createView();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        return mainBox;
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
                musicPlayer.playClickSound();
                gameType = GameType.HUMAN;
                playGolf = true;
                try {
					createView();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        Button empty = new Button(" ");
        empty.setFont(Font.font("Helvetica", 100));
        empty.setVisible(false);

        Button AI_btn = new Button("Bot Play");
        AI_btn.setFont(Font.font("Helvetica", 40));
        AI_btn.setPrefWidth(500);

        AI_btn.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {
                musicPlayer.playClickSound();
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(botCreationView());
            }
        });


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
                musicPlayer.playClickSound();
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
                try {
					courseNew = new PuttingCourse(new FunctionH(functionV), new Vector2d(goalXV,goalYV), new Vector2d(startXV,startYV),frictionV,ballSpeedV,holeDistV,gravityV,massV);
					newCourse = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 
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
                volume = (double) newValue;
                musicPlayer.setMusicVolume(volume);
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
