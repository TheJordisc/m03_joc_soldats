package com.thejordisc;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Sprite
{
    private Image image;
     double positionX;
     double positionY;
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private boolean goingLeft=false;
    private boolean goingRight=false;
    private boolean goingDown=false;
    private boolean goingUp=false;

    public Sprite() {
        width=200;
        height=200;
        positionX = 0;
        positionY = 0;
        goingRight=true;
        goingDown=true;
        velocityX = 5;
        velocityY = 5;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }

    public void move(Canvas canvas) {
        if (positionX > canvas.getWidth()-width) {
            goingLeft=true;
            goingRight=false;
        }

        if (positionX < 0) {
            goingRight=true;
            goingLeft=false;
        }

        if (positionY < 0) {
            goingDown=true;
            goingUp=false;
        }

        if (positionY > canvas.getHeight()-height) {
           goingUp=true;
           goingDown=false;
        }

        if (goingLeft) {
            positionX+=velocityX*-1;
        }

        if (goingRight) {
            positionX+=velocityX;
        }

        if (goingUp) {
            positionY+=velocityY*-1;
        }

        if (goingDown) {
            positionY+=velocityY;
        }

    }

    public void show(GraphicsContext context) {
        context.setFill(Color.GREEN);
        context.fillOval(positionX,positionY,width,height);
    }

    public void clear(GraphicsContext gc) {
        gc.clearRect(positionX,positionY,width,height);
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }

    public boolean isClicked(Point2D p){
        if (getBoundary().contains(p)) return  true;
        else  return false;
    }

    public String toString()
    {
        return " Position: [" + positionX + "," + positionY + "]" 
        + " Velocity: [" + velocityX + "," + velocityY + "]";
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}