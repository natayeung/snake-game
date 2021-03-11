package com.nata.games.snake;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.nata.games.snake.GameParameters.*;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class TilesGridUpdater implements SnakeGameUserInterface.View.ComponentInitializer, SnakeGameUserInterface.View.StateChangeObserver {

    private final Map<Point2D, Rectangle> tilesByCoordinates = new HashMap<>(TOTAL_TILES_X * TOTAL_TILES_Y);

    public TilesGridUpdater() {
    }

    @Override
    public Node initialize() {
        final GridPane tilesGrid = new GridPane();
        for (int i = 0; i < TOTAL_TILES_X; i++) {
            for (int j = 0; j < TOTAL_TILES_Y; j++) {
                final Rectangle tile = newTile(i, j);
                tilesGrid.add(tile, i, j);
            }
        }
        return tilesGrid;
    }

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        updateTilesGrid(gameState);
    }

    private Rectangle newTile(int x, int y) {
        final Rectangle tile = new Rectangle(x, y, TILE_SIZE_PX, TILE_SIZE_PX);
        tile.setFill(EMPTY_COLOR);
        tilesByCoordinates.put(new Point2D(x, y), tile);
        return tile;
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