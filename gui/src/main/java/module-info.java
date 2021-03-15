module com.natay.games.snake.gui {
    requires com.natay.games.snake.core;
    requires javafx.controls;
    requires javafx.media;
    requires org.slf4j;

    opens com.natay.games.snake.gui to javafx.controls;

    exports com.natay.games.snake.gui;
}