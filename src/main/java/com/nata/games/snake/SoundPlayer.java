package com.nata.games.snake;

import javafx.scene.media.AudioClip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Optional;

import static com.nata.games.snake.GameParameters.Resources.BITE_SOUND_CLIP;
import static com.nata.games.snake.GameParameters.Resources.GAME_OVER_SOUND_CLIP;
import static java.util.Objects.isNull;

/**
 * @author natayeung
 */
public class SoundPlayer implements SnakeGameUserInterface.View.StateChangeObserver {

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
            playSound(BITE_SOUND_CLIP);
        }
    }

    private void playSoundIfGameOver(GameState gameState) {
        if (gameState.isGameOver()) {
            playSound(GAME_OVER_SOUND_CLIP);
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