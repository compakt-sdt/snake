package com.pakt.sdt.snake;

/**
 *
 * @author compakt-sdt
 */
public enum Direction {
    
    NONE(0, 0),
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);
    
    public final int x;
    public final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public static Direction getRandom() {
        return values()[(int)(Math.random() * (values().length - 1) + 1)];
    }
    
}
