package com.thejordisc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Game extends Application {

    //TODO:cuando llegue a la meta +1nivel, si toca a mario nivel 1

    Mario mario;
    int score=0;
    int level=1;
    List<Goomba> goombas = new ArrayList<>();
    Canvas canvas;
    long lastNanoTime;
    int totalGoomba;
    Label labelScore = new Label(""+score);
    Label labelLevel = new Label("LEVEL "+level);
    GraphicsContext gc;
    double elapsedTime;
    Scene theScene;
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        theStage.setTitle( "My Game" );

        Group root = new Group();
        theScene = new Scene( root );
        theStage.setScene( theScene );

        int screenWidth = 800;
        theStage.setWidth(screenWidth);

        int screenHeight = 600;
        theStage.setHeight(screenHeight);

        canvas = new Canvas(theStage.getWidth(),theStage.getHeight());
        canvas.setOnMouseClicked(this::mouseCliked);
        root.getChildren().add(canvas);
        root.getChildren().add(labelScore);
        root.getChildren().add(labelLevel);
        gc = canvas.getGraphicsContext2D();

        mario= new Mario();
        mario.setWidth(100);

        createGoombas();

        lastNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime=currentNanoTime;

                updateWindowSize();

                clearSprites();
                renderSprites();
                moveSprites();
                checkSpriteCollision();

                updateLevel();
            }
        }.start();
        
        theStage.show();
    }

    private void updateLevel() {
        level=1;
    }

    private void updateWindowSize() {
        canvas.setWidth(theScene.getWidth());
        canvas.setHeight(theScene.getHeight());
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
        for (Goomba g :
                goombas) {
            g.clear(gc);
        }
        mario.clear(gc);
    }

    private void createGoombas() {
        totalGoomba=(int)(Math.random() * 12) + 6;
        for (int i = 0; i < totalGoomba; i++) {
            Goomba goomba = new Goomba();

            int rangePos = (int) ((canvas.getWidth()-goomba.getWidth() - 0) + 1);
            int randomPos=(int)(Math.random() * rangePos) + 0;
            goomba.setPosition(randomPos,0-goomba.getHeight());

            int rangeVel = ((50 - 20) + 1);
            int randomVel = (int)(Math.random() * rangeVel) + 20;
            goomba.setVelocityY(randomVel);
            goombas.add(goomba);
        }
    }

    private void mouseCliked(MouseEvent e) {
        for (Goomba g :
                goombas) {
            if (g.isClicked(new Point2D(e.getSceneX(),e.getSceneY()))){
               g.setVelocityY(0);
               score++;
               labelScore.setText(""+score);
            }
        }
    }

    private void checkSpriteCollision() {
        for (Goomba g :
                goombas) {
            if(mario.intersects(g)){
                score--;
                labelScore.setText(""+score);
            }
        }
    }
}