package com.natay.games.snake.core.service;


import com.natay.games.snake.core.model.Food;
import javafx.geometry.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Random;

import static com.natay.games.snake.core.common.Validation.requireArgument;
import static java.util.Objects.nonNull;

/**
 * @author natayeung
 */
public class RandomFoodProducer implements FoodProducer {

    private static final Logger logger = LoggerFactory.getLogger(RandomFoodProducer.class);

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

        final Food food = nonNull(excludingPositions) && excludingPositions.contains(position)
                ? nextFoodExcludingPositions(excludingPositions) : new Food(position);
        logger.debug("Produced {}", food);

        return food;
    }
}