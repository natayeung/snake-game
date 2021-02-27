package com.nata.games.snake;

import javafx.geometry.Point2D;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author natayeung
 */
public class Food {

    private Point2D position;

    public Food(Point2D position) {
        checkNotNull(position);

        this.position = position;
    }

    public Point2D getPosition() {
        return position;
    }
}
