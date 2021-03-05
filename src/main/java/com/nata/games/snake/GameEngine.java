package com.nata.games.snake;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.Direction.RIGHT;
import static com.nata.games.snake.GameParameters.TOTAL_TILES_X;
import static com.nata.games.snake.GameParameters.TOTAL_TILES_Y;
import static com.nata.games.snake.GameStatus.GAME_OVER;
import static com.nata.games.snake.GameStatus.IN_PROGRESS;
import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * @author natayeung
 */
public class GameEngine implements SnakeGameUserInterface.EventListener {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private static final Direction SNAKE_DEFAULT_MOVING_DIRECTION = RIGHT;
    private final SnakeGameUserInterface.View gameView;
    private final RandomFoodProducer randomFoodProducer;
    private final Map<KeyCode, Direction> directionsByInputKey;
    private Snake snake;
    private Food food;
    private int score;
    private GameStatus gameStatus;
    private boolean foodCaughtOnLastMove;
    private Direction movingDirection;

    public GameEngine(SnakeGameUserInterface.View gameView, RandomFoodProducer randomFoodProducer,
                      Map<KeyCode, Direction> inputKeyDirectionMapping) {
        isEmpty(inputKeyDirectionMapping);

        this.gameView = checkNotNull(gameView);
        this.randomFoodProducer = checkNotNull(randomFoodProducer);
        this.directionsByInputKey = inputKeyDirectionMapping;

        setUpNewGame();
    }

    @Override
    public void onMovingDirectionUpdate(KeyCode code) {
        logger.debug("Received input key {}", code);

        if (!directionsByInputKey.containsKey(code))
            return;

        Direction attemptedDirection = directionsByInputKey.get(code);

        if (!attemptedDirection.isOppositeWith(movingDirection)) {
            snake.changeMovingDirection(attemptedDirection);
            movingDirection = attemptedDirection;
            logger.info("Moving direction changed to {}", movingDirection);
        }
    }

    @Override
    public void onNextMove() {
        logger.debug("Making next move ...");

        snake.move();

        foodCaughtOnLastMove = false;
        if (snake.isCollidingWithBody() || snake.isCollidingWithEdgeOfBoard(TOTAL_TILES_X, TOTAL_TILES_Y)) {
            gameStatus = GAME_OVER;
        } else if (snake.isCollidingWith(food)) {
            snake.grow();
            score++;
            foodCaughtOnLastMove = true;
            food = newFoodNotInCollisionWith(snake);
        }

        gameView.updateGameBoard(newGameState());
    }

    @Override
    public void onGameRestart() {
        setUpNewGame();
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    private void setUpNewGame() {
        movingDirection = SNAKE_DEFAULT_MOVING_DIRECTION;
        snake = newSnake(movingDirection);
        food = newFoodNotInCollisionWith(snake);
        score = 0;
        gameStatus = IN_PROGRESS;

        gameView.initGameBoard(newGameState());
    }

    private Snake newSnake(Direction movingDirection) {
        Point2D head = new Point2D(TOTAL_TILES_X / 2, TOTAL_TILES_Y / 2);
        return new Snake(head, movingDirection);
    }

    private Food newFoodNotInCollisionWith(Snake snake) {
        Food food = randomFoodProducer.nextFoodExcludingPositions(snake.getBody());
        logger.debug("Produced food {}", food);

        return food;
    }

    private GameState newGameState() {
        return GameState.newBuilder().withSnake(snake.getBody())
                .withFood(food.getPosition())
                .withGameStatus(gameStatus)
                .withScore(score)
                .withFoodCaughtOnLastMove(foodCaughtOnLastMove)
                .build();
    }
}
