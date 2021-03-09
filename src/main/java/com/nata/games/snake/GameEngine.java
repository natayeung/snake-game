package com.nata.games.snake;

import com.nata.games.snake.model.Direction;
import com.nata.games.snake.model.Food;
import com.nata.games.snake.model.Snake;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.GameParameters.TOTAL_TILES_X;
import static com.nata.games.snake.GameParameters.TOTAL_TILES_Y;
import static com.nata.games.snake.model.Direction.RIGHT;
import static org.apache.commons.collections4.MapUtils.isEmpty;

/**
 * @author natayeung
 */
public class GameEngine implements SnakeGameUserInterface.Presenter {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private static final Direction SNAKE_DEFAULT_MOVING_DIRECTION = RIGHT;
    private final SnakeGameUserInterface.View gameView;
    private final FoodProducer foodProducer;
    private final Map<KeyCode, Direction> directionsByInputKey;
    private Snake snake;
    private Food food;
    private int score;
    private boolean foodCaughtOnLastMove;
    private boolean gameOver;
    private Direction movingDirection;

    public GameEngine(SnakeGameUserInterface.View gameView, FoodProducer foodProducer,
                      Map<KeyCode, Direction> inputKeyDirectionMapping) {
        isEmpty(inputKeyDirectionMapping);

        this.gameView = checkNotNull(gameView);
        this.foodProducer = checkNotNull(foodProducer);
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

            logger.debug("Moving direction updated to {}", movingDirection);
        }
    }

    @Override
    public void onNextMove() {
        logger.debug("Making next move ...");

        snake.move();

        foodCaughtOnLastMove = false;
        if (snake.isCollidingWithBody() || snake.isCollidingWithEdgeOfBoard(TOTAL_TILES_X, TOTAL_TILES_Y)) {
            gameOver = true;
        } else if (snake.isCollidingWith(food)) {
            snake.grow();
            score++;
            foodCaughtOnLastMove = true;
            food = newFoodNotInCollisionWith(snake);

            logger.info("Score updated to {}", score);
        }

        final GameState gameState = newGameState();
        logger.debug("Game state updated {}", gameState);

        gameView.updateGameBoard(gameState);
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
        gameOver = false;

        gameView.initializeGameBoard(newGameState());
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
        return GameState.newBuilder().withSnake(snake.getBody())
                .withFood(food.getPosition())
                .withScore(score)
                .isFoodCaughtOnLastMove(foodCaughtOnLastMove)
                .isGameOver(gameOver)
                .build();
    }
}
