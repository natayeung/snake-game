package com.nata.games.snake;

import com.nata.games.snake.model.Food;
import javafx.geometry.Point2D;

import java.util.Collection;

interface FoodProducer {

    Food nextFoodExcludingPositions(Collection<Point2D> excludingPositions);
}
