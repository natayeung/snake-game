package com.natay.games.snake.core.service;

import java.time.Duration;
import java.util.Optional;

/**
 * @author natayeung
 */
public interface GameMoveScheduler {

    void start(Runnable gameMove, SchedulingContext schedulingContext);

    void stop();

    void shutDown();

    void updateMoveIntervalIfNextMilestoneReached(int score);

    Optional<Duration> getMoveInterval();
}