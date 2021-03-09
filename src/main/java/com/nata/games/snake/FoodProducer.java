package com.nata.games.snake;

import javafx.geometry.Point2D;

import java.util.Collection;

interface FoodProducer {

    Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions);
}
