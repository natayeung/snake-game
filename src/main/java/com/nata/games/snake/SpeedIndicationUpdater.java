package com.nata.games.snake;

import javafx.scene.control.ProgressBar;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class SpeedIndicationUpdater implements SnakeGameUserInterface.View.StateChangeObserver {

    private final ProgressBar speedIndicator;
    private double lastIndication;

    public SpeedIndicationUpdater(ProgressBar speedIndicator) {
        this.speedIndicator = checkNotNull(speedIndicator, "Speed indicator must be specified");
    }

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        final double speedIndication = gameState.getSpeedIndication();
        if (speedIndication != lastIndication) {
            speedIndicator.setProgress(speedIndication);
            lastIndication = speedIndication;
        }
    }
}