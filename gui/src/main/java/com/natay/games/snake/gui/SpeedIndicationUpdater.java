package com.natay.games.snake.gui;

import com.natay.games.snake.core.service.GameState;
import com.natay.games.snake.gui.configuration.DisplayText;
import com.natay.games.snake.gui.configuration.ResourceNames;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
class SpeedIndicationUpdater implements NodeInitializer, StateChangeObserver {

    private static final Logger logger = LoggerFactory.getLogger(SpeedIndicationUpdater.class);

    private final ProgressBar speedIndicator = new ProgressBar(0.0);
    private double lastIndication;

    SpeedIndicationUpdater() {
    }

    @Override
    public Node initialize() {
        final Pane speedPane = UIUtils.newHorizontalPaneWithStyle("speed-indicator-pane");
        final Label speedLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(speedLabel, ResourceNames.SPEED_ICON, DisplayText.SPEED);
        speedPane.getChildren().addAll(speedLabel, speedIndicator);

        logger.info("Speed indicator pane initialized");

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