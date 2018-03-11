package com.thejordisc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Game extends Application {

    //TODO: clean code, implements ITERATOR instead for (Goomba g : new ArrayList<>(goombas))

    Mario mario;
    int score=0;
    int level=1;
    List<Goomba> goombas = new ArrayList<>();
    Canvas canvas;
    long lastNanoTime;
    int totalGoomba;
    int goombaClicked=0;
    Label labelScore = new Label(""+score);
    Label labelLevel = new Label("LEVEL "+level);
    Label labelLevelUp = new Label("");
    Label lastRoundScore = new Label("");
    GraphicsContext gc;
    double elapsedTime;
    Scene theScene;
    Image image;
    ImagePattern pattern;
    boolean start=false;
    final double MAX_FONT_SIZE = 50.0;
    Button startButton;
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        theStage.setTitle( "My Game" );

        StackPane root = new StackPane();
        theScene = new Scene( root );
        theStage.setScene( theScene );

        int screenWidth = 800;
        theStage.setWidth(screenWidth);

        int screenHeight = 600;
        theStage.setHeight(screenHeight);

        theStage.setResizable(false);

        canvas = new Canvas(theStage.getWidth(),theStage.getHeight());
        canvas.setOnMouseClicked(this::mouseCliked);
        startButton = new Button("Start");

        startButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                for (int i=0;i<totalGoomba;i++){
                    createGoomba();
                }
                mario= new Mario();
                mario.setWidth(100);
                startButton.setVisible(false);
                start=true;
            }

        });
        root.getChildren().addAll(canvas,labelScore,labelLevel,labelLevelUp,startButton,lastRoundScore);
        StackPane.setAlignment(labelLevel, Pos.TOP_LEFT);
        StackPane.setAlignment(labelScore, Pos.TOP_CENTER);
        StackPane.setAlignment(labelLevelUp, Pos.CENTER);
        StackPane.setAlignment(lastRoundScore, Pos.BOTTOM_CENTER);

        labelLevel.setTextFill(Color.web("#FFFFFF"));
        labelScore.setTextFill(Color.web("#FFFFFF"));
        labelLevelUp.setTextFill(Color.web("#FF0000"));
        lastRoundScore.setTextFill(Color.web("#0000FF"));
        labelLevelUp.setFont(new Font(MAX_FONT_SIZE));
        lastRoundScore.setFont(new Font(MAX_FONT_SIZE));

        image = new Image("/com/thejordisc/Sprites/bg.png");
        pattern = new ImagePattern(image);
        root.setBackground(new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true))));

        gc = canvas.getGraphicsContext2D();
        labelLevel.autosize();

        totalGoomba=(int)(Math.random() * 12) + 6;

        lastNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime=currentNanoTime;

                updateWindowSize();

                if (start){
                    lastRoundScore.setText("");
                    clearSprites();
                    renderSprites();
                    moveSprites();
                    checkSpriteCollision();
                    updateLevel();
                    System.out.println(goombas.size());
                }
                labelLevel.setText("LEVEL "+level);
                labelScore.setText("LEVEL SCORE "+score);
            }
        }.start();
        
        theStage.show();
    }


    private void updateLevel() {
        if (goombaClicked==totalGoomba){
            level++;
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
            goombaClicked=0;
        }
    }

    private void updateWindowSize() {
        canvas.setWidth(theScene.getWidth());
        canvas.setHeight(theScene.getHeight());

        theScene.setFill(pattern);
    }

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

    private void clearSprites() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void createGoomba() {
        for (int i = 0; i < 1; i++) {
            Goomba goomba = new Goomba();

            int rangePos = (int) ((canvas.getWidth()-goomba.getWidth() - 0) + 1);
            int randomPos=(int)(Math.random() * rangePos) + 0;
            goomba.setPosition(randomPos,0-goomba.getHeight());

            int rangeVel = ((50 - 20) + 1);
            int randomVel = (int)(Math.random() * rangeVel) + 20;
            goomba.setVelocityY(randomVel*level);
            goombas.add(goomba);
        }
    }

    private void mouseCliked(MouseEvent e) {
        //TODO: implements iterator
        for (Goomba g : new ArrayList<>(goombas)) {
            if (g.isClicked(new Point2D(e.getSceneX(),e.getSceneY()))){
                labelLevelUp.setText("");
                goombas.remove(g);
                score++;
                goombaClicked++;
                createGoomba();
            }
        }
    }

    private void checkSpriteCollision() {
        for (Goomba g :
                goombas) {
            if(mario.intersects(g)){
                resetGame();
            }
        }
    }

    private void resetGame() {
        lastRoundScore.setText("TOTAL SCORE "+score*level);
        score=0;
        level=1;
        goombaClicked=0;
        mario.positionX=0;
        mario.setVelocityX(2);
        start=false;
        for (Goomba g :
                goombas) {
            int rangePos = (int) ((canvas.getWidth()-g.getWidth() - 0) + 1);
            int randomPos=(int)(Math.random() * rangePos) + 0;
            g.setPosition(randomPos,0-g.getHeight());

            int rangeVel = ((50 - 20) + 1);
            int randomVel = (int)(Math.random() * rangeVel) + 20;
            g.setVelocityY(randomVel*level);
        }
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        startButton.setVisible(true);
    }
}