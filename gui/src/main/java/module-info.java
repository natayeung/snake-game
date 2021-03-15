module com.natay.games.snake.gui {
    requires com.natay.games.snake.core;
    requires javafx.controls;
    requires javafx.media;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires transitive java.naming; // Added for a known issue with logback (https://jira.qos.ch/browse/LOGBACK-1515)

    opens com.natay.games.snake.gui to javafx.controls;

    exports com.natay.games.snake.gui;
}