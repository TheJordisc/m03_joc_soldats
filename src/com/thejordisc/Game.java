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

// Animation of Earth rotating around the sun. (Hello, world!)
public class Game extends Application {

    Sprite ball = new Sprite();

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

        ball.setImage("/com/thejordisc/ball.png");

        long lastNanoTime = System.nanoTime();

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;

                ball.clear(gc);

                canvas.setWidth(theScene.getWidth());
                canvas.setHeight(theScene.getHeight());

                ball.move(canvas);
                ball.render(gc);
            }
        }.start();
        
        theStage.show();
    }

    private void mouseCliked(MouseEvent e) {
        if (ball.isClicked(new Point2D(e.getSceneX(),e.getSceneY()))){
            ball.setVelocityY(ball.getVelocityY()+10);
        }
    }


}