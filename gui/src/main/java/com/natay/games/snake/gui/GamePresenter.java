package com.natay.games.snake.gui;

import javafx.scene.input.KeyCode;

/**
 * @author natayeung
 */
public interface GamePresenter {

    void onMovingDirectionUpdate(KeyCode code);

    void onGameRestart();
}
