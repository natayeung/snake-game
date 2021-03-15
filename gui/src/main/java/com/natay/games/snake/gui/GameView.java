package com.natay.games.snake.gui;


import com.natay.games.snake.core.service.GameState;

/**
 * @author natayeung
 */
public interface GameView {

    void setPresenter(GamePresenter presenter);

    void initializeGameBoard(GameState gameState);

    void updateGameBoard(GameState gameState);
}
