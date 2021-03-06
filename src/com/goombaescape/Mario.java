package com.goombaescape;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;

public class Mario extends Sprite{
    private boolean reachedGoal =false;

    public Mario() {
        positionY=260;
        setVelocityX(160);
        this.setImage("/com/goombaescape/sprites/mario.png");
    }

    @Override
    public void move(Canvas canvas, double time) {
        //Reset Mario position when reaching goal line (out of screen)
        if (this.positionX > canvas.getWidth()+ this.getWidth()) {
            reachedGoal = true;
        }

        //Resets or move the character according to its position
        if (reachedGoal) {
            positionX=0-getWidth();
        }else{
            positionX+= getVelocityX() * time;
        }
    }

    @Override
    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,this.getWidth(),this.getHeight());
    }

    // Getters / setters

    public boolean isReachedGoal() {
        return reachedGoal;
    }

    public void setReachedGoal(boolean reachedGoal) {
        this.reachedGoal = reachedGoal;
    }
}