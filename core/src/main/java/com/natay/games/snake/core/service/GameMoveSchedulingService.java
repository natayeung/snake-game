package com.natay.games.snake.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.natay.games.snake.core.common.Validation.requireArgument;
import static java.util.Objects.*;

/**
 * @author natayeung
 */
public class GameMoveSchedulingService implements GameMoveScheduler {

    private static final Logger logger = LoggerFactory.getLogger(GameMoveSchedulingService.class);

    private final ScheduledExecutorService scheduler;
    private SchedulingContext schedulingContext;
    private Runnable gameMove;
    private ScheduledFuture<?> future;
    private Duration moveInterval;
    private int lastScoreMilestone;

    public GameMoveSchedulingService(ScheduledExecutorService scheduler) {
        this.scheduler = requireNonNull(scheduler, "Scheduler must be specified");
    }

    @Override
    public void start(Runnable gameMove, SchedulingContext schedulingContext) {
        this.gameMove = requireNonNull(gameMove, "Game move must be specified");
        this.schedulingContext = requireNonNull(schedulingContext, "Scheduling context must be specified");
        this.moveInterval = schedulingContext.initialMoveInterval();

        start(gameMove, moveInterval);
    }

    @Override
    public void stop() {
        if (nonNull(future)) {
            future.cancel(false);
        }
    }

    @Override
    public void shutDown() {
        if (nonNull(scheduler)) {
            scheduler.shutdown();
        }
    }

    @Override
    public void updateMoveIntervalIfNextMilestoneReached(int score) {
        requireArgument(score >= 0, "Score cannot be negative");

        if (isNull(gameMove) || isNull(moveInterval))
            return;

        if (isNextScoreMilestoneReached(score) && isMoveIntervalGreaterThanMinimum()) {
            moveInterval = moveInterval.minus(schedulingContext.moveIntervalDecrement());
            lastScoreMilestone = score;
            logger.info("Updated move interval={}ms, last score milestone={}", moveInterval.toMillis(), lastScoreMilestone);

            restart(gameMove, moveInterval);
        }
    }

    @Override
    public Optional<Duration> getMoveInterval() {
        return Optional.ofNullable(moveInterval);
    }

    private void start(Runnable gameMove, Duration moveInterval) {
        future = scheduler.scheduleWithFixedDelay(gameMove,
                0, moveInterval.toMillis(), TimeUnit.MILLISECONDS);

        logger.info("Started scheduling game moves, move interval={}ms", moveInterval.toMillis());
    }

    private boolean isNextScoreMilestoneReached(int score) {
        boolean isScoreChangedSinceLastMilestone = score != lastScoreMilestone;
        boolean isMilestoneReached = score % schedulingContext.scoreMilestoneForIntervalChange() == 0;
        return isScoreChangedSinceLastMilestone && isMilestoneReached;
    }

    private boolean isMoveIntervalGreaterThanMinimum() {
        return moveInterval.compareTo(schedulingContext.minMoveInterval()) > 0;
    }

    private void restart(Runnable gameMove, Duration moveInterval) {
        stop();
        start(gameMove, moveInterval);
    }
}