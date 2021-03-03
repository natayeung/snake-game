package com.nata.games.snake;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Map;

import static com.nata.games.snake.GameParameters.TOTAL_TILES_X;
import static com.nata.games.snake.GameParameters.TOTAL_TILES_Y;

/**
 * @author natayeung
 */
public class SnakeApplication extends Application {

    private SnakeGameUserInterface.View gameView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameView = new GameBoard(primaryStage);

        SnakeGameUserInterface.EventListener eventListener = new GameEngine(gameView, new RandomFoodProducer(TOTAL_TILES_X, TOTAL_TILES_Y),
                initKeyCodeDirectionMap());

        gameView.setEventListener(eventListener);
    }

    private static Map<KeyCode, Direction> initKeyCodeDirectionMap() {
        return Map.of(KeyCode.UP, Direction.UP, KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT, KeyCode.RIGHT, Direction.RIGHT);
    }
}
