package com.natay.games.snake.model;

import javafx.geometry.Point2D;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public enum Direction {
    UP(new Point2D(0, -1)),
    DOWN(new Point2D(0, 1)),
    LEFT(new Point2D(-1, 0)),
    RIGHT(new Point2D(1, 0));

    private final Point2D vector;

    Direction(Point2D vector) {
        this.vector = vector;
    }

    public Point2D vector() {
        return vector;
    }

    public boolean isOppositeWith(Direction anotherDirection) {
        if (isNull(anotherDirection))
            return false;

        return this.vector.add(anotherDirection.vector).equals(Point2D.ZERO);
    }
}
