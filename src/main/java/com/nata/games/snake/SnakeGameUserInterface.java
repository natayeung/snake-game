package com.nata.games.snake;

import javafx.scene.input.KeyCode;

public interface SnakeGameUserInterface {

    interface EventListener {
        void onMovingDirectionUpdate(KeyCode code);

        void onNextMove();

        void onGameRestart();
    }

    interface View {
        void setEventListener(EventListener listener);

        void initGameBoard(GameState gameState);

        void updateGameBoard(GameState gameState);

        void showGameOverDialog();
    }
}
