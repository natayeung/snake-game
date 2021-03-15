package com.natay.games.snake.core.model;

import javafx.geometry.Point2D;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author natayeung
 */
public class FoodTest {

    @Test(expected = NullPointerException.class)
    public void cannotBeInstantiatedWithNullPosition() {
        new Food(null);
    }

    @Test
    public void hasPosition() {
        Point2D position = new Point2D(3, 7);

        Food food = new Food(position);

        assertThat(food.getPosition(), is(position));
    }
}
