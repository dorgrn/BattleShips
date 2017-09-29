package BattleShipsEngine.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GameBoard implements Serializable {
    public static final char HIT_SYMBOL = 'H';
    public static final char MISS_SYMBOL = 'L';
    public static final char EMPTY_SYMBOL = '-';
    public static final char SHIP_SYMBOL = 'S';
    public static final char MINE_SYMBOL = 'M';
    public final int length; // one row/column length
    private char[][] board;

    public GameBoard(int boardLength) {
        this.length = boardLength;
        initializeBoard();
    }

    public static boolean isIndexInBoard(int x, int y, int boardLength) {
        return x >= 0 && x < boardLength && y >= 0 && y < boardLength;
    }

    /*
    public static boolean isRectangleEmpty(Point center, int height, int width, GameBoard board) {
        if (center == null || !isIndexInBoard(center.getY(), center.getX(), board.length)) {
            return false;
        }

        int x = center.getX();
        int y = center.getY();
        char[][] tempBoard = board.getBoard();

        for ( int i = x - (width / 2); i <= x + (width / 2); i++ ) {
            for ( int j = y - (height / 2); j <= y + (height / 2); j++ ) {
                // if index is illegal continue
                if (!isIndexInBoard(i, j, board.length)) {
                    continue;
                }

                //otherwise check that empty
                if (tempBoard[i][j] != GameBoard.EMPTY_SYMBOL) {
                    return false;
                }
            }
        }

        return true;
    }*/

    public static Point getEmptyNextToHit(GameBoard trackingGrid, Player player) {
        Point result;
        for ( Ship ship : player.getShips() ) {
            List<Point> hitPoints = ship.getHitPoints();
            for ( Point point : hitPoints ) {
                result = trackingGrid.getEmptyNeighbour(point);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    public static Point getRandomPointInBoard(int boardLength) {
        return new Point(getRandomInRange(0, boardLength - 1), getRandomInRange(0, boardLength - 1));
    }

    public static int getRandomInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean isRectangleEmpty(Point point, int deltaX, int deltaY, int width, int height, GameBoard board) {
        if (point == null || !isIndexInBoard(point.getX(), point.getY(), board.length)) {
            return false;
        }

        Point xParams = getStartPointsOfRectangle(deltaX, point.getX(), height);
        Point yParams = getStartPointsOfRectangle(deltaY, point.getY(), width);

        for ( int i = xParams.getX(); i <= xParams.getY(); i++ ) {
            for ( int j = yParams.getX(); j <= yParams.getY(); j++ ) {
                // if index is illegal continue
                if (!isIndexInBoard(j, i, board.length)) {
                    continue;
                }

                //otherwise check that empty
                if (board.getPointInBoard(new Point(i, j)) != GameBoard.EMPTY_SYMBOL) {
                    return false;
                }
            }
        }

        return true;
    }

    private static Point getStartPointsOfRectangle(int delta, int position, int difference) {
        int startPoint;
        int endPoint;

        if (delta > 0) {
            startPoint = position - 1;
            endPoint = position + difference;
        } else {
            startPoint = position - difference;
            endPoint = position + 1;
        }

        return new Point(startPoint, endPoint);
    }

    public void initializeBoard() {
        this.board = new char[this.length][this.length];

        for ( int i = 0; i < length; i++ ) {
            for ( int j = 0; j < length; j++ ) {
                board[i][j] = EMPTY_SYMBOL;
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char getPointInBoard(Point point) {
        return board[point.getX()][point.getY()];
    }

    public void setPointInBoard(Point point, char setTo) {
        board[point.getX()][point.getY()] = setTo; // and that's it
    }

    public String getBoardAsString() {
        StringBuilder returnVal = new StringBuilder();
        for ( int i = 0; i < length; i++ ) {
            for ( int j = 0; j < length; j++ ) {
                returnVal.append(board[i][j]);
            }
            returnVal.append('\n');
        }

        return returnVal.toString();
    }

    public Point getEmptyNeighbour(Point point) {
        // search for valid points in all 4 directions
        Point leftNeighbour = new Point(point.getX(), point.getY() - 1);
        Point rightNeighbour = new Point(point.getX(), point.getY() + 1);
        Point downNeighbour = new Point(point.getX() - 1, point.getY());
        Point upNeighbour = new Point(point.getX() + 1, point.getY());

        List<Point> validPoints = new ArrayList<>();
        if (isValidEmptyPoint(leftNeighbour)) {
            validPoints.add(leftNeighbour);
        }
        if (isValidEmptyPoint(rightNeighbour)) {
            validPoints.add(rightNeighbour);
        }
        if (isValidEmptyPoint(upNeighbour)) {
            validPoints.add(upNeighbour);
        }
        if (isValidEmptyPoint(downNeighbour)) {
            validPoints.add(downNeighbour);
        }

        if (validPoints.size() == 0) {
            return null;
        }
        // get random point of the valid points
        return validPoints.get(getRandomInRange(0, validPoints.size() - 1));
    }

    public boolean isValidEmptyPoint(Point point) {
        return point != null && isIndexInBoard(point.getX(), point.getY(), length)
                && getPointInBoard(point) == GameBoard.EMPTY_SYMBOL;
    }
}


