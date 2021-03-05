package com.nata.games.snake;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author natayeung
 */
public class SnakeApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameBuilder.build(primaryStage);
    }
}
