package com.natay.games.snake.core.service;

import com.natay.games.snake.core.common.Direction;
import com.natay.games.snake.core.model.Food;
import com.natay.games.snake.core.model.Snake;
import javafx.geometry.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * @author natayeung
 */
public class GameExecutionService implements GameExecutor, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GameExecutionService.class);

    private final FoodProducer foodProducer;
    private final GameMoveScheduler gameMoveScheduler;
    private final ExecutionContext executionContext;

    private GameStateChangeListener stateChangeListener;
    private Snake snake;
    private Food food;
    private int score;
    private boolean foodCaughtOnLastMove;
    private boolean gameOver;

    public GameExecutionService(FoodProducer foodProducer, GameMoveScheduler gameMoveScheduler, ExecutionContext executionContext) {
        this.foodProducer = requireNonNull(foodProducer, "Food producer must be specified");
        this.gameMoveScheduler = requireNonNull(gameMoveScheduler, "Game move scheduler must be specified");
        this.executionContext = requireNonNull(executionContext, "Execution context must be specified");

        initialize();
    }

    @Override
    public GameState startGame() {
        final GameState initialGameState = getGameState();
        gameMoveScheduler.start(this, executionContext.schedulingContext());

        return initialGameState;
    }

    @Override
    public GameState restartGame() {
        initialize();
        final GameState initialGameState = getGameState();

        gameMoveScheduler.stop();
        gameMoveScheduler.start(this, executionContext.schedulingContext());

        return initialGameState;
    }

    @Override
    public void changeSnakeMovingDirection(Direction newDirection) {
        if (!newDirection.isOppositeWith(snake.getMovingDirection())) {
            snake.changeMovingDirection(newDirection);

            logger.debug("Moving direction updated to {}", snake.getMovingDirection());
        }
    }

    @Override
    public void setStateChangeListener(GameStateChangeListener stateChangeListener) {
        this.stateChangeListener = requireNonNull(stateChangeListener, "State change listener must be specified");
    }

    @Override
    public void run() {
        snake.move();
        evaluateOutcome();

        if (nonNull(stateChangeListener)) {
            stateChangeListener.onGameStateChange(getGameState());
        }
    }

    public Direction getSnakeMovingDirection() {
        return snake.getMovingDirection();
    }

    private void initialize() {
        snake = newSnake();
        food = newFoodNotInCollisionWith(snake);
        score = 0;
        gameOver = false;
    }

    private Snake newSnake() {
        final int totalTilesX = executionContext.totalTilesX();
        final int totalTilesY = executionContext.totalTilesY();
        final Point2D head = new Point2D(totalTilesX / 2.0, totalTilesY / 2.0);
        return new Snake(head, executionContext.initialMovingDirection());
    }

    private Food newFoodNotInCollisionWith(Snake snake) {
        final Food food = foodProducer.nextFoodExcludingPositions(snake.getBody());
        logger.debug("Produced food {}", food);

        return food;
    }

    private GameState getGameState() {
        final GameState gameState = GameState.newBuilder()
                .withSnake(snake.getBody())
                .withFood(food.getPosition())
                .withScore(score)
                .isFoodCaughtOnLastMove(foodCaughtOnLastMove)
                .isGameOver(gameOver)
                .withSpeedIndication(computeSpeedIndication())
                .build();

        logger.debug("Game state={}", gameState);

        return gameState;
    }

    private double computeSpeedIndication() {
        final SchedulingContext schedulingContext = executionContext.schedulingContext();
        final Duration initialMoveInterval = schedulingContext.initialMoveInterval();
        final Duration minMoveInterval = schedulingContext.minMoveInterval();
        final Duration moveInterval = gameMoveScheduler.getMoveInterval().orElse(initialMoveInterval);

        final double progress = initialMoveInterval.minus(moveInterval).toMillis();
        final double whole = initialMoveInterval.minus(minMoveInterval).toMillis();
        return progress / whole;
    }

    private void evaluateOutcome() {
        final int totalTilesX = executionContext.totalTilesX();
        final int totalTilesY = executionContext.totalTilesY();

        foodCaughtOnLastMove = false;

        if (snake.isCollidingWithBody() || snake.isCollidingWithEdgeOfBoard(totalTilesX, totalTilesY)) {
            gameOver = true;

            gameMoveScheduler.stop();
        } else if (snake.isCollidingWith(food)) {
            snake.grow();
            score++;
            foodCaughtOnLastMove = true;
            food = newFoodNotInCollisionWith(snake);

            gameMoveScheduler.updateMoveIntervalIfNextMilestoneReached(score);
        }
    }
}
