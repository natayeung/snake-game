package com.natay.games.snake.core.service;

import com.natay.games.snake.core.common.Direction;

import static com.natay.games.snake.core.common.Validation.requireArgument;
import static java.util.Objects.requireNonNull;

/**
 * @author natayeung
 */
public class ExecutionContext {
    private final int totalTilesX;
    private final int totalTilesY;
    private final Direction initialMovingDirection;
    private final SchedulingContext schedulingContext;

    private ExecutionContext(Builder builder) {
        requireArgument(builder.totalTilesX > 1, "Total Tiles X must be greater than 1");
        requireArgument(builder.totalTilesY > 1, "Total Tiles Y must be greater than 1");
        requireNonNull(builder.initialMovingDirection, "Initial moving direction must be specified");
        requireNonNull(builder.schedulingContext, "Scheduling context must be specified");

        totalTilesX = builder.totalTilesX;
        totalTilesY = builder.totalTilesY;
        initialMovingDirection = builder.initialMovingDirection;
        schedulingContext = builder.schedulingContext;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int totalTilesX() {
        return totalTilesX;
    }

    public int totalTilesY() {
        return totalTilesY;
    }

    public Direction initialMovingDirection() {
        return initialMovingDirection;
    }

    public SchedulingContext schedulingContext() {
        return schedulingContext;
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "totalTilesX=" + totalTilesX +
                ", totalTilesY=" + totalTilesY +
                ", initialMovingDirection=" + initialMovingDirection +
                ", schedulingContext=" + schedulingContext +
                '}';
    }

    public static final class Builder {
        private int totalTilesX;
        private int totalTilesY;
        private Direction initialMovingDirection;
        private SchedulingContext schedulingContext;

        private Builder() {
        }

        public Builder withTotalTilesX(int totalTilesX) {
            this.totalTilesX = totalTilesX;
            return this;
        }

        public Builder withTotalTilesY(int totalTilesY) {
            this.totalTilesY = totalTilesY;
            return this;
        }

        public Builder withInitialMovingDirection(Direction initialMovingDirection) {
            this.initialMovingDirection = initialMovingDirection;
            return this;
        }

        public Builder withSchedulingContext(SchedulingContext schedulingContext) {
            this.schedulingContext = schedulingContext;
            return this;
        }

        public ExecutionContext build() {
            return new ExecutionContext(this);
        }
    }
}
