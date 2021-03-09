package com.nata.games.snake;

import com.nata.games.snake.model.Food;
import javafx.geometry.Point2D;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.in;
import static org.hamcrest.core.IsNot.not;

/**
 * @author natayeung
 */
public class RandomFoodProducerTest {

    private RandomFoodProducer foodProducer;

    @Before
    public void setUp() {
        foodProducer = new RandomFoodProducer(2, 2);
    }

    @Test
    public void canProduceFoodExcludingSpecifiedPositions() {
        Collection<Point2D> excludingPositions = List.of(new Point2D(0, 0), new Point2D(0, 1), new Point2D(1, 0));

        Food food = foodProducer.nextFoodExcludingPositions(excludingPositions);

        assertThat(food.getPosition(), not(in(excludingPositions)));
    }
}
