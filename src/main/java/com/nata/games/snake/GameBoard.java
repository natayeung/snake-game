package com.nata.games.snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.nata.games.snake.GameParameters.*;
import static com.nata.games.snake.GameParameters.DisplayText.*;
import static com.nata.games.snake.GameParameters.Resources.BITE_SOUND_CLIP;
import static com.nata.games.snake.GameParameters.Resources.GAME_OVER_SOUND_CLIP;
import static javafx.animation.Animation.INDEFINITE;
import static javafx.geometry.Pos.TOP_RIGHT;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

/**
 * @author natayeung
 * Credit: sound from zapsplat
 */
public class GameBoard implements SnakeGameUserInterface.View, EventHandler<KeyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(GameBoard.class);

    private final Stage stage;
    private final Map<Point2D, Rectangle> tilesByCoordinates;
    private final SnakeMovementManager snakeMovementManager = new SnakeMovementManager();
    private SnakeGameUserInterface.Presenter presenter;
    private GameState gameState;
    private Text scoreValue;
    private int lastScore = -1;

    public GameBoard(Stage stage) {
        this.stage = stage;
        this.tilesByCoordinates = new HashMap<>(TOTAL_TILES_X * TOTAL_TILES_Y);

        initializeUI();
    }

    @Override
    public void setPresenter(SnakeGameUserInterface.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initializeGameBoard(GameState gameState) {
        logger.info("Initializing game board, {}", gameState);

        this.gameState = gameState;
        refreshBoard();
        snakeMovementManager.scheduleSnakeMovement();
    }

    @Override
    public void updateGameBoard(GameState gameState) {
        logger.debug("Updating game board, {}", gameState);

        this.gameState = gameState;
        playSoundIfFoodCaughtOnLastMove();
        refreshBoard();
        snakeMovementManager.speedUpSnakeMovementWhenMilestoneMet();
    }

    @Override
    public void showGameOverDialog() {
        playSoundIfGameOver();

        ButtonType newGame = new ButtonType(NEW_GAME, ButtonBar.ButtonData.OK_DONE);
        ButtonType exit = new ButtonType(EXIT, ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert dialog = new Alert(Alert.AlertType.NONE, GAME_OVER_MESSAGE, newGame, exit);

        Optional<ButtonType> choice = dialog.showAndWait();
        if (choice.orElse(exit) == newGame) {
            presenter.onGameRestart();
        } else {
            Platform.exit();
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KEY_PRESSED && event.getCode().isArrowKey()) {
            presenter.onMovingDirectionUpdate(event.getCode());
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

    private Pane newScorePane() {
        final HBox scorePane = new HBox();
        scorePane.setPrefHeight(SCORE_DISPLAY_HEIGHT);
        scorePane.setAlignment(TOP_RIGHT);
        scorePane.setLayoutX(BOARD_PADDING_PX);
        scorePane.setLayoutY(BOARD_PADDING_PX);

        final Text scoreDisplayName = new Text(SCORE);
        scoreDisplayName.setFont(TEXT_FONT);

        scoreValue = new Text();

        scorePane.getChildren().addAll(scoreDisplayName, scoreValue);

        return scorePane;
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

    private Optional<Rectangle> getTile(Point2D coordinates) {
        return Optional.ofNullable(tilesByCoordinates.get(coordinates));
    }

    private void refreshBoard() {
        logger.debug("Refreshing game board ...");

        updateScore();
        updateTilesGrid();

        logger.debug("Refreshed game board");
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
        final int score = gameState.getScore();
        if (score != lastScore) {
            scoreValue.setText(String.valueOf(score));
            scoreValue.setFont(TEXT_FONT);
            lastScore = score;
        }
    }

    private void playSoundIfFoodCaughtOnLastMove() {
        if (gameState.isFoodCaughtOnLastMove()) {
            playSound(BITE_SOUND_CLIP);
        }
    }

    private void playSoundIfGameOver() {
        if (gameState.isGameOver()) {
            playSound(GAME_OVER_SOUND_CLIP);
        }
    }

    private void playSound(String resourceName) {
        Optional<URL> resource = Optional.ofNullable(getClass().getResource(resourceName));
        if (resource.isPresent()) {
            final AudioClip sound = new AudioClip(resource.get().toString());
            sound.play();
        } else {
            logger.warn("Sound clip {} not found", resourceName);
        }
    }

    private void makeNextMoveIfGameStillInProgress() {
        if (gameState == null || !gameState.isGameOver()) {
            presenter.onNextMove();
        } else {
            snakeMovementManager.stopSnakeMovement();
            // It needs passing to Platform#runLater or it will throw "java.lang.IllegalStateException: showAndWait is not allowed during animation or layout processing"
            Platform.runLater(this::showGameOverDialog);
        }
    }

    class SnakeMovementManager {
        private Duration snakeMovementInterval = DEFAULT_SNAKE_MOVE_INTERVAL;
        private Timeline snakeMovement;
        private int lastScoreMilestone = 0;

        private SnakeMovementManager() {
        }

        void scheduleSnakeMovement() {
            snakeMovement = new Timeline(newKeyFrame(snakeMovementInterval));
            snakeMovement.setCycleCount(INDEFINITE);
            snakeMovement.play();

            logger.info("Scheduled snake movement, interval={}", snakeMovementInterval);
        }

        void speedUpSnakeMovementWhenMilestoneMet() {
            if (snakeMovement == null || gameState.getScore() == 0)
                return;

            if (isNextScoreMilestoneReached() && isMoveIntervalGreaterThanMinimum()) {
                decrementMoveInterval();
                rescheduleSnakeMovement();
                lastScoreMilestone = gameState.getScore();
            }
        }

        void stopSnakeMovement() {
            if (snakeMovement != null) {
                snakeMovement.stop();
                logger.debug("Stopped snake movement");
            }
        }

        private KeyFrame newKeyFrame(Duration interval) {
            return new KeyFrame(interval, e -> makeNextMoveIfGameStillInProgress());
        }

        private boolean isNextScoreMilestoneReached() {
            boolean isScoreChangedSinceLastMilestone = gameState.getScore() != lastScoreMilestone;
            boolean isMilestoneReached = gameState.getScore() % SCORE_MILESTONE_FOR_SPEED_CHANGE == 0;
            return isScoreChangedSinceLastMilestone && isMilestoneReached;
        }

        private boolean isMoveIntervalGreaterThanMinimum() {
            return snakeMovementInterval.greaterThan(MIN_SNAKE_MOVE_INTERVAL);
        }

        private void decrementMoveInterval() {
            snakeMovementInterval = snakeMovementInterval.subtract(SNAKE_MOVE_INTERVAL_DECREMENT);
            logger.debug("Decremented snake move interval {}", snakeMovementInterval);
        }

        private void rescheduleSnakeMovement() {
            stopSnakeMovement();
            scheduleSnakeMovement();
        }
    }
}