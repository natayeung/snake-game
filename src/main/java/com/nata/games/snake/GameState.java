package com.nata.games.snake;

import javafx.geometry.Point2D;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author natayeung
 */
public class GameState {

    private final Collection<Point2D> snake;
    private final Point2D food;
    private final GameStatus gameStatus;
    private final int score;

    public GameState(Collection<Point2D> snake, Point2D food, GameStatus gameStatus, int score) {
        checkArgument(score >= 0, "Score cannot be negative");

        this.snake = checkNotNull(snake, "Snake must be specified");
        this.food = checkNotNull(food, "Food must be specified");
        this.gameStatus = checkNotNull(gameStatus, "Game status must be specified");
        this.score = score;
    }

    public Collection<Point2D> getSnake() {
        return snake;
    }

    public Point2D getFood() {
        return food;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "snake=" + snake +
                ", food=" + food +
                ", gameStatus=" + gameStatus +
                ", score=" + score +
                '}';
    }
}