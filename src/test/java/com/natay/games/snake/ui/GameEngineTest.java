package com.natay.games.snake.ui;

import com.natay.games.snake.GameParameters;
import com.natay.games.snake.dto.GameState;
import com.natay.games.snake.model.Direction;
import com.natay.games.snake.model.Food;
import com.natay.games.snake.model.Snake;
import com.natay.games.snake.service.FoodProducer;
import com.natay.games.snake.service.GameMoveSchedulable;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.hamcrest.core.IsNot;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.natay.games.snake.model.Direction.RIGHT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * @author natayeung
 */
@RunWith(JUnitParamsRunner.class)
public class GameEngineTest {

    private final Direction initialMovingDirection = RIGHT;
    private final Point2D foodPosition = new Point2D(0, 9);

    @Mock
    private GameView gameViewMock;
    @Mock
    private FoodProducer foodProducerMock;
    @Mock
    private GameMoveSchedulable gameMoveSchedulerMock;
    @Mock
    private Food foodMock;
    @Spy
    private Snake snakeSpy = new Snake(Point2D.ZERO, initialMovingDirection);
    @Captor
    private ArgumentCaptor<GameState> gameStateCaptor;

    private GameEngine gameEngine;

    @Before
    public void setUp() {
        openMocks(this);

        when(foodProducerMock.nextFoodExcludingPositions(any())).thenReturn(foodMock);
        when(foodMock.getPosition()).thenReturn(foodPosition);

        gameEngine = new GameEngine(gameViewMock, foodProducerMock, gameMoveSchedulerMock, inputKeyDirectionMapping());
        gameEngine.setSnake(snakeSpy);
    }

    @Test
    public void shouldStartGameMoveSchedulerOnInstantiation() {
        verify(gameMoveSchedulerMock).start(gameEngine, GameParameters.INITIAL_MOVE_INTERVAL);
    }

    @Test
    public void shouldNotifyViewToInitializeGameBoardOnInstantiation() {
        verify(gameViewMock).initializeGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertGameStateInitialisedCorrectly(capturedGameState);
    }

    @Test
    @Parameters({
            "UP   |UP",
            "DOWN |DOWN",
            "RIGHT|RIGHT"
    })
    public void shouldChangeMovingDirectionOnlyIfNotOppositeWithDirectionSnakeIsMovingIn(KeyCode inputKey, Direction direction) {
        gameEngine.onMovingDirectionUpdate(inputKey);

        verify(snakeSpy).changeMovingDirection(direction);
    }

    @Test
    public void shouldNotChangeMovingDirectionIfOppositeWithDirectionSnakeIsMovingIn() {
        gameEngine.onMovingDirectionUpdate(KeyCode.LEFT);

        verify(snakeSpy, never()).changeMovingDirection(any(Direction.class));
    }

    @Test
    public void shouldNotifyViewOfGameStatusChangeWhenSnakeIsCollidingWithBody() {
        doReturn(true).when(snakeSpy).isCollidingWithBody();

        gameEngine.run();

        verifyGameViewNotifiedOfGameOverStatus();
    }

    @Test
    public void shouldNotifyViewOfGameStatusChangeWhenSnakeIsCollidingWithEdgeOfBoard() {
        doReturn(true).when(snakeSpy).isCollidingWithEdgeOfBoard(anyInt(), anyInt());

        gameEngine.run();

        verifyGameViewNotifiedOfGameOverStatus();
    }

    @Test
    public void shouldNotifyViewOfNewGameStateWhenSnakeIsCollidingWithFood() {
        doReturn(Optional.of(GameParameters.INITIAL_MOVE_INTERVAL)).when(gameMoveSchedulerMock).getMoveInterval();
        doReturn(true).when(snakeSpy).isCollidingWith(foodMock);

        gameEngine.run();

        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertAll("New game state",
                () -> assertFalse("Game is not over", capturedGameState.isGameOver()),
                () -> assertTrue("Food is caught", capturedGameState.isFoodCaughtOnLastMove()),
                () -> assertThat("Score is increased by 1", capturedGameState.getScore(), is(1)),
                () -> assertThat("Speed indication remains unchanged", capturedGameState.getSpeedIndication(), is(0.0)),
                () -> assertThat("Snake has grown", capturedGameState.getSnake().size(), greaterThan(1)),
                () -> assertThat("Food is in a different position", capturedGameState.getFood(), IsNot.not(foodMock)));
    }

    @Test
    public void shouldNotifyViewOfNewSpeedIndicationWhenMoveIntervalIsDecreased() {
        Duration newMoveInterval = GameParameters.INITIAL_MOVE_INTERVAL.minus(Duration.ofMillis(100));
        doReturn(Optional.of(newMoveInterval)).when(gameMoveSchedulerMock).getMoveInterval();
        doReturn(true).when(snakeSpy).isCollidingWith(foodMock);

        gameEngine.run();

        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertThat(capturedGameState.getSpeedIndication(), greaterThan(0.0));
    }

    @Test
    public void shouldNotifyViewToResetGameBoardOnGameRestart() {
        setUpGameStateWhereSnakeHasGrown();
        setUpGameStateWhereGameIsOver();

        gameEngine.onGameRestart();

        verify(gameViewMock, times(2)).initializeGameBoard(gameStateCaptor.capture());
        List<GameState> capturedGameStates = gameStateCaptor.getAllValues();
        GameState lastGameState = capturedGameStates.get(1);
        assertGameStateInitialisedCorrectly(lastGameState);
    }

    private Map<KeyCode, Direction> inputKeyDirectionMapping() {
        return Map.of(KeyCode.UP, Direction.UP, KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT, KeyCode.RIGHT, RIGHT);
    }

    private void assertGameStateInitialisedCorrectly(GameState capturedGameState) {
        Assertions.assertAll("Initialized game state",
                () -> assertThat(capturedGameState.getSnake().size(), is(1)),
                () -> assertThat(capturedGameState.getFood(), is(foodPosition)),
                () -> assertThat(capturedGameState.getScore(), is(0)),
                () -> assertThat(capturedGameState.getSpeedIndication(), is(0.0)),
                () -> assertFalse(capturedGameState.isGameOver()),
                () -> assertFalse(capturedGameState.isFoodCaughtOnLastMove()));
    }

    private void verifyGameViewNotifiedOfGameOverStatus() {
        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertTrue(capturedGameState.isGameOver());
    }

    private void setUpGameStateWhereSnakeHasGrown() {
        doReturn(true).when(snakeSpy).isCollidingWith(foodMock);
        gameEngine.run();
        assertThat(snakeSpy.getLength(), greaterThan(1));
    }

    private void setUpGameStateWhereGameIsOver() {
        doReturn(true).when(snakeSpy).isCollidingWithBody();
        gameEngine.run();
    }
}