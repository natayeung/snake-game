package com.nata.games.snake;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @author natayeung
 */
public class GameParameters {

    static final int TOTAL_TILES_X = 30;
    static final int TOTAL_TILES_Y = 30;

    static final int TILE_SIZE_PX = 16;
    static final int BOARD_PADDING_PX = 15;
    static final int SCORE_DISPLAY_HEIGHT = 30;
    static final int WINDOW_HEIGHT = SCORE_DISPLAY_HEIGHT + TILE_SIZE_PX * TOTAL_TILES_Y + BOARD_PADDING_PX * 2;
    static final int WINDOW_WIDTH = TILE_SIZE_PX * TOTAL_TILES_X + BOARD_PADDING_PX * 2;

    static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    static final Color TILE_COLOR = Color.rgb(224, 242, 241);
    static final Color SNAKE_COLOR = Color.rgb(210, 105, 30);
    static final Color FOOD_COLOR = Color.rgb(255, 0, 0);
    static final Font TEXT_FONT = Font.font("Verdana", 16);

    static final int SNAKE_MOVE_INTERVAL_MILLIS = 300;

    private GameParameters() {
    }
}
