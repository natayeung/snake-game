package com.natay.games.snake.core.service;

import com.natay.games.snake.core.common.Direction;

import java.time.Duration;

import static java.time.Duration.ofMillis;

/**
 * @author natayeung
 */
class TestHelper {

    static ExecutionContext someExecutionContext() {
        return newExecutionContext(3, 3);
    }

    static ExecutionContext newExecutionContext(int totalTilesX, int totalTilesY) {
        return newExecutionContext(totalTilesX, totalTilesY, Direction.RIGHT);
    }

    static ExecutionContext newExecutionContext(int totalTilesX, int totalTilesY, Direction initialMovingDirection) {
        SchedulingContext schedulingContext = SchedulingContext.newBuilder().withInitialMoveInterval(ofMillis(2))
                .withMinMoveInterval(ofMillis(1))
                .withMoveIntervalDecrement(ofMillis(1))
                .withScoreMilestoneForIntervalChange(2)
                .build();

        return ExecutionContext.newBuilder().withTotalTilesX(totalTilesX)
                .withTotalTilesY(totalTilesY)
                .withSchedulingContext(schedulingContext)
                .withInitialMovingDirection(initialMovingDirection)
                .build();
    }

    static SchedulingContext someSchedulingContext() {
        return SchedulingContext.newBuilder()
                .withInitialMoveInterval(Duration.ofMillis(300))
                .withMinMoveInterval(Duration.ofMillis(50))
                .withMoveIntervalDecrement(Duration.ofMillis(10))
                .withScoreMilestoneForIntervalChange(3)
                .build();
    }

    private TestHelper() {
    }
}
