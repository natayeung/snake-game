package com.natay.games.snake.ui;

import com.natay.games.snake.GameParameters;
import com.natay.games.snake.dto.GameState;
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
        UIUtils.setIconIfFoundOrElseSetText(scoreLabel, GameParameters.Resources.APPLE_ICON, GameParameters.DisplayText.SCORE);
        scorePane.getChildren().addAll(scoreLabel, scoreValue);

        logger.info("Score pane initialized");

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
        }
    }
}