package com.thejordisc;

import javafx.scene.canvas.Canvas;

public class Mario extends Sprite{
    private boolean resetPosition=false;

    public Mario() {
        positionY=260;
        setVelocityX(2);
        this.setWidth(100);
        this.setImage("/com/thejordisc/Sprites/mario.png");
    }

    @Override
    public void move(Canvas canvas, double time) {
        if (positionX > canvas.getWidth()+ getWidth()) {
            resetPosition =true;
        }

        if (positionX < 0) {
            resetPosition =false;
        }

        if (resetPosition) {
            positionX=0-getWidth();
            resetPosition=false;
        }else{
            positionX+= getVelocityX() *1;
        }
    }

}