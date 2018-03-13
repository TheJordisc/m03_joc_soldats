package com.thejordisc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Game extends Application {
    //TODO: implements ITERATOR instead for (Goomba g : new ArrayList<>(goombas))
    private final double MAX_FONT_SIZE = 50.0;
    private final double MIN_FONT_SIZE = 40.0;
    private final int STAGE_WIDTH = 800;
    private final int STAGE_HEIGHT = 600;
    private final int TOTAL_GOOMBA = 10;

    private Mario mario;
    private Goomba goomba;
    private List<Goomba> goombas;
    private int score, level;
    private Canvas canvas;
    private long lastNanoTime;
    private Label labelScore,labelLevel,labelLevelUp;
    private GraphicsContext gc;
    private double elapsedTime;
    private Scene theScene;
    private Image backgroundImage;
    private boolean startButtonPressed;
    private StackPane root;
    private Button startButton;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        root = new StackPane();
        theScene = new Scene( root );

        theStage.setTitle( "Goomba Scape" );
        theStage.setScene( theScene );
        theStage.setWidth(STAGE_WIDTH);
        theStage.setHeight(STAGE_HEIGHT);
        theStage.setResizable(false);

        canvas = new Canvas(theStage.getWidth(),theStage.getHeight());
        canvas.setOnMouseClicked(this::mouseCliked);

        goombas = new ArrayList<>();
        mario= new Mario();

        initLabels();
        labelsSetStyle();
        setBackgroundImage();
        setNewGameVariables();

        gc = canvas.getGraphicsContext2D();
        lastNanoTime = System.nanoTime();

        startButton = new Button("Start new game");
        createStartButtonListener();

        root.getChildren().addAll(canvas,labelScore,labelLevel,labelLevelUp,startButton);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime=currentNanoTime;
                //updateWindowSize();

                if (startButtonPressed){
                    clearScreen();
                    renderSprites();
                    moveSprites();
                    checkSpriteCollision();
                    labelLevel.setText("LEVEL "+level);
                    labelScore.setText("SCORE "+score);
                }
                if (mario.positionX > canvas.getWidth()+ mario.getWidth()){
                updateLevel();
                }
            }
        }.start();
        theStage.show();
    }

    private void initLabels() {
        labelScore = new Label("");
        labelLevel = new Label("");
        labelLevelUp = new Label("");
    }

    private void setBackgroundImage() {
        backgroundImage = new Image("/com/thejordisc/Sprites/bg.png");
        root.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true))));
    }

    private void createStartButtonListener() {
        startButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                for (int i = 0; i< TOTAL_GOOMBA; i++){
                    createGoomba();
                }
                startButton.setVisible(false);
                startButtonPressed =true;
            }
        });
    }

    private void setNewGameVariables() {
        mario.positionX=0;
        score=0;
        level=1;
        startButtonPressed =false;
        labelLevelUp.setText("");
    }

    private void labelsSetStyle() {
        labelLevel.setTextFill(Color.web("#FFFFFF"));
        labelLevel.setFont(new Font(MIN_FONT_SIZE));
        StackPane.setAlignment(labelLevel, Pos.TOP_LEFT);

        labelScore.setTextFill(Color.web("#ff6600"));
        labelScore.setFont(new Font(MIN_FONT_SIZE));
        StackPane.setAlignment(labelScore, Pos.TOP_CENTER);

        labelLevelUp.setTextFill(Color.web("#FF0000"));
        labelLevelUp.setFont(new Font(MAX_FONT_SIZE));
        StackPane.setAlignment(labelLevelUp, Pos.CENTER);
    }

    private void updateLevel() {
            level++;
            score+=10;
            labelLevelUp.setText("LEVEL UP");
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

   /* private void updateWindowSize() {
        canvas.setWidth(theScene.getWidth());
        canvas.setHeight(theScene.getHeight());
    }*/

    private void moveSprites() {
        mario.move(canvas,elapsedTime);
        for (Goomba g :
                goombas) {
            g.move(canvas,elapsedTime);
        }
    }

    private void renderSprites() {
        mario.render(gc);
        for (Goomba g :
                goombas) {
            g.render(gc);
        }
    }

    private void clearScreen() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void createGoomba() {
        goomba = new Goomba();

        int rangePos = (int) ((canvas.getWidth()-goomba.getWidth() - 0) + 1);
        int randomPos=(int)(Math.random() * rangePos) + 0;
        goomba.setPosition(randomPos,0-goomba.getHeight());

        int rangeVel = ((50 - 20) + 1);
        int randomVel = (int)(Math.random() * rangeVel) + 20;
        goomba.setVelocityY(randomVel*level);
        goombas.add(goomba);

    }

    private void mouseCliked(MouseEvent e) {
        //TODO: implements iterator
        for (Goomba g : new ArrayList<>(goombas)) {
            if (g.isClicked(new Point2D(e.getSceneX(),e.getSceneY()))){
                labelLevelUp.setText("");
                goombas.remove(g);
                score++;
                createGoomba();
            }
        }
    }

    private void checkSpriteCollision() {
        for (Goomba g :
                goombas) {
            if(mario.intersects(g)){
                GameOver();
                break;
            }
        }
    }

    private void GameOver() {
        clearScreen();
        labelLevelUp.setText("");
        setNewGameVariables();
        goombas.clear();
        createGoomba();
        startButton.setVisible(true);
    }
}