package com.goombaescape;

import javafx.scene.canvas.Canvas;

public class Goomba extends Sprite {

    public Goomba() {
        super();
        this.setImage("/com/goombaescape/sprites/goomba.png");
    }

    @Override
    public void move(Canvas canvas, double time) {
        //Move down goombas
        this.setPosition(this.getPositionX(),getPositionY()+getVelocityY()*time);

        //Reset position when reaching bottom of screen. Computes random X position
        if (this.positionY>canvas.getHeight()){
            int rangePos = (int) ((canvas.getWidth()-this.getWidth() - 0) + 1);
            int randomPos=(int)(Math.random() * rangePos) + 0;
            setPosition(randomPos,0-this.getHeight());
        }
    }
}
