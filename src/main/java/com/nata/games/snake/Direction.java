package com.nata.games.snake;

import javafx.geometry.Point2D;

/**
 * @author natayeung
 */
enum Direction {
    UP(new Point2D(1, 0)),
    DOWN(new Point2D(-1, 0)),
    LEFT(new Point2D(0, -1)),
    RIGHT(new Point2D(0, 1));

    private final Point2D vector;

    Direction(Point2D vector) {
        this.vector = vector;
    }

    Point2D vector() {
        return vector;
    }
}
