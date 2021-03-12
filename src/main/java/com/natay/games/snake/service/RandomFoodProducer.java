package com.natay.games.snake.service;

import com.natay.games.snake.model.Food;
import com.natay.games.snake.service.FoodProducer;
import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class RandomFoodProducer implements FoodProducer {

    private final Random random = new Random(System.currentTimeMillis());
    private final int xBound;
    private final int yBound;

    public RandomFoodProducer(int xBound, int yBound) {
        checkArgument(xBound > 0, "xBound must be positive");
        checkArgument(yBound > 0, "yBound must be positive");

        this.xBound = xBound;
        this.yBound = yBound;
    }

    public Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions) {
        final int x = random.nextInt(xBound);
        final int y = random.nextInt(yBound);
        final Point2D position = new Point2D(x, y);

        return !isNull(excludingPositions) && excludingPositions.contains(position) ? nextFoodExcludingPositions(excludingPositions) : new Food(position);
    }
}