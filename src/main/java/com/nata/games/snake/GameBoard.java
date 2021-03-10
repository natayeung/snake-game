package com.nata.games.snake;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

import static com.nata.games.snake.GameParameters.*;
import static com.nata.games.snake.GameParameters.DisplayText.SCORE;
import static com.nata.games.snake.GameParameters.Resources.SPEED_IMAGE;
import static com.nata.games.snake.GameParameters.Resources.STYLESHEET;
import static java.util.Objects.isNull;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.stage.StageStyle.UNDECORATED;

/**
 * @author natayeung
 *
 * Credits:
 * - Sound from zapsplat
 * - Icons made by <a href="https://smashicons.com/" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>
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
        final Pane basePane = new VBox();
        stage.setScene(newSceneWith(basePane));
        stage.initStyle(UNDECORATED);
        basePane.getChildren().addAll(
                newScorePane(),
                newTilesGrid(),
                newSpeedIndicatorPane());

        stage.show();
    }

    private Scene newSceneWith(Parent parent) {
        final Scene scene = new Scene(parent, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(this);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
        return scene;
    }

    private Pane newSpeedIndicatorPane() {
        final Pane speedPane = newHorizontalPaneWithStyle("speed-indicator-pane");
        final Label speedLabel = new Label();
        final Optional<Image> speedImage = loadImage(SPEED_IMAGE, 24, 24);
        if (speedImage.isPresent()) {
            speedLabel.setGraphic(new ImageView(speedImage.get()));
        } else {
            speedLabel.setText("Speed:");
        }
        speedIndicator = new ProgressBar(0);
        speedPane.getChildren().addAll(speedLabel, speedIndicator);

        return speedPane;
    }

    private Pane newScorePane() {
        final Pane scorePane = newHorizontalPaneWithStyle("score-pane");
        final Text scoreDisplayName = new Text(SCORE);
        scoreValue = new Text("");

        scorePane.getChildren().addAll(scoreDisplayName, scoreValue);

        return scorePane;
    }

    private Pane newHorizontalPaneWithStyle(String styleClass) {
        final HBox pane = new HBox();
        pane.getStyleClass().add(styleClass);
        return pane;
    }

    private Optional<Image> loadImage(String resourceName, int width, int height) {
        final Optional<URL> resource = Optional.ofNullable(getClass().getResource(resourceName));
        if (resource.isPresent()) {
            final Image image = new Image(resource.get().toString(), width, height, true, true, true);
            return Optional.of(image);
        } else {
            logger.warn("Cannot load image, file {} not found", resourceName);
            return Optional.empty();
        }
    }

    private Pane newTilesGrid() {
        final GridPane tilesGrid = new GridPane();
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
        tile.setFill(EMPTY_COLOR);
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