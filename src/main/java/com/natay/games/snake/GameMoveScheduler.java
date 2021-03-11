package com.natay.games.snake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class GameMoveScheduler implements GameMoveSchedulable {

    private static final Logger logger = LoggerFactory.getLogger(GameMoveScheduler.class);

    private final ScheduledExecutorService scheduler;
    private SnakeMoveExecutable snakeMoveExecutable;
    private ScheduledFuture<?> future;
    private Duration moveInterval;
    private int lastScoreMilestone;

    public GameMoveScheduler(ScheduledExecutorService scheduler) {
        this.scheduler = checkNotNull(scheduler, "Scheduler must be specified");
    }

    @Override
    public void start(SnakeMoveExecutable snakeMoveExecutable, Duration moveInterval) {
        this.snakeMoveExecutable = checkNotNull(snakeMoveExecutable, "Snake move executable must be specified");
        this.moveInterval = checkNotNull(moveInterval, "Move interval must be specified");

        future = scheduler.scheduleWithFixedDelay(snakeMoveExecutable,
                0, moveInterval.toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        if (!isNull(future)) {
            future.cancel(false);
        }
    }

    @Override
    public void shutDown() {
        if (!isNull(scheduler)) {
            scheduler.shutdown();
        }
    }

    @Override
    public void updateMoveIntervalIfNextMilestoneReached(int score) {
        checkArgument(score >= 0, "Score cannot be negative");

        if (isNull(moveInterval))
            return;

        if (isNextScoreMilestoneReached(score) && isMoveIntervalGreaterThanMinimum()) {
            moveInterval = moveInterval.minus(GameParameters.MOVE_INTERVAL_DECREMENT);
            lastScoreMilestone = score;
            logger.info("Updated move interval={}ms, last score milestone={}", moveInterval.toMillis(), lastScoreMilestone);

            restart();
        }
    }

    @Override
    public Optional<Duration> getMoveInterval() {
        return Optional.ofNullable(moveInterval);
    }

    private boolean isNextScoreMilestoneReached(int score) {
        boolean isScoreChangedSinceLastMilestone = score != lastScoreMilestone;
        boolean isMilestoneReached = score % GameParameters.SCORE_MILESTONE_FOR_SPEED_CHANGE == 0;
        return isScoreChangedSinceLastMilestone && isMilestoneReached;
    }

    private boolean isMoveIntervalGreaterThanMinimum() {
        return moveInterval.compareTo(GameParameters.MIN_MOVE_INTERVAL) > 0;
    }

    private void restart() {
        if (isNull(snakeMoveExecutable)) {
            logger.warn("Attempt to restart scheduler failed as snake move executable has not been specified");
        }

        stop();
        start(snakeMoveExecutable, moveInterval);
    }
}