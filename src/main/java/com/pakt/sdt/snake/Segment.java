package com.pakt.sdt.snake;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author compakt-sdt
 */
public class Segment extends Rectangle {
    
    public static final double SIZE = 10.0;
    
    private Direction linkDirection;
    private Direction moveDirection;
    
    public Segment(double parentX, double parentY) {
        initializeSegment();
        setX(parentX);
        setY(parentY);
    }
    
    public Segment(double parentX, double parentY, Direction linkDirection) {
        this.linkDirection = linkDirection;
        initializeSegment();
        setX(parentX + (getWidth() * this.linkDirection.x));
        setY(parentY + (getHeight() * this.linkDirection.y));
        
    }
    
    public void moveHead(Direction moveDirection) {
        this.moveDirection = moveDirection;
        setX(getX() + (SIZE * this.moveDirection.x));
        setY(getY() + (SIZE * this.moveDirection.y));
    }
    
    public void move(double parentX, double parentY) {
        setX(parentX);
        setY(parentY);
    }
    
    public Direction getMoveDirection() {
        return moveDirection;
    }
    
    public Direction getLinkDirection() {
        return linkDirection;
    }
    
    private void initializeSegment() {
        setWidth(SIZE);
        setHeight(SIZE);
        setStrokeWidth(1.5);
        setStroke(Color.BLACK);
        setArcWidth(getWidth() / 2);
        setArcHeight(getHeight() / 2);
    }
    
}
