package com.natay.games.snake.core.service;

import com.natay.games.snake.core.model.Food;
import javafx.geometry.Point2D;

import java.util.Collection;

/**
 * @author natayeung
 */
public interface FoodProducer {

    Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions);
}