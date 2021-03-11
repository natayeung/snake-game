package com.nata.games.snake;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import static com.nata.games.snake.GameParameters.DisplayText.SCORE;
import static com.nata.games.snake.GameParameters.Resources.APPLE_ICON;
import static com.nata.games.snake.UIUtils.newHorizontalPaneWithStyle;
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
        final Pane scorePane = newHorizontalPaneWithStyle("score-pane");
        final Label scoreLabel = new Label();
        UIUtils.setIconIfFoundOrElseSetText(scoreLabel, APPLE_ICON, SCORE);

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