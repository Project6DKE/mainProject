package MainProject;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.beans.value.*;
import javafx.scene.text.*;
import javafx.event.*;
import javafx.scene.*;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.stage.Stage;

import readingOfFunctions.*;

public class Main extends Application {
    public Stage primaryStage;
    public Scene gameMenu, main3DGame;
    public Game3D1 dim3;
    public double volume = 0.5;

    private HBox mainBox;
    private final int scene_width = 1500;
    private final int scene_height = 750;
    private GameType gameType;

    String prefFontFamily = "Helvetica";
    Font smallFont = Font.font(prefFontFamily, 15);
    Font basicFont = Font.font(prefFontFamily, 20);
    Font mediumFont = Font.font(prefFontFamily, 40);
    Font bigFont = Font.font(prefFontFamily, 50);

    final int margin = 200;
    final int prefBoxLength = scene_width - margin;

    private boolean playGolf;
    private MenuMusic musicPlayer;
    private PuttingCourse courseNew;
    private boolean newCourse;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GolfAcademy Pro");

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
            Function2d height = new FunctionH("0.02");

            Vector2d flag = new Vector2d(0, 3);
            Vector2d start = new Vector2d(0, 0);

            double g, m, mu, vmax, tol;
            g = 9.81;
            m = 45.93 / 1000;
            mu = 0.131;
            vmax = 3;
            tol = 0.2;

            PuttingCourse course = new PuttingCourse(height, flag, start, mu, vmax, tol, g, m);

            if (newCourse) {
                try {
                    course = courseNew;
                } catch (NullPointerException e) {
                    System.out.println("Null pointer Exception caught. Went on the wrong place");
                }
            }

            dim3 = new Game3D1(this, course, gameType);

            primaryStage.setScene(main3DGame);
            return mainBox;
        }


        return mainBox;
    }


    private VBox createMenuView() {
        VBox sideMenu = new VBox();
        sideMenu.setPrefWidth(200);
        sideMenu.setPrefHeight(scene_height);
        sideMenu.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));

        Button space = new Button(" ");
        space.setPrefHeight(235);
        space.setVisible(false);

        int standardMinWidth = 150;

        Button playBtn = new Button("Play");
        playBtn.setFont(smallFont);
        playBtn.setMinWidth(standardMinWidth);

        Button courseGeneratorBtn = new Button("Course Generator");
        courseGeneratorBtn.setFont(smallFont);
        courseGeneratorBtn.setMinWidth(standardMinWidth);

        Button settingsBtn = new Button("Settings");
        settingsBtn.setFont(smallFont);
        settingsBtn.setMinWidth(standardMinWidth);

        Button exitBtn = new Button("Exit");
        exitBtn.setFont(smallFont);
        exitBtn.setMinWidth(150);

        playBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                changeView(playView());
            }
        });

        courseGeneratorBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                changeView(cgView());
            }
        });

        settingsBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                changeView(settingsView());
            }
        });

        exitBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                primaryStage.hide();
            }
        });

        VBox frontPageBox = new VBox();
        frontPageBox.setAlignment(Pos.CENTER);
        frontPageBox.setSpacing(20);

        frontPageBox.getChildren().addAll(space, playBtn, courseGeneratorBtn, settingsBtn, exitBtn);
        sideMenu.getChildren().add(frontPageBox);


        return sideMenu;
    }

    private void changeView(VBox view) {
        musicPlayer.playClickSound();
        mainBox.getChildren().remove(1);
        mainBox.getChildren().add(view);
    }

    private VBox introView() {
        VBox box = new VBox();

        Button empty = new Button(" ");
        empty.setMinHeight(100);
        empty.setVisible(false);

        Label header = new Label("Welcome to group 6 project");
        header.setFont(Font.font("Verdana", 50));
        header.setUnderline(true);

        Label subHeader = new Label("By Husam, Kristian, Vladislav, Tiphanie, Nicolas, Thibault");
        subHeader.setFont(Font.font("Verdana", FontPosture.ITALIC, 30));

        VBox box2 = new VBox();
        box2.getChildren().addAll(empty, header, subHeader);
        box2.setPrefWidth(prefBoxLength);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(30);

        box.getChildren().add(box2);

        return box;

    }

    private VBox botCreationView() {
        VBox mainBox = new VBox();

        mainBox.prefWidth(prefBoxLength);

        HBox box1 = new HBox();
        box1.setPrefWidth(prefBoxLength);
        HBox box2 = new HBox();
        box2.setPrefWidth(prefBoxLength);
        HBox box3 = new HBox();
        box3.setPrefWidth(prefBoxLength);


        Label title = new Label("Select difficulty");
        title.setFont(bigFont);

        Label empty = new Label("");
        empty.setFont(bigFont);

        int buttonWidth = 500;

        Button easyButton = new Button("Easy");
        easyButton.setFont(mediumFont);
        easyButton.setPrefWidth(buttonWidth);

        Button mediumButton = new Button("Expert");
        mediumButton.setFont(mediumFont);
        mediumButton.setPrefWidth(buttonWidth);

        Button hardButton = new Button("Impossible");
        hardButton.setFont(mediumFont);
        hardButton.setPrefWidth(buttonWidth);

        box1.getChildren().add(easyButton);
        box1.setAlignment(Pos.CENTER);

        box2.getChildren().add(mediumButton);
        box2.setAlignment(Pos.CENTER);

        box3.getChildren().add(hardButton);
        box3.setAlignment(Pos.CENTER);

        mainBox.getChildren().addAll(title, empty, box1,
                                     box2, box3);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setSpacing(20);


        easyButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
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

            public void handle(ActionEvent event) {
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

            public void handle(ActionEvent event) {
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

    private VBox playView() {
        VBox box1 = new VBox();

        box1.prefWidth(prefBoxLength);
        HBox box = new HBox();
        box.setPrefWidth(prefBoxLength);

        Label title = new Label("Select mode");
        title.setFont(bigFont);

        Label empty2 = new Label("");
        empty2.setFont(bigFont);

        int buttonLength = 500;

        Button humanBtn = new Button("Human player");
        humanBtn.setFont(mediumFont);
        humanBtn.setPrefWidth(buttonLength);

        humanBtn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
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
        AI_btn.setFont(mediumFont);
        AI_btn.setPrefWidth(buttonLength);

        AI_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                musicPlayer.playClickSound();
                mainBox.getChildren().remove(1);
                mainBox.getChildren().add(botCreationView());
            }
        });


        box.getChildren().addAll(humanBtn, empty, AI_btn);
        box.setAlignment(Pos.CENTER);

        box1.getChildren().addAll(title, empty2, box);
        box1.setAlignment(Pos.CENTER);

        return box1;
    }

    private VBox cgView() {
        VBox mainbox1 = new VBox();
        HBox box = new HBox();
        box.setPrefWidth(prefBoxLength);

        VBox emptyBox = new VBox();
        emptyBox.setPrefWidth(120);

        VBox box1 = new VBox();
        box1.setPrefWidth(200);
        VBox box2 = new VBox();
        box2.setPrefWidth(300);
        VBox box3 = new VBox();
        box3.setPrefWidth(200);
        VBox box4 = new VBox();
        box4.setPrefWidth(300);

        Label title = new Label("Course generator");
        title.setFont(bigFont);
        Label empty2 = new Label(" ");
        empty2.setFont(bigFont);

        String whiteSpace = "   ";
        String[] courseParameters = {"mass", "friction", "starting Y axis", "starting X axis",
                                     "file name"};


        for (String parameter: courseParameters) {
            String labelText = whiteSpace + capitalize(parameter);
            Label label = new Label(labelText);
            label.setFont(basicFont);
            label.setMinHeight(42);
            box1.getChildren().add(label);
        }


        box1.setAlignment(Pos.CENTER_LEFT);
        box1.setSpacing(30);

        TextField massField = new TextField();
        massField.setPromptText("Enter mass");
        TextField frictionField = new TextField();
        frictionField.setPromptText("Enter friction");
        TextField holeDistanceField = new TextField();
        holeDistanceField.setPromptText("Enter hole distance");
        TextField startXField = new TextField();
        startXField.setPromptText("Enter start X");
        TextField startYField = new TextField();
        startYField.setPromptText("Enter start Y");
        TextField filenameField = new TextField();
        filenameField.setPromptText("Enter file name");
        box2.getChildren().addAll(massField, frictionField, holeDistanceField,
                               startXField, startYField, filenameField);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(30);

        String[] courseParameters2 = {"ball speed", "goal X axis", "goal Y axis", "function ",
                "gravity"};

        for (String parameter: courseParameters2) {
            String labelText = whiteSpace + capitalize(parameter);
            Label label = new Label(labelText);
            label.setFont(basicFont);
            label.setMinHeight(42);
            box3.getChildren().add(label);
        }

        box3.setAlignment(Pos.CENTER_LEFT);
        box3.setSpacing(30);

        TextField ballSpeedField = new TextField();
        ballSpeedField.setPromptText("Enter ball speed");
        TextField goalXField = new TextField();
        goalXField.setPromptText("Enter goal X");
        TextField goalYField = new TextField();
        goalYField.setPromptText("Enter goal Y");
        TextField functionField = new TextField();
        functionField.setPromptText("Enter function");
        TextField gravityField = new TextField();
        gravityField.setPromptText("Enter gravity");
        Button run = new Button("run");
        run.setFont(basicFont);
        run.setPrefWidth(200);
        box4.getChildren().addAll(ballSpeedField, goalXField, goalYField,
                                  functionField, gravityField, run);
        box4.setAlignment(Pos.CENTER);
        box4.setSpacing(30);

        run.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                musicPlayer.playClickSound();

                // TODO connect this directly the PuttingCourse instead of the CourseData class
                CourseData.mass = Double.parseDouble(massField.getText());
                CourseData.friction = Double.parseDouble(frictionField.getText());
                CourseData.holeDist = Double.parseDouble(holeDistanceField.getText());
                CourseData.startX = Integer.parseInt(startXField.getText());
                CourseData.startY = Integer.parseInt(startYField.getText());
                CourseData.fileName = filenameField.getText();
                CourseData.ballSpeed = Double.parseDouble(ballSpeedField.getText());
                CourseData.goalX = Integer.parseInt(goalXField.getText());
                CourseData.goalY = Integer.parseInt(goalYField.getText());
                CourseData.function = functionField.getText();
                CourseData.gravity = Double.parseDouble(gravityField.getText());

                CourseData.printAllData();

                try {
                    courseNew = new PuttingCourse(new FunctionH(CourseData.function), new Vector2d(CourseData.goalX, CourseData.goalY),
                            new Vector2d(CourseData.startX, CourseData.startY), CourseData.friction, CourseData.ballSpeed,
                            CourseData.holeDist, CourseData.gravity, CourseData.mass);
                    newCourse = true;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

        box.getChildren().addAll(emptyBox, box1, box2, box3, box4);

        mainbox1.getChildren().addAll(title, empty2, box);
        mainbox1.setAlignment(Pos.CENTER);
        return mainbox1;
    }

    private String capitalize(String text) {
        return text.substring(0,1).toUpperCase() + text.substring(1, text.length());
    }

    private VBox settingsView() {
        VBox box = new VBox();

        int standardMinButtonHeight = 100;

        Button empty = new Button(" ");
        empty.setMinHeight(standardMinButtonHeight);
        empty.setVisible(false);

        Button empty2 = new Button(" ");
        empty2.setMinHeight(standardMinButtonHeight);
        empty2.setVisible(false);

        Label title = new Label("Settings");
        title.setFont(bigFont);

        Label sliderTitle = new Label("Music volume");
        sliderTitle.setFont(basicFont);

        Slider musicVolumeSlider = new Slider(0.0, 1.0, 0.5);
        musicVolumeSlider.setMaxWidth(200);
        musicVolumeSlider.setShowTickLabels(true);
        musicVolumeSlider.setShowTickMarks(true);
        musicVolumeSlider.setMajorTickUnit(0.25);
        musicVolumeSlider.setBlockIncrement(0.1);
        musicVolumeSlider.valueProperty().addListener(
                new ChangeListener<Number>() {

                    public void changed(ObservableValue<? extends Number>
                                                observable, Number oldValue, Number newValue) {
                        volume = (double) newValue;
                        musicPlayer.setMusicVolume(volume);
                    }
                });


        VBox box2 = new VBox();
        box2.setPrefWidth(prefBoxLength);
        box2.getChildren().addAll(empty, empty2, title, sliderTitle, musicVolumeSlider);
        box2.setAlignment(Pos.CENTER);
        box2.setSpacing(20);

        box.getChildren().add(box2);

        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
