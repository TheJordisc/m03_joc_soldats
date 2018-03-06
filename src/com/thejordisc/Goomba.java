package com.thejordisc;

import javafx.scene.canvas.Canvas;

public class Goomba extends Sprite {

    @Override
    public void move(Canvas canvas) {
        this.setPosition(this.getPositionX(),getPositionY()+1);
    }

    public Goomba() {
        super();
    }
}
