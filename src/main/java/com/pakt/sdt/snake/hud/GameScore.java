package com.pakt.sdt.snake.hud;

import com.pakt.sdt.snake.App;
import java.io.IOException;
import java.util.Arrays;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author compakt-sdt
 */
public class GameScore {
    
    private final Text score;
    
    public GameScore(double x, double y, double size, String fontLocation) {
        score = new Text();
        score.setX(x);
        score.setY(y);
        loadFont(score, fontLocation, size);
    }
    
    private void loadFont(Text text, String fontLocation, double size) {
        try(var fis = App.class.getResourceAsStream(fontLocation)) {
            text.setFont(Font.loadFont(fis, size));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }
    
    public Text get() {
        return score;
    }
}
