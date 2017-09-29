package BattleShipsEngine.engine;

import generated.BattleShipGame;

public class NavalFactory {
    public static GamePieceType getPieceTypeFromShipType(BattleShipGame.ShipTypes.ShipType shipType) {
        return getPieceTypeFromString(shipType.getId());
    }


    public static GamePieceType getPieceTypeFromString(String shipid) {
        GamePieceType type = null;

        switch (shipid) {
            case "COLUMN":
                type = GamePieceType.REGULAR_SHIP_COLUMN;
                break;
            case "ROW":
                type = GamePieceType.REGULAR_SHIP_ROW;
                break;
            case "RIGHT_DOWN":
                type = GamePieceType.L_SHAPED_RIGHT_DOWN;
                break;
            case "RIGHT_UP":
                type = GamePieceType.L_SHAPED_RIGHT_UP;
                break;
            case "UP_RIGHT":
                type = GamePieceType.L_SHAPED_UP_RIGHT;
                break;
            case "DOWN_RIGHT":
                type = GamePieceType.L_SHAPED_DOWN_RIGHT;
                break;
        }
        return type;
    }

    public static GamePiece getGamePiece(GamePieceType type, Point position, int length) throws IllegalArgumentException {
        switch (type) {
            case MINE:
                return new Mine(position);
            case REGULAR_SHIP_COLUMN:
                return new Ship(position, length, type);
            case REGULAR_SHIP_ROW:
                return new Ship(position, length, type);
            case L_SHAPED_RIGHT_DOWN:
                return new Ship(position, length, type);
            case L_SHAPED_RIGHT_UP:
                return new Ship(position, length, type);
            case L_SHAPED_UP_RIGHT:
                return new Ship(position, length, type);
            case L_SHAPED_DOWN_RIGHT:
                return new Ship(position, length, type);
            default:
                throw new IllegalArgumentException("Unsupported type was entered to factory:" + type);
        }

        //return null;
    }

    public enum GamePieceType {
        MINE("Mine"),
        REGULAR_SHIP_COLUMN("Column"),
        REGULAR_SHIP_ROW("Row"),
        L_SHAPED_RIGHT_DOWN("Right down"),
        L_SHAPED_RIGHT_UP("Right up"),
        L_SHAPED_UP_RIGHT("Up right"),
        L_SHAPED_DOWN_RIGHT("Down right");
        String name;

        GamePieceType(String name) {
            this.name = name;
        }
    }
}
