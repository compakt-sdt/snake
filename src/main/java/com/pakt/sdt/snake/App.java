package com.pakt.sdt.snake;

import com.pakt.sdt.snake.hud.GameScore;
import com.pakt.sdt.snake.hud.GameTimer;
import com.pakt.sdt.snake.hud.SplashText;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author compakt-sdt
 */
public class App extends Application {
    
    public static final double WIDTH = 500;
    public static final double HEIGHT = 600;
    
    private Scene scene;
    private AnimationTimer animationTimer;
    private final Pane root = new Pane();
    private Snake snake = new Snake();
    private final FoodFactory foodFactory = new FoodFactory();
    private final List<Circle> foods = new ArrayList<>();
    private Text score = new Text();
    private Text timer = new Text();
    private SplashText splashText;
    private LocalDateTime startTime;
    
    private final int FPS = 60;
    private final int MAX_SPEED = 3;
    private final int MIN_SPEED = 20;
    
    private int speedLimiter = MIN_SPEED;
    private int snakeMovement = 0;
    private int foodShowing = 0;
    private boolean gamePlaying = true;
    private boolean isSplash = true;
    private Direction userDirection = Direction.RIGHT;
    
    @Override
    public void start(Stage stage) throws Exception {
        initializeSceneGraph();
        startTime = LocalDateTime.now();
        // windows and linux uses forward slash for classpath resources
        stage.getIcons().add(new Image(
                App.class.getResource("/icon/snake_32px.png")
                        .toExternalForm()));
        stage.setScene(scene);
        stage.setTitle("Snake");
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
    }
    
    @Override
    public void stop() throws Exception {
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
    
    private void gameLoop() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
                if (!gamePlaying) {
                    endGame();
                }
            }
        };
        animationTimer.start();
    }
    
    private void update() {
        timer.setText(formatTime());
        
        snakeMovement++;
        foodShowing++;
        
        if (snakeMovement % speedLimiter == 0) {
            if (gamePlaying) {
                snake.move(userDirection);
            }
        }
        
        if (snake.collidedRootBounds() || snake.collidedSegmentBounds(userDirection)) {
            gamePlaying = false;
        }
        
        if (snakeMovement >= (FPS * 25)) {
            speedLimiter = (speedLimiter == MAX_SPEED) ? speedLimiter : speedLimiter - 1;
            snakeMovement = 0;
        }
        
        if (foodShowing >= (FPS * 3)) {
            addNewFood();
            foodShowing = 0;
        }
        
        if (!foods.isEmpty() && snake.eat(foods)) {
            removeEatenFood();
        }
    }
    
    private void setSceneController() {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP, W -> {
                    if (userDirection != Direction.DOWN) {
                        userDirection = Direction.UP;
                    }
                }
                case RIGHT, D -> {
                    if (userDirection != Direction.LEFT) {
                        userDirection = Direction.RIGHT;
                    }
                }
                case DOWN, S -> {
                    if (userDirection != Direction.UP) {
                        userDirection = Direction.DOWN;
                    }
                }
                case LEFT, A -> {
                    if (userDirection != Direction.RIGHT) {
                        userDirection = Direction.LEFT;
                    }
                }
                case SPACE, ENTER -> {
                    // Splash state
                    if (isSplash) {
                        if (splashText.isText()) {
                            root.getChildren().remove(splashText.get());
                        } else {
                            root.getChildren().remove(splashText.getTextFlow());
                        }
                       startTime = LocalDateTime.now();
                       gameLoop();
                       isSplash = false;
                    }
                    // Restart gameplay
                    if (!gamePlaying) {
                        restartGame();
                    }
                }
                case ESCAPE -> Platform.exit();
            }
        });
    }
    
    private void addNewFood() {
        tryAgain:
        while (true) {
            var newFood = foodFactory.produce();
            if (!foods.isEmpty()) {
                for (var food : foods) {
                    if (newFood.getCenterX() == food.getCenterX() && newFood.getCenterY() == food.getCenterY()) {
                        continue tryAgain;
                    }
                }
            }
            root.getChildren().add(newFood);
            foods.add(newFood);
            break;
        }
    }
    
    private void removeEatenFood() {
        var eatenFood = snake.getCurrentFoodEaten();
        snake.changeColor((Color) eatenFood.getFill());
        foods.remove(eatenFood);
        root.getChildren().remove(eatenFood);
        root.getChildren().add(snake.appendSegment());
    }
    
    private void restartGame() {
        root.getChildren().clear();
        snake = new Snake();
        foods.clear();
        speedLimiter = MIN_SPEED;
        snakeMovement = 0;
        foodShowing = 0;
        userDirection = Direction.RIGHT;
        root.getChildren().addAll(snake.get());
        initHud();
        startTime = LocalDateTime.now();
        gamePlaying = true;
        animationTimer.start();
    }
    
    private void endGame() {
        var segmentCount = snake.get().size();
        score.setFill(snake.getColor());
        score.setText(String.format("%03d", ((segmentCount == 3) ? 0 : segmentCount)));
        root.getChildren().add(score);
        animationTimer.stop();
    }
    
    private void initializeSceneGraph() {
        initHud();
        root.setPrefSize(App.WIDTH, App.HEIGHT);
        root.getChildren().addAll(snake.get());
        scene = new Scene(root);
        scene.setFill(Color.BLACK);
        scene.setCursor(Cursor.NONE);
        setSceneController();
    }

    private void initHud() {
        splashText = new SplashText("Press ENTER/SPACE To Start/Restart", 10.0, 25.0, 16.0, true,
                SplashText.ColorStyle.GRADIENT, "/font/Orbitron-Bold.ttf");
        // one color with font
//        splashText = new SplashText("Press ENTER/SPACE To Start/Restart", 10.0, 25.0, 16.0, 
//                Color.ORANGE, SEPARATOR + "font" + SEPARATOR + "Orbitron-Bold.ttf");
        var gameTimer = new GameTimer(
                10.0, 15.0, 12.0, Color.color(1.0, 1.0, 1.0, 0.85), "/font/Orbitron-Regular.ttf");
        timer = gameTimer.get();
        var gameScore = new GameScore(App.WIDTH - 88, 40.0, 32.0, "/font/Orbitron-Bold.ttf");
        score = gameScore.get();

        root.getChildren().add(timer);
        if (isSplash) {
            if (splashText.isText()) {
                root.getChildren().add(splashText.get());
            } else {
                root.getChildren().add(splashText.getTextFlow());
            }
        }
    }
    
    private String formatTime() {
        var timeDiff = Duration.between(startTime, LocalDateTime.now());
        return String.format("%02d:%02d", timeDiff.toMinutesPart(), timeDiff.toSecondsPart());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
