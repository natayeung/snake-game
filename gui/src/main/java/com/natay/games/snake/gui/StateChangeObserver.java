package com.natay.games.snake.gui;


import com.natay.games.snake.core.service.GameState;

public interface StateChangeObserver {

    void stateChanged(GameState gameState);
}
