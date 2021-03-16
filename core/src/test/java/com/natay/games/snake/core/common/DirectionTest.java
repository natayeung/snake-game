package com.natay.games.snake.core.common;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author natayeung
 */
public class DirectionTest {

    @ParameterizedTest
    @CsvSource({
            "UP   ,DOWN",
            "DOWN ,UP",
            "LEFT ,RIGHT",
            "RIGHT,LEFT"
    })
    public void shouldReturnTrueIfOppositeWithTheOtherDirection(Direction thisDirection, Direction theOther) {

        assertTrue(thisDirection.isOppositeWith(theOther));
    }

    @ParameterizedTest
    @CsvSource({
            "UP   ,UP",
            "UP   ,LEFT",
            "UP   ,RIGHT",
            "DOWN ,DOWN",
            "DOWN ,LEFT",
            "DOWN ,RIGHT",
            "LEFT ,LEFT",
            "LEFT ,UP",
            "LEFT ,DOWN",
            "RIGHT,RIGHT",
            "RIGHT,UP",
            "RIGHT,DOWN"
    })
    public void shouldReturnFalseIfNotOppositeWithTheOtherDirection(Direction thisDirection, Direction theOther) {

        assertFalse(thisDirection.isOppositeWith(theOther));
    }
}
