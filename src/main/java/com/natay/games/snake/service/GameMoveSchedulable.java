package com.natay.games.snake.service;

import java.time.Duration;
import java.util.Optional;

/**
 * @author natayeung
 */
public interface GameMoveSchedulable {

    void start(SnakeMoveExecutable snakeMoveExecutable, Duration moveInterval);

    void stop();

    void shutDown();

    void updateMoveIntervalIfNextMilestoneReached(int score);

    Optional<Duration> getMoveInterval();
}