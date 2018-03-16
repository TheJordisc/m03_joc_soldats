package com.goombaescape;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Game extends Application {
    //TODO: implements ITERATOR instead for (Goomba g : new ArrayList<>(goombas))
    private final double MAX_FONT_SIZE = 50.0;
    private final double MIN_FONT_SIZE = 40.0;
    private final int STAGE_WIDTH = 800;
    private final int STAGE_HEIGHT = 600;
    private final int TOTAL_GOOMBA = 10;

    private Mario mario;
    private List<Goomba> goombas;
    private int score, level;
    private Canvas canvas;
    private long lastNanoTime;
    private Label labelScore,labelLevel,labelLevelUp;
    private GraphicsContext gc;
    private double elapsedTime;
    private Scene theScene;
    private Image backgroundImage;
    private ImageView logoImage;
    private boolean gameIsRunning;
    private StackPane root;
    private Button startButton;
    private long levelUpTime;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        //Prepare scene and canvas

        root = new StackPane();
        theScene = new Scene( root );

        prepareStage(theStage);

        canvas = new Canvas(theStage.getWidth(),theStage.getHeight());
        canvas.setOnMouseClicked(this::mouseCliked);

        gc = canvas.getGraphicsContext2D();

        // Declare sprites

        goombas = new ArrayList<Goomba>();
        mario= new Mario();

        //Prepare new game and GUI

        logoImage = new ImageView("/com/goombaescape/ui/logo.png");

        initLabels();
        setLabelsStyle();
        setBackgroundImage();
        setNewGameVariables();


        startButton = new Button("START");
        startButton.setPadding(new Insets(25));
        startButton.setFont(new Font("Arial", 50));
        createStartButtonListener();

        root.getChildren().addAll(canvas,labelScore,labelLevel,labelLevelUp,startButton,logoImage);

        //Prepare start time
        lastNanoTime = System.nanoTime();
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                //Update time
                elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime=currentNanoTime;

                //Game is running and computes graphic movements
                if (gameIsRunning){
                    clearScreen();
                    moveAndRenderSprites();
                    checkSpriteCollision(mario);
                    updateLabels();

                    if (mario.isReachedGoal()){
                        levelUpTime=currentNanoTime;
                        mario.setReachedGoal(false);
                        updateLevel();
                    }

                    if (currentNanoTime/1000000000.0 >= levelUpTime/1000000000.0 + 2) {
                        labelLevelUp.setVisible(false);
                    }

                }

            }
        }.start();

    }

    private void updateLabels() {
        labelLevel.setText("LEVEL "+level);
        labelScore.setText("SCORE "+score);
    }

    private void prepareStage(Stage theStage) {
        theStage.setTitle( "Goomba Escape" );
        theStage.setScene( theScene );
        theStage.setWidth(STAGE_WIDTH);
        theStage.setHeight(STAGE_HEIGHT);
        theStage.setResizable(false);
        theStage.show();
    }

    private void initLabels() {
        labelScore = new Label("");
        labelLevel = new Label("");
        labelLevelUp = new Label("");
    }

    private void setBackgroundImage() {
        backgroundImage = new Image("/com/goombaescape/ui/bg.png");
        root.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true))));
    }

    private void createStartButtonListener() {
        startButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                setNewGameVariables();
                startGame();
            }
        });
    }

    private void startGame() {
        //Spaw goombas
        for (int i = 0; i< TOTAL_GOOMBA; i++){
            createGoomba();
        }

        startButton.setVisible(false);
        logoImage.setVisible(false);

        gameIsRunning =true;
    }

    private void setNewGameVariables() {
        mario.positionX=0;
        score=0;
        level=1;
        gameIsRunning =false;
        labelLevelUp.setText("");
    }

    private void setLabelsStyle() {
        labelLevel.setTextFill(Color.web("#000000"));
        labelLevel.setFont(new Font(MIN_FONT_SIZE));
        StackPane.setAlignment(labelLevel, Pos.BOTTOM_LEFT);
        StackPane.setMargin(labelLevel, new Insets(0,0,20,15));

        labelScore.setTextFill(Color.web("#FFFFFF"));
        labelScore.setFont(new Font(MIN_FONT_SIZE));
        StackPane.setAlignment(labelScore, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(labelScore, new Insets(0,15,20,0));

        labelLevelUp.setTextFill(Color.web("#FF0000"));
        labelLevelUp.setFont(new Font(MAX_FONT_SIZE));
        StackPane.setAlignment(labelLevelUp, Pos.BOTTOM_CENTER);
        StackPane.setMargin(labelLevelUp, new Insets(0,0,20,0));

        StackPane.setMargin(logoImage,new Insets(30,0,0,0));
        StackPane.setAlignment(logoImage,Pos.TOP_CENTER);
    }

    private void updateLevel() {
            level++;
            score+=10;
            labelLevelUp.setVisible(true);
            labelLevelUp.setText("LEVEL UP");

            //Restart goomba position to top
            for (Goomba g :
                    goombas) {
                int rangePos = (int) ((canvas.getWidth()-g.getWidth() - 0) + 1);
                int randomPos=(int)(Math.random() * rangePos) + 0;
                g.setPosition(randomPos,0-g.getHeight());

                int rangeVel = ((50 - 20) + 1);
                int randomVel = (int)(Math.random() * rangeVel) + 20;
                g.setVelocityY(randomVel*level);
            }
    }

    private void moveAndRenderSprites() {
        mario.move(canvas,elapsedTime);
        mario.render(gc);

        for (Goomba g :
                goombas) {
            g.move(canvas,elapsedTime);
            g.render(gc);
        }
    }

    private void clearScreen() {
        //Clears the WHOLE screen (fixes every clearing issue)
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void createGoomba() {
        Goomba goomba = new Goomba();

        //Calculates random X position
        int rangePos = (int) ((canvas.getWidth()- goomba.getWidth() - 0) + 1);
        int randomPos=(int)(Math.random() * rangePos) + 0;
        goomba.setPosition(randomPos,0 - goomba.getHeight());

////        //TODO: Check if collides goomba
//        while (checkSpriteCollision(goomba)) {
//            rangePos = (int) ((canvas.getWidth()- goomba.getWidth() - 0) + 1);
//            randomPos=(int)(Math.random() * rangePos) + 0;
//            goomba.setPosition(randomPos,0 - goomba.getHeight());
//        }

        //Calculates random velocity
        int rangeVel = ((50 - 20) + 1);
        int randomVel = (int)(Math.random() * rangeVel) + 20;
        goomba.setVelocityY(randomVel*level);

        //Adds new goomba to list
        goombas.add(goomba);
    }

    private void mouseCliked(MouseEvent e) {
        Iterator<Goomba> i = goombas.iterator();
        while (i.hasNext()) {
            Goomba g = i.next(); // must be called before you can call i.remove()
            if (g.isClicked(new Point2D(e.getX(), e.getY()))) {
                i.remove();
                score++;
                createGoomba();
                break;
            }
        }
    }

    private boolean checkSpriteCollision(Sprite sprite) {
        for (Goomba g :
                goombas) {
            if(sprite.intersects(g)){
                gameOver();
                return true;
            }

            if (g.intersects(sprite)) {
                return true;
            }
        }

        return false;
    }

    private void gameOver() {
        goombas.clear();
        clearScreen();
       // setNewGameVariables();
        startButton.setVisible(true);
        logoImage.setVisible(true);
        labelLevelUp.setVisible(false);
        gameIsRunning=false;
    }
}