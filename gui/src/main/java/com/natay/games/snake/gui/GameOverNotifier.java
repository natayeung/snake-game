package com.natay.games.snake.gui;


import com.natay.games.snake.core.service.GameState;
import com.natay.games.snake.gui.configuration.DisplayText;
import com.natay.games.snake.gui.configuration.ResourceNames;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

/**
 * @author natayeung
 */
class GameOverNotifier implements StateChangeObserver {

    private final GameRestartable gameRestartable;

    GameOverNotifier(GameRestartable gameRestartable) {
        this.gameRestartable = requireNonNull(gameRestartable, "Game restartable must be specified");
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
        final ButtonType retry = new ButtonType(DisplayText.RETRY, OK_DONE);
        final ButtonType quit = new ButtonType(DisplayText.QUIT, CANCEL_CLOSE);
        final Alert dialog = newGameOverDialog(retry, quit);

        final Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.orElse(quit) == retry) {
            gameRestartable.restartGame();
        } else {
            Platform.exit();
        }
    }

    private Alert newGameOverDialog(ButtonType retry, ButtonType quit) {
        final Alert dialog = new Alert(Alert.AlertType.NONE, DisplayText.GAME_OVER_MESSAGE, retry, quit);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource(ResourceNames.STYLESHEET).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        return dialog;
    }
}