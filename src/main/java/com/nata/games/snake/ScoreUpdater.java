package com.nata.games.snake;

import javafx.scene.text.Text;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class ScoreUpdater implements SnakeGameUserInterface.View.StateChangeObserver {

    private final Text scoreField;
    private int lastScore = -1;

    public ScoreUpdater(Text scoreField) {
        this.scoreField = checkNotNull(scoreField, "Score field must be specified");
    }

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        int score = gameState.getScore();
        if (score != lastScore) {
            scoreField.setText(String.valueOf(score));
            lastScore = score;
        }
    }
}