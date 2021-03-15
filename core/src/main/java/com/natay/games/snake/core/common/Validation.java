package com.natay.games.snake.core.common;

import java.util.Map;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class Validation {

    public static void requireArgument(boolean expression, String reason) {
        if (!expression) {
            throw new IllegalArgumentException(reason);
        }
    }

    public static <K, V> Map<K, V> requireNonEmpty(Map<K, V> map, String reason) {
        if (isNull(map) || map.isEmpty()) {
            throw new IllegalArgumentException(reason);
        }
        return map;
    }

    private Validation() {
    }
}
