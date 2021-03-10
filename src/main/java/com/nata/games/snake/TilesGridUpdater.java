package com.nata.games.snake;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.GameParameters.*;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class TilesGridUpdater implements SnakeGameUserInterface.View.StateChangeObserver {

    private final Map<Point2D, Rectangle> tilesByCoordinates;

    public TilesGridUpdater(Map<Point2D, Rectangle> tilesByCoordinates) {
        this.tilesByCoordinates = checkNotNull(tilesByCoordinates, "Tiles by coordinates map must be specified");
    }

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        updateTilesGrid(gameState);
    }

    private void updateTilesGrid(GameState gameState) {
        for (Rectangle tile : tilesByCoordinates.values()) {
            tile.setFill(EMPTY_COLOR);
        }

        final Collection<Point2D> snake = gameState.getSnake();
        for (Point2D p : snake) {
            getTileBy(p).ifPresent(tile -> tile.setFill(SNAKE_COLOR));
        }

        final Point2D food = gameState.getFood();
        getTileBy(food).ifPresent(tile -> tile.setFill(FOOD_COLOR));
    }

    private Optional<Rectangle> getTileBy(Point2D coordinates) {
        return Optional.ofNullable(tilesByCoordinates.get(coordinates));
    }
}