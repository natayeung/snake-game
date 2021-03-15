package com.natay.games.snake.gui;


import com.natay.games.snake.core.common.Direction;
import com.natay.games.snake.core.service.GameExecutor;
import com.natay.games.snake.core.service.GameState;
import com.natay.games.snake.core.service.GameStateChangeListener;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.natay.games.snake.core.common.Validation.requireNonEmpty;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * @author natayeung
 */
class GameEngine implements GamePresenter, GameStateChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(GameEngine.class);

    private final GameView gameView;
    private final GameExecutor gameExecutor;
    private final Map<KeyCode, Direction> directionsByInputKey;

    GameEngine(GameView gameView,
               GameExecutor gameExecutor,
               Map<KeyCode, Direction> inputKeyDirectionMapping) {

        this.gameView = requireNonNull(gameView, "Game view must be specified");
        this.gameExecutor = requireNonNull(gameExecutor, "Game executor must be specified");
        this.directionsByInputKey = requireNonEmpty(inputKeyDirectionMapping, "Input key direction mapping cannot be empty");

        startNewGame();
    }

    @Override
    public void onMovingDirectionUpdate(KeyCode code) {
        logger.debug("Received input key {}", code);

        if (!directionsByInputKey.containsKey(code))
            return;

        final Direction direction = directionsByInputKey.get(code);
        gameExecutor.changeSnakeMovingDirection(direction);
    }

    @Override
    public void onGameRestart() {
        final GameState gameState = gameExecutor.restartGame();
        gameView.initializeGameBoard(gameState);
    }

    @Override
    public void onGameStateChange(GameState gameState) {
        if (nonNull(gameState)) {
            gameView.updateGameBoard(gameState);
        }
    }

    private void startNewGame() {
        final GameState gameState = gameExecutor.startGame();
        gameView.initializeGameBoard(gameState);
    }
}