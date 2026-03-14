package com.pakt.sdt.snake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author compakt-sdt
 */
public class Snake {
    
    private final List<Segment> snake = new ArrayList<>();
    
    private final double initialX;
    private final double initialY;
    
    private Circle currentFoodEaten;
    private Color currentColor;
    
    public Snake() {
        initialX = 30.0;
        initialY = 70.0;
        initializeSegments();
    }
    
    public List<Segment> get() {
        return snake;
    }
    
    public void move(Direction direction) {
        var previousParentX = snake.get(0).getX();
        var previousParentY = snake.get(0).getY();
        snake.get(0).moveHead(direction);
        
        double currentParentX;
        double currentParentY;
        
        for (int i = 1; i < snake.size(); i++) {
            currentParentX = snake.get(i).getX();
            currentParentY = snake.get(i).getY();
            snake.get(i).move(previousParentX, previousParentY);
            
            previousParentX = currentParentX;
            previousParentY = currentParentY;
        }
    }
    
    public Segment appendSegment() {
        var tail = snake.get(snake.size() - 1);
        var newTail = new Segment(tail.getX(), tail.getY());
        newTail.setFill((Color)tail.getFill());
        snake.add(newTail);
        return newTail;
    }
    
    public boolean eat(List<Circle> foods) {
        var snakeHead = snake.get(0);
        for (var food : foods) {
            if (snakeHead.getX() + snakeHead.getWidth() >= food.getCenterX() &&
                    snakeHead.getX() <= food.getCenterX() &&
                    snakeHead.getY() + snakeHead.getHeight() >= food.getCenterY() &&
                    snakeHead.getY() <= food.getCenterY()) {
                currentFoodEaten = food;
                return true;
            }
        }
        return false;
    }
    
    public boolean collidedRootBounds() {
        var snakeHead = snake.get(0);
        return snakeHead.getX() + snakeHead.getWidth() > App.WIDTH ||
                snakeHead.getY() + snakeHead.getHeight() > App.HEIGHT ||
                snakeHead.getX() < 0 || snakeHead.getY() < 0;
    }
    
    public boolean collidedSegmentBounds(Direction userDirection) {
        var snakeHead = snake.get(0);
        for (int i = 1; i < snake.size(); i++) {
            var segment = snake.get(i);
            if (snakeHead.getX() == segment.getX() && snakeHead.getY() == segment.getY()) {
                return true;
            }
        }
        return false;
    }
    
    public Circle getCurrentFoodEaten() {
        return currentFoodEaten;
    }
    
    public void changeColor(Color color) {
        for (var segment : snake) {
            segment.setFill(color);
        }
        currentColor = color;
    }
    
    public Color getColor() {
        return currentColor;
    }
   
    private void initializeSegments() {
        var head = new Segment(initialX, initialY, Direction.NONE);
        var body = new Segment(head.getX(), head.getY(), Direction.LEFT);
        var tail = new Segment(body.getX(), body.getY(), Direction.LEFT);
        snake.addAll(Arrays.asList(head, body, tail));
        snake.forEach(segment -> segment.setFill(Color.WHITE));
        currentColor = Color.WHITE;
    }
    
}
