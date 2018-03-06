package com.thejordisc;

import javafx.scene.canvas.Canvas;

public class Goomba extends Sprite {

    @Override
    public void move(Canvas canvas, double time) {
        this.setPosition(this.getPositionX(),getPositionY()+getVelocityY()*time);
    }

    public Goomba() {
        super();
    }
}
