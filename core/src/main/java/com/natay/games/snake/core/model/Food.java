package com.natay.games.snake.core.model;

import javafx.geometry.Point2D;

import static java.util.Objects.requireNonNull;


/**
 * @author natayeung
 */
public class Food {

    private final Point2D position;

    public Food(Point2D position) {
        this.position = requireNonNull(position, "Position must be specified");
    }

    public static Food at(int x, int y) {
        return new Food(new Point2D(x, y));
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
