package com.natay.games.snake.core.service;

import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Objects;

import static com.natay.games.snake.core.common.Validation.requireArgument;
import static java.util.Objects.requireNonNull;


/**
 * @author natayeung
 */
public class GameState {

    private final Collection<Point2D> snake;
    private final Point2D food;
    private final int score;
    private final boolean foodCaughtOnLastMove;
    private final boolean gameOver;
    private final double speedIndication;

    private GameState(Builder builder) {
        requireArgument(builder.score >= 0, "Score cannot be negative");
        requireArgument(builder.speedIndication >= 0.0 && builder.speedIndication <= 1.0, "Speed indication must be between 0.0 and 1.0");

        snake = requireNonNull(builder.snake, "Snake must be specified");
        food = requireNonNull(builder.food, "Food must be specified");
        score = builder.score;
        foodCaughtOnLastMove = builder.foodCaughtOnLastMove;
        gameOver = builder.gameOver;
        speedIndication = builder.speedIndication;
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

    public double getSpeedIndication() {
        return speedIndication;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return score == gameState.score
                && foodCaughtOnLastMove == gameState.foodCaughtOnLastMove
                && gameOver == gameState.gameOver
                && Double.compare(gameState.speedIndication, speedIndication) == 0
                && snake.equals(gameState.snake)
                && food.equals(gameState.food);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snake, food, score, foodCaughtOnLastMove, gameOver, speedIndication);
    }

    @Override
    public String toString() {
        return "GameState{" +
                "snake=" + snake +
                ", food=" + food +
                ", score=" + score +
                ", foodCaughtOnLastMove=" + foodCaughtOnLastMove +
                ", gameOver=" + gameOver +
                ", speedIndication=" + speedIndication +
                '}';
    }

    public static final class Builder {
        private Collection<Point2D> snake;
        private Point2D food;
        private int score;
        private boolean foodCaughtOnLastMove;
        private boolean gameOver;
        private double speedIndication;

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

        public Builder withSpeedIndication(double speedIndication) {
            this.speedIndication = speedIndication;
            return this;
        }

        public GameState build() {
            return new GameState(this);
        }
    }
}