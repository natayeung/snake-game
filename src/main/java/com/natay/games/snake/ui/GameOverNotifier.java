package com.natay.games.snake.ui;

import com.natay.games.snake.GameParameters;
import com.natay.games.snake.dto.GameState;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

/**
 * @author natayeung
 */
class GameOverNotifier implements StateChangeObserver {

    private final GameRestartable gameRestartable;

    GameOverNotifier(GameRestartable gameRestartable) {
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
        final ButtonType retry = new ButtonType(GameParameters.DisplayText.RETRY, OK_DONE);
        final ButtonType quit = new ButtonType(GameParameters.DisplayText.QUIT, CANCEL_CLOSE);
        final Alert dialog = newGameOverDialog(retry, quit);

        final Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.orElse(quit) == retry) {
            gameRestartable.restartGame();
        } else {
            Platform.exit();
        }
    }

    private Alert newGameOverDialog(ButtonType retry, ButtonType quit) {
        final Alert dialog = new Alert(Alert.AlertType.NONE, GameParameters.DisplayText.GAME_OVER_MESSAGE, retry, quit);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource(GameParameters.Resources.STYLESHEET).toExternalForm());
        dialog.getDialogPane().getStyleClass().add("dialog-pane");
        return dialog;
    }
}