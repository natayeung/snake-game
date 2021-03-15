package com.natay.games.snake.gui;


import com.natay.games.snake.gui.configuration.DisplayText;
import com.natay.games.snake.gui.configuration.ResourceNames;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author natayeung
 */
class HeaderRenderer implements NodeInitializer {

    private static final Logger logger = LoggerFactory.getLogger(HeaderRenderer.class);

    HeaderRenderer() {
    }

    @Override
    public Node initialize() {
        final HBox headerPane = UIUtils.newHorizontalPaneWithStyle("header-pane");
        headerPane.getChildren().addAll(newSnakeLabel(), newFiller(), newCloseButton());

        logger.info("Header initialized");

        return headerPane;
    }

    private Label newSnakeLabel() {
        final Label snakeLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(snakeLabel, ResourceNames.SNAKE_ICON, DisplayText.TITLE);

        return snakeLabel;
    }

    private Button newCloseButton() {
        final Button closeButton = new Button();
        closeButton.getStyleClass().add("close-button");
        closeButton.setFocusTraversable(false); // This is so that the scene can receive arrow key events.
        closeButton.setOnMouseClicked(mouseEvent -> Platform.exit());

        UIUtils.setIconIfFoundOrElseSetText(closeButton, ResourceNames.CLOSE_BUTTON_ICON, DisplayText.CLOSE);

        return closeButton;
    }

    private Region newFiller() {
        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        return filler;
    }
}
