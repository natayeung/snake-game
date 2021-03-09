package com.nata.games.snake;

import javafx.geometry.Point2D;

import java.util.Collection;

/**
 * @author natayeung
 */
interface FoodProducer {

    Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions);
}