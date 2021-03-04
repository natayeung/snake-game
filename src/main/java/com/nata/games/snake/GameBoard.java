package com.nata.games.snake;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.nata.games.snake.GameParameters.*;
import static com.nata.games.snake.GameParameters.DisplayText.*;
import static com.nata.games.snake.GameStatus.GAME_OVER;
import static com.nata.games.snake.GameStatus.IN_PROGRESS;
import static javafx.geometry.Pos.TOP_RIGHT;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

/**
 * @author natayeung
 */
public class GameBoard implements SnakeGameUserInterface.View, EventHandler<KeyEvent> {

    private final Stage stage;
    private final Text scoreValue = new Text();
    private final Map<Point2D, Rectangle> tilesByCoordinates;
    private SnakeGameUserInterface.EventListener eventListener;
    private GameState gameState;

    public GameBoard(Stage stage) {
        this.stage = stage;

        this.tilesByCoordinates = new HashMap<>(TOTAL_TILES_X * TOTAL_TILES_Y);
        initializeUI();
    }

    @Override
    public void setEventListener(SnakeGameUserInterface.EventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void initGameBoard(GameState gameState) {
        updateGameBoard(gameState);
        moveSnakePeriodically();
    }

    @Override
    public void updateGameBoard(GameState gameState) {
        this.gameState = gameState;
        refreshBoard();
    }

    @Override
    public void showGameOverDialog() {
        ButtonType newGame = new ButtonType(NEW_GAME, ButtonBar.ButtonData.OK_DONE);
        ButtonType exit = new ButtonType(EXIT, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert dialog = new Alert(Alert.AlertType.NONE, GAME_OVER_MESSAGE, newGame, exit);

        Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.orElse(exit) == newGame) {
            eventListener.onGameRestart();
        } else {
            Platform.exit();
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KEY_PRESSED && event.getCode().isArrowKey()) {
            eventListener.onMovingDirectionUpdate(event.getCode());
        }
        event.consume();
    }

    private void initializeUI() {
        Pane basePane = newBasePane();
        stage.setScene(newSceneWith(basePane));

        basePane.getChildren().add(newScorePane());
        basePane.getChildren().add(newTilesGrid());

        stage.show();
    }

    private Scene newSceneWith(Parent parent) {
        Scene scene = new Scene(parent, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(this);
        return scene;
    }

    private Pane newBasePane() {
        VBox pane = new VBox();
        pane.setBackground(newWindowBackground());
        pane.setPadding(new Insets(BOARD_PADDING_PX));
        return pane;
    }

    private Pane newScorePane() {
        HBox scorePane = new HBox();
        scorePane.setPrefHeight(SCORE_DISPLAY_HEIGHT);
        scorePane.setAlignment(TOP_RIGHT);
        scorePane.setLayoutX(BOARD_PADDING_PX);
        scorePane.setLayoutY(BOARD_PADDING_PX);

        Text scoreDisplayName = new Text(SCORE);
        scoreDisplayName.setFont(TEXT_FONT);
        scorePane.getChildren().addAll(scoreDisplayName, scoreValue);

        return scorePane;
    }

    private Background newWindowBackground() {
        return new Background((new BackgroundFill(WINDOW_BACKGROUND_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private Pane newTilesGrid() {
        GridPane tilesGrid = new GridPane();
        tilesGrid.setLayoutX(BOARD_PADDING_PX);
        tilesGrid.setLayoutY(BOARD_PADDING_PX);

        for (int i = 0; i < TOTAL_TILES_X; i++) {
            for (int j = 0; j < TOTAL_TILES_Y; j++) {
                Rectangle tile = newTile(i, j);
                tilesGrid.add(tile, i, j);
            }
        }
        return tilesGrid;
    }

    private Rectangle newTile(int x, int y) {
        Rectangle tile = new Rectangle(x, y, TILE_SIZE_PX, TILE_SIZE_PX);
        tile.setFill(TILE_COLOR);

        tilesByCoordinates.put(new Point2D(x, y), tile);

        return tile;
    }

    private Optional<Rectangle> getTile(Point2D coordinates) {
        return Optional.ofNullable(tilesByCoordinates.get(coordinates));
    }

    private void refreshBoard() {
        updateScore();
        updateTilesGrid();
    }

    private void updateTilesGrid() {
        for (Rectangle tile : tilesByCoordinates.values()) {
            tile.setFill(TILE_COLOR);
        }

        Collection<Point2D> snake = gameState.getSnake();
        for (Point2D p : snake) {
            getTile(p).ifPresent(tile -> tile.setFill(SNAKE_COLOR));
        }

        Point2D food = gameState.getFood();
        getTile(food).ifPresent(tile -> tile.setFill(FOOD_COLOR));
    }

    private void updateScore() {
        scoreValue.setText(String.valueOf(gameState.getScore()));
        scoreValue.setFont(TEXT_FONT);
    }

    private void moveSnakePeriodically() {
        ScheduledExecutorService scheduler = newScheduler();
        scheduler.scheduleWithFixedDelay(() -> makeNextMoveIfNotAlreadyGameOver(scheduler),
                0, SNAKE_MOVE_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    private ScheduledExecutorService newScheduler() {
        return Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

    private void makeNextMoveIfNotAlreadyGameOver(ScheduledExecutorService scheduler) {
        if (gameState == null || gameState.getGameStatus() == IN_PROGRESS) {
            Platform.runLater(() -> eventListener.onNextMove());
        } else if (gameState.getGameStatus() == GAME_OVER) {
            Platform.runLater(this::showGameOverDialog);
            scheduler.shutdown();
        }
    }
}