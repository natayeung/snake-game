package com.natay.games.snake.gui;


import com.natay.games.snake.core.service.GameState;
import com.natay.games.snake.gui.configuration.DisplayText;
import com.natay.games.snake.gui.configuration.ResourceNames;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
class ScoreUpdater implements NodeInitializer, StateChangeObserver {

    private static final Logger logger = LoggerFactory.getLogger(ScoreUpdater.class);

    private final Text scoreValue = new Text("");
    private int lastScore = -1;

    ScoreUpdater() {
    }

    @Override
    public Node initialize() {
        final Pane scorePane = UIUtils.newHorizontalPaneWithStyle("score-pane");
        final Label scoreLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(scoreLabel, ResourceNames.APPLE_ICON, DisplayText.SCORE);
        scorePane.getChildren().addAll(scoreLabel, scoreValue);

        logger.info("Score initialized");

        return scorePane;
    }

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        int score = gameState.getScore();
        if (score != lastScore) {
            scoreValue.setText(String.valueOf(score));
            lastScore = score;

            logger.debug("Updated score={}", score);
        }
    }
}