package com.natay.games.snake.gui;

import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Optional;

import static com.natay.games.snake.gui.configuration.Styles.ICON_SIZE_PX;


/**
 * @author natayeung
 */
class UIUtils {

    private static final Logger logger = LoggerFactory.getLogger(UIUtils.class);

    static HBox newHorizontalPaneWithStyle(String styleClass) {
        final HBox pane = new HBox();
        pane.getStyleClass().add(styleClass);
        return pane;
    }

    static void setIconIfFoundOrElseSetText(Labeled control, String iconResourceName, String text) {
        final Optional<Image> icon = loadImage(iconResourceName, ICON_SIZE_PX, ICON_SIZE_PX);
        if (icon.isPresent()) {
            control.setGraphic(new ImageView(icon.get()));
        } else {
            control.setText(text);
        }
    }

    static Optional<Image> loadImage(String resourceName, int width, int height) {
        final Optional<URL> resource = Optional.ofNullable(UIUtils.class.getResource(resourceName));
        if (resource.isPresent()) {
            final Image image = new Image(resource.get().toString(), width, height, true, true, true);
            return Optional.of(image);
        } else {
            logger.warn("Cannot load image, file {} not found", resourceName);
            return Optional.empty();
        }
    }

    private UIUtils() {
    }
}