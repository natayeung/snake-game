package com.nata.games.snake;

import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.nata.games.snake.Direction.RIGHT;

/**
 * @author natayeung
 */
public class Snake {

    private Direction movingDirection;
    private Point2D head;
    private Deque<Point2D> body = new LinkedList<>();

    public Snake(Point2D head, Direction movingDirection) {
        checkNotNull(head);
        checkNotNull(movingDirection);

        this.head = head;
        this.body.offerFirst(head);
        this.movingDirection = movingDirection;
    }

    public Snake(Point2D head) {
        this(head, RIGHT);
    }

    /**
     * @return the head of this snake.
     */
    public Point2D getHead() {
        return head;
    }

    /**
     * @return the body of this snake, including its head.
     */
    public Collection<Point2D> getBody() {
        return Collections.unmodifiableCollection(body);
    }

    /**
     * @return the length of this snake.
     */
    public int getLength() {
        return body.size();
    }

    /**
     * Move this snake in given direction by 1 unit.
     *
     * @param movingDirection
     */
    public void move(Direction movingDirection) {
        checkNotNull(movingDirection);

        this.movingDirection = movingDirection;
        head = head.add(movingDirection.vector());
        body.offerFirst(head);
    }

    /**
     * @param food
     * @return true if this snake is colliding with given food; false otherwise.
     */
    public boolean isCollidingWith(Food food) {
        checkNotNull(food);

        return head.equals(food.getPosition());
    }

    /**
     * Grow this snake by extending its tail by 1 unit in length.
     */
    public void grow() {
        if (body.isEmpty())
            return;

        Point2D tail = body.peekLast();
        Point2D newTail = tail.subtract(movingDirection.vector());
        body.offerLast(newTail);
    }
}
