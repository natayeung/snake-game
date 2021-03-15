package com.natay.games.snake.core.service;


import com.natay.games.snake.core.model.Food;
import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Random;

import static com.natay.games.snake.core.common.Validation.requireArgument;
import static java.util.Objects.nonNull;

/**
 * @author natayeung
 */
public class RandomFoodProducer implements FoodProducer {

    private final Random random = new Random(System.currentTimeMillis());
    private final int xBound;
    private final int yBound;

    public RandomFoodProducer(int xBound, int yBound) {
        requireArgument(xBound > 0, "xBound must be positive");
        requireArgument(yBound > 0, "yBound must be positive");

        this.xBound = xBound;
        this.yBound = yBound;
    }

    public Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions) {
        final int x = random.nextInt(xBound);
        final int y = random.nextInt(yBound);
        final Point2D position = new Point2D(x, y);

        return nonNull(excludingPositions) && excludingPositions.contains(position) ? nextFoodExcludingPositions(excludingPositions) : new Food(position);
    }
}