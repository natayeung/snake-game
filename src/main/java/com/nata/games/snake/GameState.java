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
    private final boolean foodCaughtOnLastMove;

    private GameState(Builder builder) {
        checkArgument(builder.score >= 0, "Score cannot be negative");

        snake = checkNotNull(builder.snake, "Snake must be specified");
        food = checkNotNull(builder.food, "Food must be specified");
        gameStatus = builder.gameStatus;
        score = builder.score;
        foodCaughtOnLastMove = builder.foodCaughtOnLastMove;
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

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getScore() {
        return score;
    }

    public boolean isFoodCaughtOnLastMove() {
        return foodCaughtOnLastMove;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "snake=" + snake +
                ", food=" + food +
                ", gameStatus=" + gameStatus +
                ", score=" + score +
                ", foodCaughtOnLastMove=" + foodCaughtOnLastMove +
                '}';
    }

    public static final class Builder {
        private Collection<Point2D> snake;
        private Point2D food;
        private GameStatus gameStatus;
        private int score;
        private boolean foodCaughtOnLastMove;

        private Builder() {
        }

        public Builder withSnake(Collection<Point2D> val) {
            snake = val;
            return this;
        }

        public Builder withFood(Point2D val) {
            food = val;
            return this;
        }

        public Builder withGameStatus(GameStatus val) {
            gameStatus = val;
            return this;
        }

        public Builder withScore(int val) {
            score = val;
            return this;
        }

        public Builder withFoodCaughtOnLastMove(boolean val) {
            foodCaughtOnLastMove = val;
            return this;
        }

        public GameState build() {
            return new GameState(this);
        }
    }
}