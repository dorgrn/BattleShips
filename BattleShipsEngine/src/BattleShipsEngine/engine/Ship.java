package BattleShipsEngine.engine;
import generated.BattleShipGame;

import java.util.ArrayList;
import java.util.List;
import static BattleShipsEngine.engine.GameBoard.isIndexInBoard;

public class Ship extends GamePiece {
    private final static int DEFAULT_SCORE = 1;
    private GameBoard board;
    private NavalFactory.GamePieceType shape;
    private BattleShipGame.ShipTypes.ShipType shipType;
    private int score = DEFAULT_SCORE;
    private int length;

    public Ship(Point position, int length, NavalFactory.GamePieceType type) {
        super(length, position);

        this.length = length;

        this.shape = type;


        createBody(type);
    }

    public static List<Point> getPointsRangeInDirection(Point position, Direction direction, int length) {
        List<Point> result = new ArrayList<>(length);
        Point toInsert = Point.DeepClone(position);

        int deltaX = 0;
        int deltaY = 0;

        switch (direction) {
            case LEFT:
                deltaY = -1;
                break;
            case RIGHT:
                deltaY = 1;
                break;
            case DOWN:
                deltaX = 1;
                break;
            case UP:
                deltaX = -1;
                break;
        }

        for ( int i = 0; i < length; i++ ) {
            result.add(toInsert);
            toInsert = new Point(toInsert.getX() + deltaX, toInsert.getY() + deltaY);
        }

        return result;
    }

    public void addAdditionalDetails(BattleShipGame.ShipTypes.ShipType shipType, int score) {
        this.shipType = shipType;
        this.score = score;
    }

    protected void createBody(NavalFactory.GamePieceType type) {
        List<Point> firstDirection = null;
        List<Point> secondDirection = null;

        if (shape == null) {
            throw new UnsupportedOperationException("Shape of ship is null in createLShapedBody");
        }
        switch (shape) {

            case MINE:
                throw new UnsupportedOperationException("bad type in L shape: " + shape.name);
            case REGULAR_SHIP_COLUMN:
                firstDirection = getPointsRangeInDirection(position, Direction.DOWN, length);
                break;
            case REGULAR_SHIP_ROW:
                firstDirection = getPointsRangeInDirection(position, Direction.RIGHT, length);
                break;
            case L_SHAPED_RIGHT_DOWN:
                firstDirection = getPointsRangeInDirection(position, Direction.LEFT, length);
                secondDirection = getPointsRangeInDirection(position, Direction.DOWN, length);
                break;
            case L_SHAPED_RIGHT_UP:
                firstDirection = getPointsRangeInDirection(position, Direction.LEFT, length);
                secondDirection = getPointsRangeInDirection(position, Direction.UP, length);
                break;
            case L_SHAPED_UP_RIGHT:
                firstDirection = getPointsRangeInDirection(position, Direction.DOWN, length);
                secondDirection = getPointsRangeInDirection(position, Direction.RIGHT, length);
                break;
            case L_SHAPED_DOWN_RIGHT:
                firstDirection = getPointsRangeInDirection(position, Direction.UP, length);
                secondDirection = getPointsRangeInDirection(position, Direction.RIGHT, length);
                break;
        }

        if (firstDirection != null) {
            for ( Point point : firstDirection ) {
                body.put(point, new BodyPart(point));
            }
        }

        if (secondDirection != null) {
            for ( Point point : secondDirection ) {
                body.put(point, new BodyPart(point));
            }
        }
    }

    public boolean isPointShipEnd(Point point) {
        boolean result = false;

        if (!isIndexInBoard(point.getX(), point.getY(), board.length)) {
            return false;
        }

        switch (shape) {
            case REGULAR_SHIP_COLUMN:
                result = (isIndexInBoard(point.getX() - 1, point.getY(), board.length) &&
                        !this.hasPoint(point))
                        || (isIndexInBoard(point.getX() + 1, point.getY(), board.length) &&
                        !this.hasPoint(point));
                break;
            case REGULAR_SHIP_ROW:
                result = ((isIndexInBoard(point.getY() - 1, point.getY(), board.length) &&
                        !this.hasPoint(point)))
                        || ((isIndexInBoard(point.getY() + 1, point.getY(), board.length) &&
                        !this.hasPoint(point)));
                break;

        }

        return result;
    }

    private void createLShapedBody() {


    }

    private void createRegularBody() {
//        int deltaX = 0;
//        int deltaY = 0;
//        Point toInsert = Point.DeepClone(this.position);
        List<Point> pointsInShip = null;
        if (shape == NavalFactory.GamePieceType.REGULAR_SHIP_COLUMN) {
            pointsInShip = getPointsRangeInDirection(position, Direction.DOWN, length);
            ;
        } else if (shape == NavalFactory.GamePieceType.REGULAR_SHIP_ROW) {
            pointsInShip = getPointsRangeInDirection(position, Direction.RIGHT, length);
        }

        if (pointsInShip != null) {
            for ( Point point : pointsInShip ) {
                body.put(point, new BodyPart(point));
            }
        }


        //        for ( int i = 0; i < length; i++ ) {
//            this.body.put(toInsert, new GamePiece.BodyPart(toInsert));
//            toInsert = new Point(toInsert.getX() + deltaX, toInsert.getY() + deltaY);
//        }
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public BattleShipGame.ShipTypes.ShipType getId() {
        return shipType;
    }

    public void setId(BattleShipGame.ShipTypes.ShipType shipType) {
        this.shipType = shipType;
    }


    public NavalFactory.GamePieceType getShape() {
        return shape;
    }


    public boolean isInVerticalPart(Point point) {
        return point != null && body.containsKey(point) && (body.containsKey(new Point(point.getX() - 1, point.getY())) || body.containsKey(new Point(point.getX() + 1, point.getY())));

    }

    public boolean isInHorizontalPart(Point point) {
        return point != null && body.containsKey(point) && (body.containsKey(new Point(point.getX(), point.getY() + 1)) || body.containsKey(new Point(point.getX(), point.getY() - 1)));

    }

    public BattleShipGame.ShipTypes.ShipType getShipType() {
        return shipType;
    }

    public enum Direction {
        LEFT,
        RIGHT,
        DOWN,
        UP
    }
}
