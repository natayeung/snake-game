package com.natay.games.snake.core.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.github.npathai.hamcrestopt.OptionalMatchers.hasValue;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.natay.games.snake.core.service.TestHelper.someSchedulingContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * @author natayeung
 */
public class GameMoveSchedulingServiceTest {

    @Mock
    private ScheduledExecutorService mockScheduler;
    @Mock
    private Runnable mockGameMove;

    private SchedulingContext schedulingContext;
    private GameMoveSchedulingService service;

    @Before
    public void setUp() {
        openMocks(this);

        schedulingContext = someSchedulingContext();
        service = new GameMoveSchedulingService(mockScheduler);
    }

    @Test
    public void canStartSchedulingGameMove() {
        long moveInterval = schedulingContext.initialMoveInterval().toMillis();

        service.start(mockGameMove, schedulingContext);

        verify(mockScheduler).scheduleWithFixedDelay(eq(mockGameMove), anyLong(), eq(moveInterval), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void shouldUpdateMoveIntervalWhenMilestoneIsReached() {
        service.start(mockGameMove, schedulingContext);
        Duration originalMoveInterval = service.getMoveInterval().orElseThrow();
        Duration updatedMoveInterval = originalMoveInterval.minus(schedulingContext.moveIntervalDecrement());
        int score = 3;

        service.updateMoveIntervalIfNextMilestoneReached(score);

        assertThat(service.getMoveInterval(), hasValue(updatedMoveInterval));
        verify(mockScheduler).scheduleWithFixedDelay(eq(mockGameMove), anyLong(), eq(updatedMoveInterval.toMillis()), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void shouldNotUpdateMoveIntervalWhenMilestoneIsNotReached() {
        service.start(mockGameMove, schedulingContext);
        Duration originalMoveInterval = service.getMoveInterval().orElseThrow();
        Duration updatedMoveInterval = originalMoveInterval.minus(schedulingContext.moveIntervalDecrement());
        int score = 2;

        service.updateMoveIntervalIfNextMilestoneReached(score);

        assertThat(service.getMoveInterval(), hasValue(originalMoveInterval));
        verify(mockScheduler, never()).scheduleWithFixedDelay(eq(mockGameMove), anyLong(), eq(updatedMoveInterval.toMillis()), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void canReturnMoveInterval() {
        service.start(mockGameMove, schedulingContext);

        Optional<Duration> moveInterval = service.getMoveInterval();

        assertThat(moveInterval, isPresent());
    }
}
