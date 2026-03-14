package com.pakt.sdt.snake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author compakt-sdt
 */
public class FoodFactory {
    
    private final List<Color> colors = new ArrayList<>();
    private final Random random = new Random();
    private Circle food;
    
    public FoodFactory() {
        initializeColors();
    }
    
    public Circle produce() {
        food = new Circle(Segment.SIZE / 2);
        food.setFill(randomColor());
        food.setCenterX(randomPoint());
        food.setCenterY(randomPoint());
        return food;
    }
    
    public double randomPoint() {
        var randomInt = random.nextInt(60);
        return Segment.SIZE * randomInt + food.getRadius();
     }
    
    public Color randomColor() {
        return colors.get(((int)(Math.random() * colors.size())));
    }
    
    private void initializeColors() {
        colors.addAll(Arrays.asList(Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.BLUE, Color.ORANGE,
                Color.GREEN, Color.RED, Color.FUCHSIA, Color.VIOLET, Color.TURQUOISE, Color.LIME));
    }
    
}
