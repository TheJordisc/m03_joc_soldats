package com.goombascape;

import javafx.scene.canvas.Canvas;

public class Mario extends Sprite{
    private boolean reachedGoal =false;

    public Mario() {
        positionY=260;
        setVelocityX(160);
        this.setWidth(100);
        this.setImage("/com/goombascape/sprites/mario.png");
    }

    @Override
    public void move(Canvas canvas, double time) {
        //Reset Mario position when reaching goal line (out of screen)
        if (positionX > canvas.getWidth()+ getWidth()) {
            reachedGoal = true;
        }

        //Resets or move the character according to its position
        if (reachedGoal) {
            positionX=0-getWidth();
        }else{
            positionX+= getVelocityX() * time;
        }
    }

    // Getters / setters

    public boolean isReachedGoal() {
        return reachedGoal;
    }

    public void setReachedGoal(boolean reachedGoal) {
        this.reachedGoal = reachedGoal;
    }
}