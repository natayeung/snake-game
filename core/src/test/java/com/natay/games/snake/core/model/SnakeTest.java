package com.natay.games.snake.core.model;

import com.natay.games.snake.core.common.Direction;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.natay.games.snake.core.common.Direction.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author natayeung
 */
public class SnakeTest {

    private final Point2D startingHead = new Point2D(3, 7);
    private Snake snake;

    @BeforeEach
    public void setUp() {
        snake = new Snake(startingHead, RIGHT);
    }

    @Test
    public void cannotBeInstantiatedWithNullHead() {
        assertThrows(NullPointerException.class,
                () -> new Snake(null, RIGHT));
    }

    @Test
    public void cannotBeInstantiatedWithNullMovingDirection() {
        assertThrows(NullPointerException.class,
                () -> new Snake(startingHead, null));
    }

    @Test
    public void hasHead() {
        Point2D head = snake.getHead();

        assertThat(head, is(startingHead));
    }

    @Test
    public void hasLength() {
        int length = snake.getLength();

        assertThat(length, is(1));
    }

    @Test
    public void hasMovingDirection() {
        Direction movingDirection = snake.getMovingDirection();

        assertThat(movingDirection, is(RIGHT));
    }

    @ParameterizedTest
    @CsvSource({
            "UP   ,3,6",
            "DOWN ,3,8",
            "LEFT ,2,7",
            "RIGHT,4,7"
    })
    public void canMoveInSpecifiedDirection(Direction direction, int expectedHeadX, int expectedHeadY) {
        Point2D expectedHead = new Point2D(expectedHeadX, expectedHeadY);

        snake.changeMovingDirection(direction);
        snake.move();

        assertThat("Snake can move " + direction, snake.getHead(), is(expectedHead));
        assertThat("Snake should move without growing", snake.getLength(), is(1));
    }

    @Test
    public void collidesWithFood() {
        Food food = new Food(startingHead);

        assertTrue(snake.isCollidingWith(food), "Snake is colliding with food");
    }

    @Test
    public void doesNotCollideWithFood() {
        Food food = new Food(startingHead.add(new Point2D(1, 0)));

        assertFalse(snake.isCollidingWith(food), "Snake is not colliding with food");
    }

    @Test
    public void collidesWithBody() {
        growSnake(5);

        snake.changeMovingDirection(UP);
        snake.move();
        snake.changeMovingDirection(LEFT);
        snake.move();
        snake.changeMovingDirection(DOWN);
        snake.move();

        assertTrue(snake.isCollidingWithBody(), "Snake is colliding with body");
    }

    @Test
    public void doesNotCollideWithBody() {
        growSnake(5);

        snake.changeMovingDirection(UP);
        snake.move();

        assertFalse(snake.isCollidingWithBody(), "Snake is not colliding with body");
    }

    @ParameterizedTest
    @CsvSource({
            "3,8",
            "4,7",
    })
    public void collidesWithEdgeOfBoard(int boundX, int boundY) {
        assertTrue(snake.isCollidingWithEdgeOfBoard(boundX, boundY), "Snake is colliding with edge of board");
    }

    @ParameterizedTest
    @CsvSource({
            "4,8",
            "9,9"
    })
    public void doesNotCollideWithEdgeOfBoard(int boundX, int boundY) {
        assertFalse(snake.isCollidingWithEdgeOfBoard(boundX, boundY), "Snake is not colliding with edge of board");
    }

    @Test
    public void canGrow() {
        Point2D[] expectedBodyParts = new Point2D[]{startingHead, new Point2D(2, 7), new Point2D(1, 7), new Point2D(0, 7)};

        growSnake(3);

        assertThat(snake.getLength(), is(4));
        assertThat(snake.getBody(), contains(expectedBodyParts));
    }

    private void growSnake(int n) {
        for (int i = 0; i < n; i++) {
            snake.grow();
        }
    }
}