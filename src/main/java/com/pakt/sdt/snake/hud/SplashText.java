package com.pakt.sdt.snake.hud;

import com.pakt.sdt.snake.App;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 *
 * @author compakt-sdt
 */
public class SplashText {
    
    private Text text;
    private TextFlow textFlow;
    private List<Color> colors;
    
    private boolean isText = true;
    
    public static enum ColorStyle {
        GRADIENT, GRADIENT_RAND, INDIVIDUAL_RAND, GROUP_RAND
    }
    
    public SplashText(String text, double x, double y, double size, Color color) {
        this.text = new Text(x, y, text);
        this.text.setFill(color);
    }
    
    public SplashText(String text, double x, double y, double size, Color color, String fontLocation) {
        this.text = new Text(x, y, text);
        this.text.setFill(color);
        loadFont(this.text, fontLocation, size);
    }
    
    public SplashText(String text, double x, double y, double size, boolean multipleColors, ColorStyle colorStyle, String fontLocation) {
        if (multipleColors) {
            switch (colorStyle) {
                case GRADIENT -> {
                    this.text = new Text(x, y, text);
                    loadFont(this.text, fontLocation, size);
                    this.text.setFill(new LinearGradient(
                            0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE, getGradient(false)));
                }
                case GRADIENT_RAND -> {
                    this.text = new Text(x, y, text);
                    loadFont(this.text, fontLocation, size);
                    initColors();
                    this.text.setFill(new LinearGradient(
                            0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE, getGradient(true)));
                }
                case INDIVIDUAL_RAND -> {
                    textFlow = new TextFlow();
                    textFlow.setTranslateX(x);
                    textFlow.setTranslateY(y);
                    initColors();
                    individualFactory(text, fontLocation, size);
                    isText = false;
                }
                case GROUP_RAND -> {
                    textFlow = new TextFlow();
                    textFlow.setTranslateX(x);
                    textFlow.setTranslateY(y);
                    initColors();
                    groupFactory(text, fontLocation, size);
                    isText = false;
                }
            }
        } else {
            this.text = new Text(x, y, text);
            this.text.setFill(Color.WHITE);
            loadFont(this.text, fontLocation, size);
        }
    }
    
    public Text get() {
        return (this.text != null) ? text : new Text(0.0, 0.0, "Text");
    }
    
    public TextFlow getTextFlow() throws NullPointerException {
        return textFlow;
    }
    
    public boolean isText() {
        // true - get() | false - getTextFlow()
        return this.isText;
    }
    
    private void loadFont(Text text, String fontLocation, double size) {
        try(var fis = App.class.getResourceAsStream(fontLocation)) {
            text.setFont(Font.loadFont(fis, size));
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }
    
    private Stop[] getGradient(boolean isRandom) {
        if (isRandom) {
            return new Stop[]{
                new Stop(0.0, getRandomColor()), new Stop(0.125, getRandomColor()), new Stop(0.25, getRandomColor()),
                new Stop(0.375, getRandomColor()), new Stop(0.5, getRandomColor()), new Stop(0.625, getRandomColor()),
                new Stop(0.75, getRandomColor()), new Stop(0.875, getRandomColor()), new Stop(1.0, getRandomColor())
            };
        } else {
            return new Stop[]{
                new Stop(0.0, Color.RED), new Stop(0.125, Color.ORANGE), new Stop(0.25, Color.YELLOW),
                new Stop(0.375, Color.YELLOWGREEN), new Stop(0.5, Color.GREEN), new Stop(0.625, Color.TURQUOISE),
                new Stop(0.75, Color.CYAN), new Stop(0.875, Color.TEAL), new Stop(1.0, Color.BLUE)
            };
        }
    }
    
    private Color getRandomColor() {
        var i = (int) (Math.random() * ((colors.size() - 1) - 0) + 0);
        var color = colors.get(i);
        return (color.equals(Color.BLACK) || color.equals(Color.WHITE)) ? Color.GREY : color;
    }
    
    private void initColors() {
        colors = new ArrayList<>();
        Class<Color> colorClass = Color.class;
        Field[] colorFields = colorClass.getFields();
        for (Field field : colorFields) {
            if (Color.class.equals(field.getType())) {
                try {
                    Object obj = field.get(null);
                    if (obj instanceof Color color) {
                        colors.add(color);
                    }
                } catch (IllegalAccessException ex) {
                    System.out.println(Arrays.asList(ex.getStackTrace()));
                }
            }
        }
    }
    
    private void individualFactory(String text, String fontLocation, double size) {
        var wordArr = text.split("\\s+");
        for (String word : wordArr) {
            var charArr = word.toCharArray();
            for (char c : charArr) {
                var newText = new Text(String.valueOf(c));
                newText.setFill(getRandomColor());
                loadFont(newText, fontLocation, size);
                textFlow.getChildren().add(newText);
            }
            textFlow.getChildren().add(new Text("\\s"));
        }
    }
    
    private void groupFactory(String text, String fontLocation, double size) {
        var wordArr = text.split("\\s+");
        for (String word : wordArr) {
            var newText = new Text(word);
            newText.setFill(getRandomColor());
            loadFont(newText, fontLocation, size);
            textFlow.getChildren().addAll(newText,  new Text("\\s"));
        }
    }
}
