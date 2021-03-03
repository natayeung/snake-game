package com.nata.games.snake;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Map;

import static com.nata.games.snake.GameStatus.GAME_OVER;
import static com.nata.games.snake.GameStatus.IN_PROGRESS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * @author natayeung
 */
@RunWith(JUnitParamsRunner.class)
public class GameEngineTest {

    @Mock
    private SnakeGameUserInterface.View gameViewMock;
    @Mock
    private RandomFoodProducer randomFoodProducerMock;
    @Mock
    private Food foodMock;
    @Spy
    private Snake snakeSpy = new Snake(Point2D.ZERO, Direction.RIGHT);
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
            "LEFT |LEFT",
            "RIGHT|RIGHT"
    })
    public void shouldChangeMovingDirectionOfSnake(KeyCode inputKey, Direction direction) {
        gameEngine.onMovingDirectionUpdate(inputKey);

        verify(snakeSpy).changeMovingDirection(direction);
    }

    @Test
    public void shouldNotifyViewOfGameStatusChangeWhenSnakeIsCollidingWithBody() {
        doReturn(true).when(snakeSpy).isCollidingWithBody();

        gameEngine.onNextMove();

        verifyGameViewNotifiedOfGameStatus(GAME_OVER);
    }

    @Test
    public void shouldNotifyViewOfGameStatusChangeWhenSnakeIsCollidingWithEdgeOfBoard() {
        doReturn(true).when(snakeSpy).isCollidingWithEdgeOfBoard(anyInt(), anyInt());

        gameEngine.onNextMove();

        verifyGameViewNotifiedOfGameStatus(GAME_OVER);
    }

    @Test
    public void shouldNotifyViewOfNewGameStateWhenSnakeIsCollidingWithFood() {
        doReturn(true).when(snakeSpy).isCollidingWith(foodMock);

        gameEngine.onNextMove();

        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        Assertions.assertAll(
                () -> assertThat("Game status remains as IN_PROGRESS", capturedGameState.getGameStatus(), is(IN_PROGRESS)),
                () -> assertThat("Score is increased by 1", capturedGameState.getScore(), is(1)),
                () -> assertThat("Snake has grown", capturedGameState.getSnake().size(), is(2)),
                () -> assertThat("Food is in a different position", capturedGameState.getFood(), not(foodMock)));
    }

    private Map<KeyCode, Direction> inputKeyDirectionMapping() {
        return Map.of(KeyCode.UP, Direction.UP, KeyCode.DOWN, Direction.DOWN,
                KeyCode.LEFT, Direction.LEFT, KeyCode.RIGHT, Direction.RIGHT);
    }

    private void verifyGameViewNotifiedOfGameStatus(GameStatus status) {
        verify(gameViewMock).updateGameBoard(gameStateCaptor.capture());
        GameState capturedGameState = gameStateCaptor.getValue();
        assertThat(capturedGameState.getGameStatus(), is(status));
    }
}
