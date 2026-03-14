package com.pakt.sdt.snake.hud;

import com.pakt.sdt.snake.App;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author compakt-sdt
 */
public class GameTimer {
    
    private final Text timer;
    
    public GameTimer(double x, double y, double size, Color color, String fontLocation) {
        timer = new Text();
        timer.setX(x);
        timer.setY(y);
        timer.setFill(color);
        loadFont(timer, fontLocation, size);
    }
    
    private void loadFont(Text text, String fontLocation, double size) {
        try(var fis = App.class.getResourceAsStream(fontLocation)) {
            text.setFont(Font.loadFont(fis, size));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }
    
    public Text get() {
        return timer;
    }
}
