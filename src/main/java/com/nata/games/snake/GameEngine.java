package com.nata.games.snake;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.Direction.RIGHT;
import static com.nata.games.snake.GameParameters.*;
import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * @author natayeung
 */
public class GameEngine implements SnakeGameUserInterface.Presenter, SnakeMoveExecutable {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);
    private static final Direction DEFAULT_MOVING_DIRECTION = RIGHT;

    private final SnakeGameUserInterface.View gameView;
    private final FoodProducer foodProducer;
    private final Map<KeyCode, Direction> directionsByInputKey;
    private final GameMoveSchedulable gameMoveScheduler;

    private Snake snake;
    private Food food;
    private int score;
    private boolean foodCaughtOnLastMove;
    private boolean gameOver;
    private Direction movingDirection;

    public GameEngine(SnakeGameUserInterface.View gameView,
                      FoodProducer foodProducer,
                      GameMoveSchedulable gameMoveScheduler,
                      Map<KeyCode, Direction> inputKeyDirectionMapping) {
        isEmpty(inputKeyDirectionMapping);

        this.gameView = checkNotNull(gameView, "Game view must be specified");
        this.foodProducer = checkNotNull(foodProducer, "Food producer must be specified");
        this.gameMoveScheduler = checkNotNull(gameMoveScheduler, "Game move scheduler must be specified");
        this.directionsByInputKey = inputKeyDirectionMapping;

        setUpNewGame();
    }

    @Override
    public void onMovingDirectionUpdate(KeyCode code) {
        logger.debug("Received input key {}", code);

        if (!directionsByInputKey.containsKey(code))
            return;

        final Direction attemptedDirection = directionsByInputKey.get(code);

        if (!attemptedDirection.isOppositeWith(movingDirection)) {
            snake.changeMovingDirection(attemptedDirection);
            movingDirection = attemptedDirection;

            logger.debug("Moving direction updated to {}", movingDirection);
        }
    }

    @Override
    public void onGameRestart() {
        setUpNewGame();
    }

    @Override
    public void run() {
        executeNextMove();
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    private void setUpNewGame() {
        initialize();
        gameView.initializeGameBoard(newGameState());
        gameMoveScheduler.start(this, INITIAL_MOVE_INTERVAL);
    }

    private void initialize() {
        movingDirection = DEFAULT_MOVING_DIRECTION;
        snake = newSnake(movingDirection);
        food = newFoodNotInCollisionWith(snake);
        score = 0;
        gameOver = false;
    }

    private Snake newSnake(Direction movingDirection) {
        final Point2D head = new Point2D(TOTAL_TILES_X / 2, TOTAL_TILES_Y / 2);
        return new Snake(head, movingDirection);
    }

    private Food newFoodNotInCollisionWith(Snake snake) {
        final Food food = foodProducer.nextFoodExcludingPositions(snake.getBody());
        logger.debug("Produced food {}", food);

        return food;
    }

    private GameState newGameState() {
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
        final Duration moveInterval = gameMoveScheduler.getMoveInterval().orElse(INITIAL_MOVE_INTERVAL);
        final double progress = INITIAL_MOVE_INTERVAL.toMillis() - moveInterval.toMillis();
        final double whole = INITIAL_MOVE_INTERVAL.toMillis() - MIN_MOVE_INTERVAL.toMillis();
        return progress / whole;
    }

    private void executeNextMove() {
        snake.move();
        evaluateOutcome();
        gameView.updateGameBoard(newGameState());
    }

    private void evaluateOutcome() {
        foodCaughtOnLastMove = false;

        if (snake.isCollidingWithBody() || snake.isCollidingWithEdgeOfBoard(TOTAL_TILES_X, TOTAL_TILES_Y)) {
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
