package com.natay.games.snake.core.model;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author natayeung
 */
public class FoodTest {

    @Test
    public void cannotBeInstantiatedWithNullPosition() {
        assertThrows(NullPointerException.class, () -> {
            new Food(null);
        });
    }

    @Test
    public void hasPosition() {
        Point2D position = new Point2D(3, 7);

        Food food = new Food(position);

        assertThat(food.getPosition(), is(position));
    }
}
