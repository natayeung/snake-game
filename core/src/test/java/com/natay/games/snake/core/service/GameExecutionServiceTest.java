package com.natay.games.snake.core.service;

import com.natay.games.snake.core.common.Direction;
import com.natay.games.snake.core.model.Food;
import javafx.geometry.Point2D;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.util.List;
import java.util.concurrent.Executors;

import static com.natay.games.snake.core.common.Direction.*;
import static com.natay.games.snake.core.service.TestHelper.newExecutionContext;
import static com.natay.games.snake.core.service.TestHelper.someExecutionContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * @author natayeung
 */
public class GameExecutionServiceTest {

    @Mock
    private FoodProducer mockFoodProducer;
    @Mock
    private GameMoveScheduler mockGameMoveScheduler;
    @Mock
    private GameStateChangeListener mockStateChangeListener;
    @Mock
    private Food mockFood;
    @Captor
    private ArgumentCaptor<GameState> gameStateCaptor;

    private ExecutionContext executionContext;
    private GameExecutionService service;

    @Before
    public void setUp() {
        openMocks(this);

        doReturn(mockFood).when(mockFoodProducer).nextFoodExcludingPositions(any());
        doReturn(new Point2D(0, 0)).when(mockFood).getPosition();

        executionContext = someExecutionContext();

        service = new GameExecutionService(mockFoodProducer, mockGameMoveScheduler, executionContext);
    }

    @Test
    public void shouldStartSchedulingGameMovesWhenGameIsStarted() {
        service.startGame();

        verify(mockGameMoveScheduler).start(any(Runnable.class), eq(executionContext.schedulingContext()));
    }

    @Test
    public void shouldReturnAnInitialGameStateWhenGameIsStarted() {
        GameState gameState = service.startGame();

        assertGameStateInitialized(gameState);
    }

    @Test
    public void shouldRescheduleGameMovesWhenGameIsRestarted() {
        service.restartGame();

        InOrder inOrder = inOrder(mockGameMoveScheduler);
        inOrder.verify(mockGameMoveScheduler).stop();
        inOrder.verify(mockGameMoveScheduler).start(any(Runnable.class), eq(executionContext.schedulingContext()));
    }

    @Test
    public void shouldReturnNewInitialGameStateWhenGameIsRestarted() {
        GameState gameState = service.restartGame();

        assertGameStateInitialized(gameState);
    }

    @Test
    public void shouldChangeMovingDirectionOnlyIfNotOppositeWithDirectionSnakeIsMovingIn() {
        Direction originalDirection = service.getSnakeMovingDirection();
        Direction newDirection = (originalDirection == RIGHT || originalDirection == LEFT) ? UP : RIGHT;

        service.changeSnakeMovingDirection(newDirection);

        Direction returned = service.getSnakeMovingDirection();
        assertThat(returned, is(newDirection));
    }

    @Test
    public void shouldNotChangeMovingDirectionIfOppositeWithDirectionSnakeIsMovingIn() {
        Direction originalDirection = service.getSnakeMovingDirection();
        Direction newDirection = oppositeDirectionWith(originalDirection);

        service.changeSnakeMovingDirection(newDirection);

        Direction returned = service.getSnakeMovingDirection();

        assertThat(returned, is(originalDirection));
    }

    private void assertGameStateInitialized(GameState gameState) {
        Assertions.assertAll("Initial game state",
                () -> assertThat(gameState.getSnake(), hasSize(1)),
                () -> assertThat(gameState.getFood(), Matchers.isA(Point2D.class)),
                () -> assertThat(gameState.getScore(), is(0)),
                () -> assertThat(gameState.getSpeedIndication(), is(0.0)),
                () -> assertFalse(gameState.isGameOver()),
                () -> assertFalse(gameState.isFoodCaughtOnLastMove())
        );
    }

    @Test
    public void shouldNotifyListenerOfGameStateChanges() {
        service = new GameExecutionService(mockFoodProducer,
                new GameMoveSchedulingService(Executors.newSingleThreadScheduledExecutor()), newExecutionContext(3, 3));
        service.setStateChangeListener(mockStateChangeListener);

        service.startGame();

        verify(mockStateChangeListener, after(1000).atLeast(2)).onGameStateChange(gameStateCaptor.capture());
        List<GameState> capturedGameStates = gameStateCaptor.getAllValues();
        GameState firstState = capturedGameStates.get(0);
        GameState secondState = capturedGameStates.get(1);
        assertThat(secondState, not(firstState));
    }

    @Test
    public void shouldNotifyListenerOfGameOverStatusWhenSnakeHasRunIntoEdgeOfBoard() {
        service = new GameExecutionService(mockFoodProducer,
                new GameMoveSchedulingService(Executors.newSingleThreadScheduledExecutor()), newExecutionContext(2, 2));
        service.setStateChangeListener(mockStateChangeListener);

        service.startGame();

        verify(mockStateChangeListener, after(1000).atMost(2)).onGameStateChange(gameStateCaptor.capture());
        List<GameState> capturedGameStates = gameStateCaptor.getAllValues();
        GameState lastState = capturedGameStates.get(capturedGameStates.size() - 1);
        assertThat(lastState.isGameOver(), is(true));
    }

    @Test
    public void shouldNotifyListenerOfNewScoreWhenSnakeHasRunIntoFood() {
        doReturn(Food.at(1, 0)).when(mockFoodProducer).nextFoodExcludingPositions(any());
        service = new GameExecutionService(mockFoodProducer,
                new GameMoveSchedulingService(Executors.newSingleThreadScheduledExecutor()), newExecutionContext(2, 2, UP));
        service.setStateChangeListener(mockStateChangeListener);

        service.startGame();

        verify(mockStateChangeListener, after(1000).atLeastOnce()).onGameStateChange(gameStateCaptor.capture());
        List<GameState> capturedGameStates = gameStateCaptor.getAllValues();
        GameState firstState = capturedGameStates.get(0);
        assertThat(firstState.getScore(), is(1));
    }

    private Direction oppositeDirectionWith(Direction theOther) {
        return switch (theOther) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
