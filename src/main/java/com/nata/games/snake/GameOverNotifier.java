package com.nata.games.snake;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.GameParameters.DisplayText.*;
import static java.util.Objects.isNull;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

/**
 * @author natayeung
 */
public class GameOverNotifier implements SnakeGameUserInterface.View.StateChangeObserver {

    private final SnakeGameUserInterface.GameRestartable gameRestartable;

    public GameOverNotifier(SnakeGameUserInterface.GameRestartable gameRestartable) {
        this.gameRestartable = checkNotNull(gameRestartable, "Game restartable must be specified");
    }

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        if (gameState.isGameOver()) {
            Platform.runLater(this::showGameOverDialog);
        }
    }

    private void showGameOverDialog() {
        final ButtonType newGame = new ButtonType(NEW_GAME, OK_DONE);
        final ButtonType exit = new ButtonType(EXIT, CANCEL_CLOSE);
        final Alert dialog = new Alert(Alert.AlertType.NONE, GAME_OVER_MESSAGE, newGame, exit);

        final Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.orElse(exit) == newGame) {
            gameRestartable.restartGame();
        } else {
            Platform.exit();
        }
    }
}