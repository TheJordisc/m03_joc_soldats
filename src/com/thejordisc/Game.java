package com.thejordisc;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.xqj.exist.bin.G;

import java.util.ArrayList;
import java.util.List;

// Animation of Earth rotating around the sun. (Hello, world!)
public class Game extends Application {

    Sprite mario = new Sprite();

    List<Goomba> goombas = new ArrayList<>();

    long lastNanoTime;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {

        theStage.setTitle( "My Game" );

        Group root = new Group();
        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        int screenWidth = 800;
        theStage.setWidth(screenWidth);

        int screenHeight = 600;
        theStage.setHeight(screenHeight);

        Canvas canvas = new Canvas(theStage.getWidth(),theStage.getHeight());
        canvas.setOnMouseClicked(this::mouseCliked);
        root.getChildren().add( canvas );

        GraphicsContext gc = canvas.getGraphicsContext2D();

        mario.setImage("/com/thejordisc/ball.png");

        for (int i = 0; i < 5; i++) {
            Goomba goomba = new Goomba();
            goomba.setImage("/com/thejordisc/goomba.gif");
            int rangePos = (int) ((canvas.getWidth()-goomba.getWidth() - 0) + 1);
            int randomPos=(int)(Math.random() * rangePos) + 0;
            goomba.setPosition(randomPos,0-goomba.getHeight());

            int rangeVel = ((50 - 20) + 1);
            int randomVel = (int)(Math.random() * rangeVel) + 20;
            goomba.setVelocityY(randomVel);
            goombas.add(goomba);
        }

        lastNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime=currentNanoTime;

                mario.clear(gc);

                for (Goomba g :
                        goombas) {
                    g.clear(gc);
                }

                canvas.setWidth(theScene.getWidth());
                canvas.setHeight(theScene.getHeight());

                mario.move(canvas,elapsedTime);
                mario.render(gc);

                for (Goomba g :
                        goombas) {
                    g.move(canvas,elapsedTime);
                    g.render(gc);
                }
            }
        }.start();
        
        theStage.show();
    }

    private void mouseCliked(MouseEvent e) {
        if (mario.isClicked(new Point2D(e.getSceneX(),e.getSceneY()))){
            mario.setVelocityY(mario.getVelocityY()+10);
        }
    }


}