package com.natay.games.snake;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author natayeung
 */
public class GameBuilder {

    public static void build(Stage stage) {
        final SnakeGameUserInterface.View gameView = new GameBoard(stage);

        final FoodProducer foodProducer = new RandomFoodProducer(GameParameters.TOTAL_TILES_X, GameParameters.TOTAL_TILES_Y);
        final GameMoveSchedulable gameMoveScheduler = new GameMoveScheduler(newScheduledExecutor());
        final Map<KeyCode, Direction> inputKeyDirectionMapping = initKeyCodeDirectionMap();
        final SnakeGameUserInterface.Presenter gameEngine = new GameEngine(gameView, foodProducer, gameMoveScheduler, inputKeyDirectionMapping);

        gameView.setPresenter(gameEngine);
    }

    private static Map<KeyCode, Direction> initKeyCodeDirectionMap() {
        return Map.of(KeyCode.UP, Direction.UP, KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT, KeyCode.RIGHT, Direction.RIGHT);
    }

    private static ScheduledExecutorService newScheduledExecutor() {
        return Executors.newSingleThreadScheduledExecutor(GameBuilder::newDaemonThread);
    }

    private static Thread newDaemonThread(Runnable task) {
        final Thread thread = new Thread(task);
        thread.setDaemon(true);
        return thread;
    }

    private GameBuilder() {
    }
}