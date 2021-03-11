package com.natay.games.snake;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class SpeedIndicationUpdater implements SnakeGameUserInterface.View.ComponentInitializer, SnakeGameUserInterface.View.StateChangeObserver {

    private final ProgressBar speedIndicator = new ProgressBar(0.0);
    private double lastIndication;

    public SpeedIndicationUpdater() {
    }

    @Override
    public Node initialize() {
        final Pane speedPane = UIUtils.newHorizontalPaneWithStyle("speed-indicator-pane");
        final Label speedLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(speedLabel, GameParameters.Resources.SPEED_ICON, GameParameters.DisplayText.SPEED);

        speedPane.getChildren().addAll(speedLabel, speedIndicator);

        return speedPane;
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