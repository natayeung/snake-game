package com.natay.games.snake.core.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author natayeung
 */
public class ValidationTest {

    @Test
    public void shouldThrowExceptionWhenExpressionIsFalse() {
        int x = 5;
        assertThrows(IllegalArgumentException.class,
                () -> Validation.requireArgument(x < 0, "Negative numbers expected"));
    }

    @Test
    public void shouldNotThrowAnyExceptionWhenExpressionIsTrue() {
        int x = -5;
        Assertions.assertDoesNotThrow(
                () -> Validation.requireArgument(x < 0, "Negative numbers expected"));
    }

    @Test
    public void shouldThrowExceptionWhenMapIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> Validation.requireNonEmpty(null, "Map cannot be empty"));
    }

    @Test
    public void shouldThrowExceptionWhenMapIsEmpty() {
        assertThrows(IllegalArgumentException.class,
                () -> Validation.requireNonEmpty(Map.of(), "Map cannot be empty"));
    }

    @Test
    public void shouldNotThrowAnyExceptionWhenMapIsNotEmpty() {
        Assertions.assertDoesNotThrow(
                () -> Validation.requireNonEmpty(Map.of("1", "Bla"), "Map cannot be empty"));
    }
}
