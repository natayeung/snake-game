package com.nata.games.snake;

import com.nata.games.snake.model.Direction;
import com.nata.games.snake.model.Food;
import com.nata.games.snake.model.Snake;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;
import java.util.Map;

import static com.nata.games.snake.model.Direction.RIGHT;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * @author natayeung
 */
@RunWith(JUnitParamsRunner.class)
public class GameEngineTest {

    private final Direction initialMovingDirection = RIGHT;

    @Mock
    private SnakeGameUserInterface.View gameViewMock;
    @Mock
    private RandomFoodProducer randomFoodProducerMock;
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

        when(randomFoodProducerMock.nextFoodExcludingPositions(any())).thenReturn(foodMock);
        when(foodMock.getPosition()).thenReturn(new Point2D(0, 9));

        gameEngine = new GameEngine(gameViewMock, randomFoodProducerMock, inputKeyDirectionMapping());
        gameEngine.setSnake(snakeSpy);
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
    public void shouldNotifyViewOfGameStatusChangeWhenSnakeIsCollidingWithBodyOnNextMove() {
        doReturn(true).when(snakeSpy).isCollidingWithBody();

        gameEngine.onNextMove();

        verifyGameViewNotifiedOfGameOverStatus();
    }

    @Test
    public void shouldNotifyViewOfGameStatusChangeWhenSnakeIsCollidingWithEdgeOfBoardOnNextMove() {
        doReturn(true).when(snakeSpy).isCollidingWithEdgeOfBoard(anyInt(), anyInt());

        gameEngine.onNextMove();

        verifyGameViewNotifiedOfGameOverStatus();
    }

    @Test
    public void shouldNotifyViewOfNewGameStateWhenSnakeIsCollidingWithFoodOnNextMove() {
        doReturn(true).when(snakeSpy).isCollidingWith(foodMock);

        gameEngine.onNextMove();

        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertAll(
                () -> assertFalse("Game is not over", capturedGameState.isGameOver()),
                () -> assertThat("Score is increased by 1", capturedGameState.getScore(), is(1)),
                () -> assertThat("Snake has grown", capturedGameState.getSnake().size(), is(2)),
                () -> assertThat("Food is in a different position", capturedGameState.getFood(), not(foodMock)));
    }

    @Test
    public void shouldNotifyViewToResetGameBoardOnGameRestart() {
        setUpGameStateWhereSnakeHasGrown();
        setUpGameStateWhereGameIsOver();

        gameEngine.onGameRestart();

        verify(gameViewMock, times(2)).initializeGameBoard(gameStateCaptor.capture());
        List<GameState> capturedGameStates = gameStateCaptor.getAllValues();
        GameState lastGameState = capturedGameStates.get(1);
        assertAll(
                () -> assertFalse("Game is reset", lastGameState.isGameOver()),
                () -> assertThat("Score is reset to 0", lastGameState.getScore(), is(0)),
                () -> assertThat("Snake is re-created", lastGameState.getSnake().size(), is(1)));
    }

    private Map<KeyCode, Direction> inputKeyDirectionMapping() {
        return Map.of(KeyCode.UP, Direction.UP, KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT, KeyCode.RIGHT, RIGHT);
    }

    private void verifyGameViewNotifiedOfGameOverStatus() {
        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertTrue(capturedGameState.isGameOver());
    }

    private void setUpGameStateWhereSnakeHasGrown() {
        doReturn(true).when(snakeSpy).isCollidingWith(foodMock);
        gameEngine.onNextMove();
        assertThat(snakeSpy.getLength(), is(2));
    }

    private void setUpGameStateWhereGameIsOver() {
        doReturn(true).when(snakeSpy).isCollidingWithBody();
        gameEngine.onNextMove();
    }
}
