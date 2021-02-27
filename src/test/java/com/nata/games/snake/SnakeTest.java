package com.nata.games.snake;

import javafx.geometry.Point2D;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.nata.games.snake.Direction.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author natayeung
 */
@RunWith(JUnitParamsRunner.class)
public class SnakeTest {

    private final Point2D startingHead = new Point2D(3, 7);
    private Snake snake;

    @Before
    public void setUp() {
        snake = new Snake(startingHead, RIGHT);
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
    @Parameters({
            "UP   |4|7",
            "DOWN |2|7",
            "LEFT |3|6",
            "RIGHT|3|8",
    })
    public void canMoveInDirection(Direction direction, int expectedHeadX, int expectedHeadY) {
        Point2D expectedHead = new Point2D(expectedHeadX, expectedHeadY);

        snake.move(direction);

        assertThat("Snake can move " + direction, snake.getHead(), is(expectedHead));
        assertThat("Snake should move without growing", snake.getLength(), is(1));
    }

    @Test
    public void collidesWithFood() {
        Food food = new Food(startingHead);

        assertTrue("Snake is colliding with food", snake.isCollidingWith(food));
    }

    @Test
    public void doesNotCollideWithFood() {
        Food food = new Food(startingHead.add(new Point2D(1, 0)));

        assertFalse("Snake is not colliding with food", snake.isCollidingWith(food));
    }

    @Test
    public void collidesWithBody() {
        growSnake(5);

        snake.move(UP);
        snake.move(LEFT);
        snake.move(DOWN);

        assertTrue("Snake is colliding with body", snake.isCollidingWithBody());
    }

    @Test
    public void doesNotCollideWithBody() {
        growSnake(5);

        snake.move(RIGHT);

        assertFalse("Snake is not colliding with body", snake.isCollidingWithBody());
    }

    @Test
    public void canGrow() {
        Point2D[] newBodyParts = new Point2D[]{startingHead, new Point2D(3, 6), new Point2D(3, 5), new Point2D(3, 4)};

        growSnake(3);

        assertThat(snake.getLength(), is(4));
        assertThat(snake.getBody(), contains(newBodyParts));
    }

    private void growSnake(int n) {
        for (int i = 0; i < n; i++) {
            snake.grow();
        }
    }
}