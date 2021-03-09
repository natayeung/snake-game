package com.nata.games.snake;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nata.games.snake.GameParameters.*;
import static com.nata.games.snake.GameParameters.DisplayText.SCORE;
import static java.util.Objects.isNull;
import static javafx.geometry.Pos.BOTTOM_CENTER;
import static javafx.geometry.Pos.TOP_RIGHT;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.scene.layout.Border.EMPTY;

/**
 * @author natayeung
 * Credits: sound from zapsplat
 */
public class GameBoard implements SnakeGameUserInterface.View, EventHandler<KeyEvent>, SnakeGameUserInterface.GameRestartable {

    private static final Logger logger = LoggerFactory.getLogger(GameBoard.class);

    private final Stage stage;
    private final List<StateChangeObserver> stateChangeObservers = new ArrayList<>();
    private final Map<Point2D, Rectangle> tilesByCoordinates = new HashMap<>(TOTAL_TILES_X * TOTAL_TILES_Y);

    private SnakeGameUserInterface.Presenter presenter;
    private Text scoreValue;
    private ProgressBar speedIndicator;

    public GameBoard(Stage stage) {
        this.stage = stage;

        initializeUI();
        addStateChangeObservers();
    }

    @Override
    public void setPresenter(SnakeGameUserInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initializeGameBoard(GameState gameState) {
        logger.info("Initializing game board, {}", gameState);

        refreshBoard(gameState);
    }

    @Override
    public void updateGameBoard(GameState gameState) {
        logger.debug("Updating game board, {}", gameState);

        refreshBoard(gameState);
    }

    @Override
    public void handle(KeyEvent event) {
        if (isNull(event))
            return;

        if (event.getEventType() == KEY_PRESSED && event.getCode().isArrowKey()) {
            presenter.onMovingDirectionUpdate(event.getCode());
        }

        event.consume();
    }

    @Override
    public void restartGame() {
        presenter.onGameRestart();
    }

    private void initializeUI() {
        final Pane basePane = newBasePane();
        stage.setScene(newSceneWith(basePane));

        basePane.getChildren().addAll(newScorePane(),
                newTilesGrid(),
                newSpeedIndicatorPane());

        stage.show();
    }

    private Scene newSceneWith(Parent parent) {
        final Scene scene = new Scene(parent, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(this);
        return scene;
    }

    private Pane newBasePane() {
        final Pane pane = new VBox();
        pane.setBackground(newWindowBackground());
        pane.setPadding(new Insets(BOARD_PADDING_PX));
        return pane;
    }

    private Pane newSpeedIndicatorPane() {
        final Pane speedPane = newInfoDisplayPane(BOTTOM_CENTER);
        speedIndicator = new ProgressBar(0);
        speedIndicator.setBorder(EMPTY);
        speedPane.getChildren().add(speedIndicator);

        return speedPane;
    }

    private Pane newScorePane() {
        final Pane scorePane = newInfoDisplayPane(TOP_RIGHT);
        final Text scoreDisplayName = newDisplayText(SCORE);
        scoreValue = newDisplayText("");

        scorePane.getChildren().addAll(scoreDisplayName, scoreValue);

        return scorePane;
    }

    private Pane newInfoDisplayPane(Pos alignment) {
        final HBox pane = new HBox();
        pane.setPrefHeight(INFO_DISPLAY_HEIGHT);
        pane.setAlignment(alignment);
        pane.setLayoutX(BOARD_PADDING_PX);
        pane.setLayoutY(BOARD_PADDING_PX);

        return pane;
    }

    private Text newDisplayText(String text) {
        final Text displayText = new Text(text);
        displayText.setFont(TEXT_FONT);
        displayText.setFill(TEXT_COLOR);
        return displayText;
    }

    private Background newWindowBackground() {
        return new Background((new BackgroundFill(WINDOW_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private Pane newTilesGrid() {
        final GridPane tilesGrid = new GridPane();
        tilesGrid.setLayoutX(BOARD_PADDING_PX);
        tilesGrid.setLayoutY(BOARD_PADDING_PX);

        for (int i = 0; i < TOTAL_TILES_X; i++) {
            for (int j = 0; j < TOTAL_TILES_Y; j++) {
                final Rectangle tile = newTile(i, j);
                tilesGrid.add(tile, i, j);
            }
        }
        return tilesGrid;
    }

    private Rectangle newTile(int x, int y) {
        final Rectangle tile = new Rectangle(x, y, TILE_SIZE_PX, TILE_SIZE_PX);
        tile.setFill(TILE_COLOR);

        tilesByCoordinates.put(new Point2D(x, y), tile);

        return tile;
    }

    private void addStateChangeObservers() {
        stateChangeObservers.add(new TilesGridUpdater(tilesByCoordinates));
        stateChangeObservers.add(new ScoreUpdater(scoreValue));
        stateChangeObservers.add(new SoundPlayer());
        stateChangeObservers.add(new SpeedIndicationUpdater(speedIndicator));
        stateChangeObservers.add(new GameOverNotifier(this));
    }

    private void refreshBoard(GameState gameState) {
        for (StateChangeObserver observer : stateChangeObservers) {
            observer.stateChanged(gameState);
        }
    }
}