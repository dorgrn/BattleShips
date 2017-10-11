package BattleShipsEngine.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GameBoard implements Serializable {
    public static final char HIT_SYMBOL = 'H';
    public static final char MISS_SYMBOL = 'L';
    public static final char EMPTY_SYMBOL = '-';
    public static final char SHIP_SYMBOL = 'S';
    public static final char MINE_SYMBOL = 'M';
    public final int length; // one row/column length
    private char[][] board;
    private static final Logger LOGGER = Logger.getLogger(GameConfig.class.getName());

    public GameBoard(int boardLength) {
        this.length = boardLength;
        initializeBoard();
    }

    public static boolean isIndexInBoard(int x, int y, int boardLength) {
        return x >= 0 && x < boardLength && y >= 0 && y < boardLength;
    }

    public static Point getEmptyNextToHit(GameBoard trackingGrid, Player player) {
        Point result = null;
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

    public static boolean isRectangleEmpty(final Point point, int height, int width, Ship.Direction direction, final GameBoard board) {
        if (point == null || !isIndexInBoard(point.getX(), point.getY(), board.length)) {
            return false;
        }

        int rowsUp = 0;
        int rowsDown = 0;
        int colsLeft = 0;
        int colsRight = 0;

        switch (direction) {
            case LEFT:
                rowsUp = point.getX() - 1;
                rowsDown = point.getX() + 1;
                colsLeft = point.getY() - width;
                colsRight = point.getY() + 1;
                break;
            case RIGHT:
                rowsUp = point.getX() - 1;
                rowsDown = point.getX() + 1;
                colsLeft = point.getY() - 1;
                colsRight = point.getY() + width;
                break;
            case DOWN:
                rowsUp = point.getX() - 1;
                rowsDown = point.getX() + height;
                colsLeft = point.getY() - 1;
                colsRight = point.getY() + 1;
                break;
            case UP:
                rowsUp = point.getX() - height;
                rowsDown = point.getX() + 1;
                colsLeft = point.getY() - 1;
                colsRight = point.getY() + 1;
                break;
        }


//        colsRight = rowsDown = Math.max(rowsDown, board.length);
//        colsLeft = rowsUp = Math.min(0, board.length);

        final String logMessage =
                "Checking Ship " + "Height: " + height + " Width:" + width
                        + "Checking rows Up:" + rowsUp + " Down:" + rowsDown
                        + "Checking cols Left:" + colsLeft + " Right:" + colsRight;

        LOGGER.log(Level.FINER, logMessage);

        for ( int i = rowsUp; i <= rowsDown; i++ ) {
            for ( int j = colsLeft; j <= colsRight; j++ ) {
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
}


