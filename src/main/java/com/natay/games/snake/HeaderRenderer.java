package com.natay.games.snake;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * @author natayeung
 */
public class HeaderRenderer implements SnakeGameUserInterface.View.ComponentInitializer {

    @Override
    public Node initialize() {
        final HBox headerPane = UIUtils.newHorizontalPaneWithStyle("header-pane");
        final Label snakeLabel = newSnakeLabel();
        final Button closeButton = newCloseButton();
        final Region filler = newFiller();
        headerPane.getChildren().addAll(snakeLabel, filler, closeButton);

        return headerPane;
    }

    private Label newSnakeLabel() {
        final Label snakeLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(snakeLabel, GameParameters.Resources.SNAKE_ICON, GameParameters.DisplayText.TITLE);

        return snakeLabel;
    }

    private Button newCloseButton() {
        final Button closeButton = new Button();
        closeButton.getStyleClass().add("close-button");
        closeButton.setFocusTraversable(false); // This is so that the scene can receive arrow key events.
        closeButton.setOnMouseClicked(mouseEvent -> Platform.exit());

        UIUtils.setIconIfFoundOrElseSetText(closeButton, GameParameters.Resources.CLOSE_BUTTON_ICON, GameParameters.DisplayText.CLOSE);

        return closeButton;
    }

    private Region newFiller() {
        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        return filler;
    }
}
