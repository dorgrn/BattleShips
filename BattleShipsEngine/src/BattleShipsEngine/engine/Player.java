package BattleShipsEngine.engine;



import java.io.Serializable;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static generated.BattleShipGame.*;

public class Player implements Serializable {
    //private Game game;
    private List<Mine> mines = new LinkedList<>();
    private LinkedList<Ship> ships = new LinkedList<>();
    private int amountOfMinesToPlace;
    private Hashtable<ShipTypes.ShipType, Integer> shipsAmountByType;

    private GameBoard primaryGrid;
    private GameBoard trackingGrid;
    private Type playerType;
    private boolean hasWon = false;
    private int score = 0;
    private int turnNumber = 0;
    private int hitAmount = 0;
    private int missAmount = 0;
    private long totalTurnTime = 0;

    public Player(Type playerType) {
        this.playerType = playerType;
    }

    public Player(Type playerType, int boardSize, Game game) {
        this.playerType = playerType;
        primaryGrid = new GameBoard(boardSize);
        trackingGrid = new GameBoard(boardSize);
        shipsAmountByType = new Hashtable<>();

        initTrackingBoard();
        //this.game = game;
    }

    private static Type getPlayerTypeById(int id) {
        if (id == Type.PLAYER_ONE.playerId) {
            return Type.PLAYER_ONE;
        } else {
            return Type.PLAYER_TWO;
        }
    }

    public static Type getOtherPlayerById(int idChoice) {
        if (getPlayerTypeById(idChoice).playerId == Type.PLAYER_ONE.playerId) {
            return Type.PLAYER_TWO;
        } else {
            return Type.PLAYER_ONE;
        }
    }

    static Player shallowClone(Player player) {
        Player result = new Player(player.playerType);
        result.ships = player.ships;
        result.mines = player.mines;
        //result.game = player.game;
        result.primaryGrid = player.primaryGrid;
        result.playerType = player.playerType;
        result.hasWon = player.hasWon;
        result.turnNumber = player.turnNumber;
        result.hitAmount = player.hitAmount;
        result.missAmount = player.missAmount;
        result.totalTurnTime = player.totalTurnTime;
        return result;
    }

    void removeShip(Ship ship) {
        for ( ShipTypes.ShipType type : shipsAmountByType.keySet() ) {
            if (Objects.equals(type.getId(), ship.getShipType().getId()) && type.getAmount() == 1) {
                shipsAmountByType.remove(type);
                break;
            }
        }

        ships.remove(ship);
    }

    public boolean getHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean set) {
        hasWon = set;
    }

    public int getShipsAmount() {
        return ships.size();
    }

    void addShip(Ship ship) {
        if (ship == null) {
            return;
        }

        int amount = shipsAmountByType.getOrDefault(ship, 0);

        shipsAmountByType.put(ship.getId(), amount + 1);

        ships.add(ship);
    }

    public Ship findShipInPoint(Point point) {
        for ( Ship ship : ships ) {
            if (ship.hasPoint(point)) {
                return ship;
            }
        }

        return null;
    }

    void endTurn(long turnTime) {
        ++turnNumber;
        totalTurnTime += turnTime;
    }

    public void addTurn() {
        ++turnNumber;
    }

    public int getHitAmount() {
        return hitAmount;
    }

    public void setHitAmount(int hitAmount) {
        this.hitAmount = hitAmount;
    }

    public boolean hasPlayerLost() {
        return ships.size() == 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public int getMissAmount() {
        return missAmount;
    }

    public void setMissAmount(int missAmount) {
        this.missAmount = missAmount;
    }

    public long getTotalTurnTime() {
        return totalTurnTime;
    }

    public Type getPlayerType() {
        return playerType;
    }

    public GameBoard getPrimaryGrid() {
        return primaryGrid;
    }

    public GameBoard getTrackingGrid() {
        return trackingGrid;
    }

    private long getAverageTurnTimeInMillisecs() {
        if (turnNumber == 0) {
            return 0;
        }
        return totalTurnTime / turnNumber;
    }

    public boolean hasWon() {
        return hasWon;
    }

    public int getMinesAmount() {
        return mines.size();
    }

    public int getAmountOfMinesToPlace() {
        return amountOfMinesToPlace;
    }

    private void initTrackingBoard() {
        this.trackingGrid.initializeBoard();
    }

    public void addMine(Mine mine) {
        mines.add(mine);
    }

    public String getName() {
        return this.playerType.name;
    }

    public void increaseHitAmount() {
        ++hitAmount;
    }

    public void removeMine(Point hitPoint) {
        List<Mine> tempMines = mines.stream()
                .filter(mine -> mine.getPosition().equals(hitPoint))
                .limit(1)
                .collect(Collectors.toList());
        if (tempMines != null) {
            mines.remove(tempMines.get(0));
        }
    }

    public List<Ship> getShips() {
        return ships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        return playerType == player.playerType;
    }

    @Override
    public int hashCode() {
        return playerType.hashCode();
    }

    public String getTimeElapsedString() {
        long milliseconds = getAverageTurnTimeInMillisecs();
        return String.format("%d minutes, %d seconds.",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }

    public List<Mine> getMines() {
        return mines;
    }

    public int getMinesToPlace() {
        return amountOfMinesToPlace;
    }

    public void setMinesToPlace(int minesToPlace) {
        this.amountOfMinesToPlace = minesToPlace;
    }

    public Hashtable<ShipTypes.ShipType, Integer> getShipAmountByType() {
        return shipsAmountByType;
    }

    public void setShipsAmountByType(Hashtable<ShipTypes.ShipType, Integer> shipsAmountByType) {
        this.shipsAmountByType = shipsAmountByType;
    }

    public void addScore(int score) {
        this.score += score;
    }


    public enum Type {
        PLAYER_ONE("One", 1),
        PLAYER_TWO("Two", 2);

        private String name;
        private int playerId;

        Type(String name, int playerId) {
            this.name = name;
            this.playerId = playerId;
        }


        public String getName() {
            return name;
        }

        public int getPlayerId() {
            return playerId;
        }
    }

    public static Player.Type getOtherPlayerType(Player.Type playerType){
        Player.Type result = null;
        if (playerType == null){
            return null;
        }

        switch (playerType){

            case PLAYER_ONE:
                result =  Player.Type.PLAYER_TWO;
                break;
            case PLAYER_TWO:
                result =  Player.Type.PLAYER_ONE;
                break;
        }
        return result;
    }

}
