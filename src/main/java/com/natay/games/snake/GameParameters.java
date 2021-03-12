package com.natay.games.snake;

import javafx.scene.paint.Color;

import java.time.Duration;

/**
 * @author natayeung
 */
public class GameParameters {

    public static final int TOTAL_TILES_X = 30;
    public static final int TOTAL_TILES_Y = 30;
    public static final int TILE_SIZE_PX = 16;
    public static final int ICON_SIZE_PX = 24;

    public static final int WINDOW_HEIGHT = 624;
    public static final int WINDOW_WIDTH = 510;

    public static final Color EMPTY_COLOR = Color.FLORALWHITE;
    public static final Color SNAKE_COLOR = Color.PERU;
    public static final Color FOOD_COLOR = Color.TOMATO;

    public static final Duration INITIAL_MOVE_INTERVAL = Duration.ofMillis(500);
    public static final Duration MIN_MOVE_INTERVAL = Duration.ofMillis(100);
    public static final Duration MOVE_INTERVAL_DECREMENT = Duration.ofMillis(50);
    public static final int SCORE_MILESTONE_FOR_SPEED_CHANGE = 5;

    public static class DisplayText {
        public static final String GAME_OVER_MESSAGE = "GAME OVER";
        public static final String RETRY = "Retry";
        public static final String QUIT = "Quit";
        public static final String SCORE = "Score:";
        public static final String SPEED = "Speed:";
        public static final String TITLE = "Snake";
        public static final String CLOSE = "X";

        private DisplayText() {
        }
    }

    public static class Resources {
        public static final String STYLESHEET = "/css/styles.css";
        public static final String BITE_SOUND_CLIP = "/sound/bite.mp3";
        public static final String GAME_OVER_SOUND_CLIP = "/sound/gameover.mp3";
        public static final String SPEED_ICON = "/images/speed.png";
        public static final String SNAKE_ICON = "/images/snake.png";
        public static final String CLOSE_BUTTON_ICON = "/images/close_button.png";
        public static final String APPLE_ICON = "/images/apple.png";
    }

    private GameParameters() {
    }
}