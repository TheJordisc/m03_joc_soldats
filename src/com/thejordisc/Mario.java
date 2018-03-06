package com.thejordisc;

import javafx.scene.canvas.Canvas;

public class Mario extends Sprite{
    private boolean reset=false;
//TODO: acabar

    @Override
    public void move(Canvas canvas, double time) {
        if (positionX > canvas.getWidth()- getWidth()) {
            reset=true;

        }

        if (positionX < 0) {

            reset=false;
        }

        if (reset) {
            positionX= 0;
        }

    }
}
