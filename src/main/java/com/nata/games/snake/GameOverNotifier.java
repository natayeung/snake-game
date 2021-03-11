package com.nata.games.snake;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.GameParameters.DisplayText.*;
import static com.nata.games.snake.GameParameters.Resources.STYLESHEET;
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
        final ButtonType retry = new ButtonType(RETRY, OK_DONE);
        final ButtonType quit = new ButtonType(QUIT, CANCEL_CLOSE);
        final Alert dialog = new Alert(Alert.AlertType.NONE, GAME_OVER_MESSAGE, retry, quit);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");

        final Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.orElse(quit) == retry) {
            gameRestartable.restartGame();
        } else {
            Platform.exit();
        }
    }
}