package com.nata.games.snake;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.nata.games.snake.GameParameters.Resources.STYLESHEET;
import static com.nata.games.snake.GameParameters.WINDOW_HEIGHT;
import static com.nata.games.snake.GameParameters.WINDOW_WIDTH;
import static java.util.Objects.isNull;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;
import static javafx.stage.StageStyle.UNDECORATED;

/**
 * @author natayeung
 * <p>
 * Credits:
 * - Sound effects from zapsplat
 * - Apple icon made by <a href="https://smashicons.com/" title="Smashicons">Smashicons</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>
 * - Close button icon made by <a href="http://catalinfertu.com" title="Catalin Fertu">Catalin Fertu</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>
 * - Snake and speed icons made by <a href="https://www.freepik.com" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a>
 */
public class GameBoard implements SnakeGameUserInterface.View, EventHandler<KeyEvent>, SnakeGameUserInterface.GameRestartable {

    private static final Logger logger = LoggerFactory.getLogger(GameBoard.class);

    private final Stage stage;
    private final List<StateChangeObserver> stateChangeObservers = new ArrayList<>();
    private final List<ComponentInitializer> componentInitializers = new ArrayList<>();
    private SnakeGameUserInterface.Presenter presenter;

    public GameBoard(Stage stage) {
        this.stage = stage;

        final HeaderRenderer headerRenderer = new HeaderRenderer();
        final TilesGridUpdater tilesGridUpdater = new TilesGridUpdater();
        final ScoreUpdater scoreUpdater = new ScoreUpdater();
        final SpeedIndicationUpdater speedIndicationUpdater = new SpeedIndicationUpdater();
        final SoundPlayer soundPlayer = new SoundPlayer();
        final GameOverNotifier gameOverNotifier = new GameOverNotifier(this);

        addComponentInitializers(headerRenderer, scoreUpdater, tilesGridUpdater, speedIndicationUpdater);
        addStateChangeObservers(tilesGridUpdater, scoreUpdater, speedIndicationUpdater, soundPlayer, gameOverNotifier);
        initializeUI();
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

        for (ComponentInitializer initializer : componentInitializers) {
            basePane.getChildren().add(initializer.initialize());
        }

        stage.show();
    }

    private Scene newSceneWith(Parent parent) {
        final Scene scene = new Scene(parent, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(this);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());
        return scene;
    }

    private void addComponentInitializers(ComponentInitializer... initializers) {
        this.componentInitializers.addAll(Arrays.asList(initializers));
    }

    private void addStateChangeObservers(StateChangeObserver... observers) {
        stateChangeObservers.addAll(Arrays.asList(observers));
    }

    private void refreshBoard(GameState gameState) {
        for (StateChangeObserver observer : stateChangeObservers) {
            observer.stateChanged(gameState);
        }
    }
}