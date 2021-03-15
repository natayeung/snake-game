package com.natay.games.snake.core.service;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

/**
 * @author natayeung
 */
public class SchedulingContext {

    private final Duration initialMoveInterval;
    private final Duration minMoveInterval;
    private final Duration moveIntervalDecrement;
    private final int scoreMilestoneForIntervalChange;

    private SchedulingContext(Builder builder) {
        initialMoveInterval = requireNonNull(builder.initialMoveInterval, "Initial move interval must be specified");
        minMoveInterval = requireNonNull(builder.minMoveInterval, "Min move interval must be specified");
        moveIntervalDecrement = requireNonNull(builder.moveIntervalDecrement, "Move interval decrement must be specified");
        scoreMilestoneForIntervalChange = builder.scoreMilestoneForIntervalChange;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public Duration initialMoveInterval() {
        return initialMoveInterval;
    }

    public Duration minMoveInterval() {
        return minMoveInterval;
    }

    public Duration moveIntervalDecrement() {
        return moveIntervalDecrement;
    }

    public int scoreMilestoneForIntervalChange() {
        return scoreMilestoneForIntervalChange;
    }

    public static final class Builder {
        private Duration initialMoveInterval;
        private Duration minMoveInterval;
        private Duration moveIntervalDecrement;
        private int scoreMilestoneForIntervalChange;

        private Builder() {
        }

        public Builder withInitialMoveInterval(Duration initialMoveInterval) {
            this.initialMoveInterval = initialMoveInterval;
            return this;
        }

        public Builder withMinMoveInterval(Duration minMoveInterval) {
            this.minMoveInterval = minMoveInterval;
            return this;
        }

        public Builder withMoveIntervalDecrement(Duration moveIntervalDecrement) {
            this.moveIntervalDecrement = moveIntervalDecrement;
            return this;
        }

        public Builder withScoreMilestoneForIntervalChange(int scoreMilestone) {
            this.scoreMilestoneForIntervalChange = scoreMilestone;
            return this;
        }

        public SchedulingContext build() {
            return new SchedulingContext(this);
        }
    }
}
