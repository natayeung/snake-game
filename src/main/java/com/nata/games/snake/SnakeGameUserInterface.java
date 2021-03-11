package com.nata.games.snake;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * @author natayeung
 */
public interface SnakeGameUserInterface {

    interface Presenter {
        void onMovingDirectionUpdate(KeyCode code);

        void onGameRestart();
    }

    interface View {
        void setPresenter(Presenter presenter);

        void initializeGameBoard(GameState gameState);

        void updateGameBoard(GameState gameState);

        interface ComponentInitializer {
            Node initialize();
        }

        interface StateChangeObserver {
            void stateChanged(GameState gameState);
        }
    }

    interface GameRestartable {
        void restartGame();
    }
}