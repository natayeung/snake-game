package com.natay.games.snake.ui;

import com.natay.games.snake.dto.GameState;

public interface StateChangeObserver {

    void stateChanged(GameState gameState);
}
