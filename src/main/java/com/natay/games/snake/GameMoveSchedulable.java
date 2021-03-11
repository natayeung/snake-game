package com.natay.games.snake;

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