package com.natay.games.snake.gui;

import com.natay.games.snake.core.common.Direction;
import com.natay.games.snake.core.service.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.natay.games.snake.core.common.Direction.RIGHT;
import static com.natay.games.snake.gui.configuration.Styles.TOTAL_TILES_X;
import static com.natay.games.snake.gui.configuration.Styles.TOTAL_TILES_Y;

/**
 * @author natayeung
 */
public class GameBuilder {

    private static final Duration INITIAL_MOVE_INTERVAL = Duration.ofMillis(500);
    private static final Duration MIN_MOVE_INTERVAL = Duration.ofMillis(50);
    private static final Duration MOVE_INTERVAL_DECREMENT = Duration.ofMillis(50);
    private static final int SCORE_MILESTONE_FOR_INTERVAL_CHANGE = 5;
    private static final Direction INITIAL_MOVING_DIRECTION = RIGHT;

    public static void build(Stage stage) {
        final GameView gameView = new GameBoard(stage);

        final FoodProducer foodProducer = new RandomFoodProducer(TOTAL_TILES_X, TOTAL_TILES_Y);
        final GameMoveScheduler gameMoveScheduler = new GameMoveSchedulingService(newScheduledExecutor());
        final ExecutionContext executionContext = newExecutionContext();
        final GameExecutor gameExecutor = new GameExecutionService(foodProducer, gameMoveScheduler, executionContext);

        final GameEngine gameEngine = new GameEngine(gameView, gameExecutor, inputKeyDirectionMapping());

        gameView.setPresenter(gameEngine);
        gameExecutor.setStateChangeListener(gameEngine);
    }

    private static Map<KeyCode, Direction> inputKeyDirectionMapping() {
        return Map.of(KeyCode.UP, Direction.UP,
                KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT,
                KeyCode.RIGHT, RIGHT);
    }

    private static ScheduledExecutorService newScheduledExecutor() {
        return Executors.newSingleThreadScheduledExecutor(GameBuilder::newDaemonThread);
    }

    private static ExecutionContext newExecutionContext() {
        final SchedulingContext schedulingContext = SchedulingContext.newBuilder()
                .withInitialMoveInterval(INITIAL_MOVE_INTERVAL)
                .withMinMoveInterval(MIN_MOVE_INTERVAL)
                .withMoveIntervalDecrement(MOVE_INTERVAL_DECREMENT)
                .withScoreMilestoneForIntervalChange(SCORE_MILESTONE_FOR_INTERVAL_CHANGE)
                .build();

        return ExecutionContext.newBuilder()
                .withSchedulingContext(schedulingContext)
                .withInitialMovingDirection(INITIAL_MOVING_DIRECTION)
                .withTotalTilesX(TOTAL_TILES_X)
                .withTotalTilesY(TOTAL_TILES_Y)
                .build();
    }

    private static Thread newDaemonThread(Runnable task) {
        final Thread thread = new Thread(task);
        thread.setDaemon(true);
        return thread;
    }

    private GameBuilder() {
    }
}