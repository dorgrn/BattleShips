package BattleShipsEngine.engine;

import generated.BattleShipGame;
import generated.BattleShipGame.Boards;
import generated.BattleShipGame.ShipTypes.ShipType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameConfig implements Serializable {
    private static final int MIN_BOARD_LENGTH = 5;
    private static final int MAX_BOARD_LENGTH = 20;
    private static final int AMOUNT_OF_PLAYERS = 2;
    private static final String fileExtension = ".xml";
    private static final String XML_START_VALID = "<?xml";
    private BattleShipGame generatedGame;
    private static final Logger LOGGER = Logger.getLogger(GameConfig.class.getName());


    public Game initiateGameFromGenerated() throws ConfigException {
        if (generatedGame == null) {
            throw new ConfigException("No com.BattleShips.Ex02.generated game");
        }

        // check size
        if (generatedGame.getBoardSize() < MIN_BOARD_LENGTH || generatedGame.getBoardSize() > MAX_BOARD_LENGTH) {
            throw new ConfigException("Board size isn't legal");
        }

        Game game = new Game(generatedGame.getBoardSize());
        insertGamePieces(game);

        return game;
    }

    private void insertGamePieces(Game game) throws ConfigException {
        List<ShipType> shipTypes = generatedGame.getShipTypes().getShipType();
        List<Boards.Board> boards = generatedGame.getBoards().getBoard();

        if (boards.size() != AMOUNT_OF_PLAYERS) {
            throw new ConfigException("Amount of boards doesn't fit players!");
        }

        Player currentPlayer = game.getPlayer1();
        // for each one of the boards
        for ( Boards.Board board : boards ) {

            // hash of each ship type and its amount as in file
            Hashtable<ShipType, Integer> typesAmount = getShipTypesAmount(shipTypes);
            currentPlayer.setShipsAmountByType(fromShipTypeToInt(typesAmount));

            // for each ship in board
            for ( Boards.Board.Ship ship : board.getShip() ) {
                insertShipToPlayer(ship, currentPlayer, shipTypes, typesAmount);
            }

            checkAllTypesZero(typesAmount); // check all types were zeroed (shipType amount == ship types in file)
            updatePlayerMines(currentPlayer, generatedGame.getMine());//
            currentPlayer = game.getOtherPlayer(currentPlayer); // switch players
        }
    }

    private Hashtable<ShipType, Integer> fromShipTypeToInt(Hashtable<ShipType, Integer> original) {
        if (original == null) {
            return null;
        }
        Hashtable<ShipType, Integer> result = new Hashtable<>(original.size());
        Set<ShipType> keys = original.keySet();

        for ( ShipType key : keys ) {
            result.put(key, original.get(key));
        }

        return result;
    }

    private void updatePlayerMines(Player player, BattleShipGame.Mine mine) throws ConfigException {
        if (mine == null) {
            player.setMinesToPlace(0);
            return;
        } else if (mine.getAmount() < 0) {
            throw new ConfigException("Mines amount can't be negative.");
        }

        player.setMinesToPlace(mine.getAmount());
    }

    private void insertShipToPlayer(Boards.Board.Ship ship, Player player, List<ShipType> shipTypes,
                                    Hashtable<ShipType, Integer> typesAmount) throws ConfigException {
        GameBoard gameBoard = player.getPrimaryGrid();

        ShipType shipType = findShipInShipTypes(ship, shipTypes); // get ship type
        checkShipType(shipType, typesAmount); // check validity of type
        checkIndexLegality(ship, shipType.getLength(), gameBoard); // check indices of each type
        addIndicesToBoard(ship, shipType.getLength(), gameBoard); // add given indices to board
        Point shipPosition = generatedPointToBoardPoint(new Point(ship.getPosition().getX(), ship.getPosition().getY())); // insert ship to board.
        // -1 because (x,y) in generated translates to (x-1,y-1) in gameboard
        Ship toInsert =
                (Ship) NavalFactory.getGamePiece(getSubTypeFromShip(ship), shipPosition, shipType.getLength());
        toInsert.addAdditionalDetails(shipType, shipType.getScore());
        player.addShip(toInsert);
    }

    private void checkAllTypesZero(Hashtable<ShipType, Integer> shipTypesHash) throws ConfigException {
        for ( Map.Entry<ShipType, Integer> entry : shipTypesHash.entrySet() ) {
            if (entry.getValue() != 0) {
                throw new ConfigException("Not enough ships of type: \"" + entry.getKey().getId() + "\" in board.");
            }
        }
    }

    private Hashtable<ShipType, Integer> getShipTypesAmount(List<ShipType> list) throws ConfigException {
        if (list == null) {
            throw new ConfigException("No types of ships defined.");
        }
        Hashtable<ShipType, Integer> result = new Hashtable<>();

        for ( ShipType shipType : list ) {
            result.put(shipType, shipType.getAmount());
        }


        // check that all ship types are unique
        if (result.size() != list.size()) {
            throw new ConfigException("Ship types defined are not unique!");
        }

        return result;
    }

    private void addIndicesToBoard(Boards.Board.Ship ship, int shipLength, GameBoard gameBoard) {
        NavalFactory.GamePieceType type = NavalFactory.getPieceTypeFromString(ship.getDirection());


        Ship toInsert = (Ship) NavalFactory.getGamePiece(type,
                generatedPointToBoardPoint(ship.getPosition().getX(), ship.getPosition().getY()), shipLength);

        for ( Point point : toInsert.getBody().keySet() ) {
            gameBoard.setPointInBoard(point, GameBoard.SHIP_SYMBOL);
        }
    }

    private Point generatedPointToBoardPoint(final Point generatedPoint) {
        if (generatedPoint == null) {
            return null;
        }
        return new Point(generatedPoint.getX() - 1, generatedPoint.getY() - 1);
    }

    private Point generatedPointToBoardPoint(int x, int y) {
        return generatedPointToBoardPoint(new Point(x, y));
    }

    private void checkShipType(ShipType shipType, Hashtable<ShipType, Integer> shipTypesHash) throws ConfigException {
        if (shipType == null) {
            throw new ConfigException("Ship type not found.");
        }

        int amountOfShips = shipTypesHash.get(shipType);
        if (amountOfShips == 0) {
            throw new ConfigException("Too many ships in board");
        }

        shipTypesHash.put(shipType, amountOfShips - 1); // subtract one
    }


    private void checkBodyInBoard(Point position, Ship.Direction direction, int shipLength, GameBoard gameBoard) throws ConfigException {
        int x = position.getX();
        int y = position.getY();
        int boardLength = gameBoard.length;

        // delta = the advancement in units in relation to board
        // for example deltaX = -1 means we take 1 step left in board for each point
        int deltaY = 0;
        int deltaX = 0;
        int height = 0;
        int width = 0;

        switch (direction) {
            case LEFT:
                deltaY = -1;
                height = 1;
                width = shipLength;
                break;
            case RIGHT:
                deltaY = 1;
                height = 1;
                width = shipLength;
                break;
            case DOWN:
                deltaX = 1;
                height = shipLength;
                width = 1;
                break;
            case UP:
                deltaX = -1;
                height = shipLength;
                width = 1;
                break;
        }

        if (x + (shipLength - 1) * deltaX >= boardLength || x + (shipLength - 1) * deltaX < 0
                || y + (shipLength - 1) * deltaY < 0 || y + (shipLength - 1) * deltaY >= boardLength) {
            throw new ConfigException("Ships indexes exceed board.");
        }

        if (!GameBoard.isRectangleEmpty(position, height, width, direction, gameBoard)) {
            throw new ConfigException(String.format(
                    "Ships are too close together in %s", position));
        }
    }

    private void checkIndexLegality(Boards.Board.Ship ship, int shipLength, GameBoard gameBoard) throws ConfigException {
        int x = ship.getPosition().getX() - 1; // (1,A) represnets (0,0) on board
        int y = ship.getPosition().getY() - 1;

        if (!GameBoard.isIndexInBoard(x, y, gameBoard.length)) {
            throw new ConfigException("Illegal position of ship on board.");
        }

        NavalFactory.GamePieceType type = NavalFactory.getPieceTypeFromString(ship.getDirection());

        Point position = new Point(x, y);

        LOGGER.log(Level.FINER, "dealing with type: " + type.toString());

        switch (type) {
            case REGULAR_SHIP_COLUMN:
                checkBodyInBoard(position, Ship.Direction.DOWN, shipLength, gameBoard);
                break;
            case REGULAR_SHIP_ROW:
                checkBodyInBoard(position, Ship.Direction.RIGHT, shipLength, gameBoard);
                break;
            case L_SHAPED_RIGHT_DOWN:
                checkBodyInBoard(position, Ship.Direction.LEFT, shipLength, gameBoard);
                checkBodyInBoard(position, Ship.Direction.DOWN, shipLength, gameBoard);
                break;
            case L_SHAPED_RIGHT_UP:
                checkBodyInBoard(position, Ship.Direction.LEFT, shipLength, gameBoard);
                checkBodyInBoard(position, Ship.Direction.UP, shipLength, gameBoard);
                break;
            case L_SHAPED_UP_RIGHT:
                checkBodyInBoard(position, Ship.Direction.DOWN, shipLength, gameBoard);
                checkBodyInBoard(position, Ship.Direction.RIGHT, shipLength, gameBoard);
                break;
            case L_SHAPED_DOWN_RIGHT:
                checkBodyInBoard(position, Ship.Direction.UP, shipLength, gameBoard);
                checkBodyInBoard(position, Ship.Direction.RIGHT, shipLength, gameBoard);
                break;
        }
    }

    private NavalFactory.GamePieceType getSubTypeFromShip(Boards.Board.Ship ship) {
        return NavalFactory.getPieceTypeFromString(ship.getDirection());
    }


    private ShipType findShipInShipTypes(Boards.Board.Ship ship, List<ShipType> shipTypes) {
        for ( ShipType shipType : shipTypes ) {
            if (ship.getShipTypeId().equals(shipType.getId())) { // if the same id is found
                return shipType;
            }
        }

        return null;
    }

    public void load(File file) throws IOException, ConfigException, JAXBException {
        checkValidXMLFormat(file);
        generateGame(file);
    }

    public void load(String filePath) throws JAXBException, ConfigException, IOException {
        //check if file name has suffix xml
        if (!filePath.endsWith(fileExtension)) {
            filePath += fileExtension;
        }

        File file = new File(filePath);


        checkValidXMLFormat(file);

        generateGame(file);
    }

    private void generateGame(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BattleShipGame.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        generatedGame = (BattleShipGame) jaxbUnmarshaller.unmarshal(file);
    }

    private void checkValidXMLFormat(File file) throws ConfigException, IOException {
        if (file == null) {
            throw new ConfigException("No file entered.");
        }

        if (!file.exists()) {
            throw new ConfigException("Filename " + file.getAbsolutePath() + " does not exist.");
        }

        // check first line of xml file
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String fileStart = bufferedReader.readLine().substring(0, XML_START_VALID.length());
        if (!fileStart.equals(XML_START_VALID)) {
            throw new ConfigException("Filename " + file.getAbsolutePath() + " is not a valid XML file.");
        }
    }

    public BattleShipGame getGame() {
        return generatedGame;
    }
}
