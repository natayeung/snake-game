package com.nata.games.snake;

import javafx.scene.paint.Color;

import java.time.Duration;

/**
 * @author natayeung
 */
public class GameParameters {

    static final int TOTAL_TILES_X = 30;
    static final int TOTAL_TILES_Y = 30;
    static final int TILE_SIZE_PX = 16;

    static final int WINDOW_HEIGHT = 570;
    static final int WINDOW_WIDTH = 510;

    static final Color EMPTY_COLOR = Color.FLORALWHITE;
    static final Color SNAKE_COLOR = Color.PERU;
    static final Color FOOD_COLOR = Color.TOMATO;

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
        static final String STYLESHEET = "/css/stylesheet.css";
        static final String BITE_SOUND_CLIP = "/sound/bite.mp3";
        static final String GAME_OVER_SOUND_CLIP = "/sound/gameover.mp3";
        static final String SPEED_IMAGE = "/images/speed.png";
    }

    private GameParameters() {
    }
}