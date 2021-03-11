package com.natay.games.snake;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class ScoreUpdater implements SnakeGameUserInterface.View.ComponentInitializer, SnakeGameUserInterface.View.StateChangeObserver {

    private final Text scoreValue = new Text("");
    private int lastScore = -1;

    public ScoreUpdater() {
    }

    @Override
    public Node initialize() {
        final Pane scorePane = UIUtils.newHorizontalPaneWithStyle("score-pane");
        final Label scoreLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(scoreLabel, GameParameters.Resources.APPLE_ICON, GameParameters.DisplayText.SCORE);

        scorePane.getChildren().addAll(scoreLabel, scoreValue);

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