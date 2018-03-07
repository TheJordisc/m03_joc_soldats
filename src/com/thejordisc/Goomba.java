package com.thejordisc;

import javafx.scene.canvas.Canvas;

public class Goomba extends Sprite {

    public Goomba() {
        super();
        this.setImage("/com/thejordisc/goomba.gif");
    }

    @Override
    public void move(Canvas canvas, double time) {
        this.setPosition(this.getPositionX(),getPositionY()+getVelocityY()*time);
    }
}
