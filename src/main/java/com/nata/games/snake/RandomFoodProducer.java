package com.nata.games.snake;

import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author natayeung
 */
class RandomFoodProducer {

    private final Random random = new Random(System.currentTimeMillis());
    private final int xBound;
    private final int yBound;

    RandomFoodProducer(int xBound, int yBound) {
        checkArgument(xBound > 0, "xBound must be positive");
        checkArgument(yBound > 0, "yBound must be positive");

        this.xBound = xBound;
        this.yBound = yBound;
    }

    Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions) {
        int x = random.nextInt(xBound);
        int y = random.nextInt(yBound);
        Point2D position = new Point2D(x, y);

        return excludingPositions != null && excludingPositions.contains(position) ? nextFoodExcludingPositions(excludingPositions) : new Food(position);
    }
}
