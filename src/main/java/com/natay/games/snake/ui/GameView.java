package com.natay.games.snake.ui;

import com.natay.games.snake.dto.GameState;

/**
 * @author natayeung
 */
public interface GameView {

    void setPresenter(GamePresenter presenter);

    void initializeGameBoard(GameState gameState);

    void updateGameBoard(GameState gameState);
}
