package com.natay.games.snake.model;

import javafx.geometry.Point2D;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author natayeung
 */
public class Food {

    private final Point2D position;

    public Food(Point2D position) {
        this.position = checkNotNull(position, "Position must be specified");
    }

    public Point2D getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Food{" +
                "position=" + position +
                '}';
    }
}
