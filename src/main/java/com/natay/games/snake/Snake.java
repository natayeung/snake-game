package com.natay.games.snake;

import javafx.geometry.Point2D;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author natayeung
 */
public class Snake {

    private final LinkedList<Point2D> body = new LinkedList<>();
    private Direction movingDirection;

    public Snake(Point2D head, Direction movingDirection) {
        checkNotNull(head, "Head must be specified");
        checkNotNull(movingDirection, "Moving direction must be specified");

        this.body.offer(head);
        this.movingDirection = movingDirection;
    }

    /**
     * @return the head of this snake.
     */
    public Point2D getHead() {
        return body.peek();
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
     * Change the moving direction of this snake.
     *
     * @param movingDirection
     */
    public void changeMovingDirection(Direction movingDirection) {
        this.movingDirection = checkNotNull(movingDirection);
    }

    /**
     * Move this snake in the direction it is moving by 1 unit.
     */
    public void move() {
        final Point2D head = getHead().add(movingDirection.vector());
        body.offerFirst(head);
        body.pollLast();
    }

    /**
     * @param food
     * @return true if the head of this snake is colliding with given food; false otherwise.
     */
    public boolean isCollidingWith(Food food) {
        checkNotNull(food, "food must be specified");

        return getHead().equals(food.getPosition());
    }

    /**
     * @return true if the head of this snake is colliding with its own body; false otherwise.
     */
    public boolean isCollidingWithBody() {
        return body.lastIndexOf(getHead()) != 0;
    }

    /**
     * @param boundX
     * @param boundY
     * @return true if the head of this snake is colliding with the edge of the game board; false otherwise.
     */
    public boolean isCollidingWithEdgeOfBoard(int boundX, int boundY) {
        final Point2D head = getHead();
        return head.getX() < 0 || head.getX() >= boundX
                || head.getY() < 0 || head.getY() >= boundY;
    }

    /**
     * Grow this snake by extending its tail by 1 unit in length.
     */
    public void grow() {
        if (body.isEmpty())
            return;

        final Point2D newTail = getTail().subtract(movingDirection.vector());
        body.offerLast(newTail);
    }

    private Point2D getTail() {
        return body.peekLast();
    }

    @Override
    public String toString() {
        return "Snake{" +
                "body=" + body +
                ", movingDirection=" + movingDirection +
                '}';
    }
}