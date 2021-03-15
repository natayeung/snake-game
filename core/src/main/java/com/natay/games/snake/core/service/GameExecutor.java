package com.natay.games.snake.core.service;

import com.natay.games.snake.core.common.Direction;

/**
 * @author natayeung
 */
public interface GameExecutor {

    GameState startGame();

    GameState restartGame();

    void changeSnakeMovingDirection(Direction newDirection);

    void setStateChangeListener(GameStateChangeListener stateChangeListener);
}
