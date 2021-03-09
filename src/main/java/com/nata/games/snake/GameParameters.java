package com.nata.games.snake;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.time.Duration;

import static javafx.scene.text.FontWeight.BOLD;

/**
 * @author natayeung
 */
public class GameParameters {

    static final int TOTAL_TILES_X = 30;
    static final int TOTAL_TILES_Y = 30;

    static final int TILE_SIZE_PX = 16;
    static final int BOARD_PADDING_PX = 15;
    static final int INFO_DISPLAY_HEIGHT = 30;
    static final int WINDOW_HEIGHT = INFO_DISPLAY_HEIGHT * 2 + TILE_SIZE_PX * TOTAL_TILES_Y + BOARD_PADDING_PX * 2;
    static final int WINDOW_WIDTH = TILE_SIZE_PX * TOTAL_TILES_X + BOARD_PADDING_PX * 2;

    static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    static final Color TILE_COLOR = Color.rgb(224, 242, 241);
    static final Color SNAKE_COLOR = Color.rgb(210, 105, 30);
    static final Color FOOD_COLOR = Color.rgb(255, 0, 0);
    static final Color TEXT_COLOR = Color.rgb(64, 64, 64);
    static final Font TEXT_FONT = Font.font("Verdana", BOLD, 14);

    static final Duration INITIAL_MOVE_INTERVAL = Duration.ofMillis(500);
    static final Duration MIN_MOVE_INTERVAL = Duration.ofMillis(100);
    static final Duration MOVE_INTERVAL_DECREMENT = Duration.ofMillis(50);
    static final int SCORE_MILESTONE_FOR_SPEED_CHANGE = 5;

    static class DisplayText {
        static final String GAME_OVER_MESSAGE = "Game Over!";
        static final String NEW_GAME = "New Game";
        static final String EXIT = "Exit";
        static final String SCORE = "Score: ";

        private DisplayText() {
        }
    }

    static class Resources {
        static final String BITE_SOUND_CLIP = "/sound/bite.mp3";
        static final String GAME_OVER_SOUND_CLIP = "/sound/gameover.mp3";
    }

    private GameParameters() {
    }
}