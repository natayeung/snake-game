package com.nata.games.snake;

import com.nata.games.snake.model.Food;
import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author natayeung
 */
class RandomFoodProducer implements FoodProducer {

    private final Random random = new Random(System.currentTimeMillis());
    private final int xBound;
    private final int yBound;

    RandomFoodProducer(int xBound, int yBound) {
        checkArgument(xBound > 0, "xBound must be positive");
        checkArgument(yBound > 0, "yBound must be positive");

        this.xBound = xBound;
        this.yBound = yBound;
    }

    public Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions) {
        int x = random.nextInt(xBound);
        int y = random.nextInt(yBound);
        var position = new Point2D(x, y);

        return excludingPositions != null && excludingPositions.contains(position) ? nextFoodExcludingPositions(excludingPositions) : new Food(position);
    }
}
