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
    private final int score;
    private final boolean foodCaughtOnLastMove;
    private final boolean gameOver;

    private GameState(Builder builder) {
        checkArgument(builder.score >= 0, "Score cannot be negative");

        snake = checkNotNull(builder.snake, "Snake must be specified");
        food = checkNotNull(builder.food, "Food must be specified");
        score = builder.score;
        foodCaughtOnLastMove = builder.foodCaughtOnLastMove;
        gameOver = builder.gameOver;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Collection<Point2D> getSnake() {
        return snake;
    }

    public Point2D getFood() {
        return food;
    }

    public int getScore() {
        return score;
    }

    public boolean isFoodCaughtOnLastMove() {
        return foodCaughtOnLastMove;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "snake=" + snake +
                ", food=" + food +
                ", score=" + score +
                ", foodCaughtOnLastMove=" + foodCaughtOnLastMove +
                ", gameOver=" + gameOver +
                '}';
    }

    public static final class Builder {
        private Collection<Point2D> snake;
        private Point2D food;
        private int score;
        private boolean foodCaughtOnLastMove;
        private boolean gameOver;

        private Builder() {
        }

        public Builder withSnake(Collection<Point2D> snake) {
            this.snake = snake;
            return this;
        }

        public Builder withFood(Point2D food) {
            this.food = food;
            return this;
        }

        public Builder withScore(int score) {
            this.score = score;
            return this;
        }

        public Builder isFoodCaughtOnLastMove(boolean foodCaughtOnLastMove) {
            this.foodCaughtOnLastMove = foodCaughtOnLastMove;
            return this;
        }

        public Builder isGameOver(boolean gameOver) {
            this.gameOver = gameOver;
            return this;
        }

        public GameState build() {
            return new GameState(this);
        }
    }
}