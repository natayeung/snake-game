package com.nata.games.snake;

import javafx.geometry.Point2D;

/**
 * @author natayeung
 */
enum Direction {
    UP(new Point2D(0, -1)),
    DOWN(new Point2D(0, 1)),
    LEFT(new Point2D(-1, 0)),
    RIGHT(new Point2D(1, 0));

    private final Point2D vector;

    Direction(Point2D vector) {
        this.vector = vector;
    }

    Point2D vector() {
        return vector;
    }

    boolean isOppositeWith(Direction anotherDirection) {
        if (anotherDirection == null)
            return false;

        return this.vector.add(anotherDirection.vector).equals(Point2D.ZERO);
    }
}
