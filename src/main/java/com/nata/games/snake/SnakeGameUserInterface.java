package com.nata.games.snake;

import javafx.scene.input.KeyCode;

public interface SnakeGameUserInterface {

    interface Presenter {
        void onMovingDirectionUpdate(KeyCode code);

        void onNextMove();

        void onGameRestart();
    }

    interface View {
        void setPresenter(Presenter presenter);

        void initializeGameBoard(GameState gameState);

        void updateGameBoard(GameState gameState);
    }
}
