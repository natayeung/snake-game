package com.natay.games.snake.ui;

import com.natay.games.snake.GameParameters;
import com.natay.games.snake.dto.GameState;
import javafx.scene.media.AudioClip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Optional;

import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
class SoundPlayer implements StateChangeObserver {

    private static final Logger logger = LoggerFactory.getLogger(SoundPlayer.class);

    @Override
    public void stateChanged(GameState gameState) {
        if (isNull(gameState))
            return;

        playSoundIfFoodCaughtOnLastMove(gameState);
        playSoundIfGameOver(gameState);
    }

    private void playSoundIfFoodCaughtOnLastMove(GameState gameState) {
        if (gameState.isFoodCaughtOnLastMove()) {
            playSound(GameParameters.Resources.BITE_SOUND_CLIP);
        }
    }

    private void playSoundIfGameOver(GameState gameState) {
        if (gameState.isGameOver()) {
            playSound(GameParameters.Resources.GAME_OVER_SOUND_CLIP);
        }
    }

    private void playSound(String resourceName) {
        final Optional<URL> resource = Optional.ofNullable(getClass().getResource(resourceName));
        if (resource.isPresent()) {
            final AudioClip sound = new AudioClip(resource.get().toString());
            sound.play();
        } else {
            logger.warn("Cannot play sound, clip {} not found", resourceName);
        }
    }
}