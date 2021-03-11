package com.natay.games.snake;

import javafx.geometry.Point2D;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.natay.games.snake.Direction.*;
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
            "UP   |3|6",
            "DOWN |3|8",
            "LEFT |2|7",
            "RIGHT|4|7",
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

        snake.changeMovingDirection(UP);
        snake.move();
        snake.changeMovingDirection(LEFT);
        snake.move();
        snake.changeMovingDirection(DOWN);
        snake.move();

        assertTrue("Snake is colliding with body", snake.isCollidingWithBody());
    }

    @Test
    public void doesNotCollideWithBody() {
        growSnake(5);

        snake.changeMovingDirection(UP);
        snake.move();

        assertFalse("Snake is not colliding with body", snake.isCollidingWithBody());
    }

    @Test
    @Parameters({
            "3|8",
            "4|7",
    })
    public void collidesWithEdgeOfBoard(int boundX, int boundY) {
        assertTrue("Snake is colliding with edge of board", snake.isCollidingWithEdgeOfBoard(boundX, boundY));
    }

    @Test
    @Parameters({
            "4|8",
            "9|9"
    })
    public void doesNotCollideWithEdgeOfBoard(int boundX, int boundY) {
        assertFalse("Snake is not colliding with edge of board", snake.isCollidingWithEdgeOfBoard(boundX, boundY));
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