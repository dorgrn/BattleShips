package BattleShipsEngine.engine;

public class Mine extends GamePiece {
    public static final int MINE_RECTANGLE = 3;
    private static final int MINE_SIZE = 1;

    public Mine(Point position) {
        super(MINE_SIZE, position);
    }

    @Override
    protected void createBody(NavalFactory.GamePieceType type) {
        // a mine's body is its position
        this.body.put(position, new GamePiece.BodyPart(position));
    }
}
