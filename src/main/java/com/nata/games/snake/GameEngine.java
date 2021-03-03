package com.nata.games.snake;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

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

    private static final Direction SNAKE_DEFAULT_MOVING_DIRECTION = RIGHT;
    private final SnakeGameUserInterface.View gameView;
    private final RandomFoodProducer randomFoodProducer;
    private final Map<KeyCode, Direction> directionsByInputKey;
    private Snake snake;
    private Food food;
    private int score;
    private GameStatus gameStatus;

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
        if (!directionsByInputKey.containsKey(code))
            return;

        Direction movingDirection = directionsByInputKey.get(code);

        snake.changeMovingDirection(movingDirection);
    }

    @Override
    public void onNextMove() {
        snake.move();

        if (snake.isCollidingWithBody() || snake.isCollidingWithEdgeOfBoard(TOTAL_TILES_X, TOTAL_TILES_Y)) {
            gameStatus = GAME_OVER;
        } else if (snake.isCollidingWith(food)) {
            snake.grow();
            score++;
            food = newFood();
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
        snake = newSnake();
        food = newFood();
        score = 0;
        gameStatus = IN_PROGRESS;

        gameView.initGameBoard(newGameState());
    }

    private Snake newSnake() {
        Point2D head = new Point2D(TOTAL_TILES_X / 2, TOTAL_TILES_Y / 2);
        return new Snake(head, SNAKE_DEFAULT_MOVING_DIRECTION);
    }

    private Food newFood() {
        return randomFoodProducer.nextFoodExcludingPositions(snake.getBody());
    }

    private GameState newGameState() {
        return new GameState(snake.getBody(), food.getPosition(), gameStatus, score);
    }
}
