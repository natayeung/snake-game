package com.natay.games.snake.gui;

import com.natay.games.snake.core.common.Direction;
import com.natay.games.snake.core.service.GameExecutor;
import com.natay.games.snake.core.service.GameState;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * @author natayeung
 */
public class GameEngineTest {

    private final GameState initialGameState = aGameState();

    @Mock
    private GameView mockGameView;
    @Mock
    private GameExecutor mockGameExecutor;
    @Captor
    private ArgumentCaptor<GameState> gameStateCaptor;

    private GameEngine gameEngine;

    @BeforeEach
    public void setUp() {
        openMocks(this);

        doReturn(initialGameState).when(mockGameExecutor).startGame();

        gameEngine = new GameEngine(mockGameView, mockGameExecutor, inputKeyDirectionMapping());
    }

    @Test
    public void shouldStartGameOnInstantiation() {
        verify(mockGameExecutor).startGame();
    }

    @Test
    public void shouldNotifyViewToInitializeGameBoardOnInstantiation() {
        verify(mockGameView).initializeGameBoard(gameStateCaptor.capture());
        GameState captured = gameStateCaptor.getValue();
        assertThat(captured, is(initialGameState));
    }

    @ParameterizedTest
    @CsvSource({
            "UP   ,UP",
            "DOWN ,DOWN",
            "LEFT ,LEFT",
            "RIGHT,RIGHT"
    })
    public void shouldRequestToChangeMovingDirectionOfSnakeWhenInputKeyIsRecognised(KeyCode inputKey, Direction direction) {
        gameEngine.onMovingDirectionUpdate(inputKey);

        verify(mockGameExecutor).changeSnakeMovingDirection(direction);
    }

    @Test
    public void shouldNotRequestToChangeMovingDirectionOfSnakeWhenInputKeyIsNotRecognised() {
        KeyCode unrecognisedKey = KeyCode.A;

        gameEngine.onMovingDirectionUpdate(unrecognisedKey);

        verify(mockGameExecutor, never()).changeSnakeMovingDirection(any());
    }

    @Test
    public void shouldNotifyViewToResetGameBoardOnGameRestart() {
        GameState gameState = aGameState();
        doReturn(gameState).when(mockGameExecutor).restartGame();

        gameEngine.onGameRestart();

        verify(mockGameExecutor).restartGame();

        verify(mockGameView, times(2)).initializeGameBoard(gameStateCaptor.capture());
        List<GameState> capturedGameStates = gameStateCaptor.getAllValues();
        GameState lastState = capturedGameStates.get(1);
        assertThat(lastState, is(gameState));
    }

    @Test
    public void shouldNotifyViewToUpdateGameBoardOnStateChange() {
        GameState gameState = aGameState();

        gameEngine.onGameStateChange(gameState);

        verify(mockGameView).updateGameBoard(gameState);
    }

    private Map<KeyCode, Direction> inputKeyDirectionMapping() {
        return Map.of(KeyCode.UP, Direction.UP, KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT, KeyCode.RIGHT, Direction.RIGHT);
    }

    private GameState aGameState() {
        Random random = new Random();
        return GameState.newBuilder()
                .withSnake(List.of(new Point2D(2,2)))
                .withFood(new Point2D(random.nextInt(5), random.nextInt(5)))
                .withScore(0)
                .withSpeedIndication(0.0)
                .isGameOver(false)
                .isFoodCaughtOnLastMove(false)
                .build();
    }
}