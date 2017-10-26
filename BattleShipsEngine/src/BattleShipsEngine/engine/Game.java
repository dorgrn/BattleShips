package BattleShipsEngine.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    public static final int PLAYERS_AMOUNT = 2;
    private final long startTime;
    private int boardSize = 0;
    private int turnsAmount = 0;

    private Player currentPlayer;

    private List<Player> players;

    private Player player1;
    private Player player2;

    public Game(int boardSize) {
        startTime = System.currentTimeMillis();
        this.boardSize = boardSize;
        players = new ArrayList<>(PLAYERS_AMOUNT);

        player1 = new Player(Player.Type.PLAYER_ONE, boardSize, this);
        player1.addTurn();
        player2 = new Player(Player.Type.PLAYER_TWO, boardSize, this);
        currentPlayer = player1;

        players.add(player1);
        players.add(player2);
    }

    // if game has no winner, returns null
    public Player.Type getWinner() {
        Player.Type result = null;


        for ( Player player : players ) {
            Player enemy = getOtherPlayer(player);

            if (enemy.hasPlayerLost() || player.getHasWon()) {
                result = player.getPlayerType();
                break;
            }
        }

        return result;
    }


    public HitStatus makeTurn(Point pointToHit, long turnStartTime) {
        HitStatus result = null;

        Player enemy = getOtherPlayer(currentPlayer);
        GameBoard enemyPrimaryGrid = enemy.getPrimaryGrid();


        char hitOnEnemyBoard = enemyPrimaryGrid.getBoard()[pointToHit.getX()][pointToHit.getY()];
        char hitOnTrackingBoard = currentPlayer.getTrackingGrid().getBoard()[pointToHit.getX()][pointToHit.getY()];

        if (hitOnTrackingBoard != GameBoard.EMPTY_SYMBOL) {
            return HitStatus.ALREADY_TRIED_POINT;
        }
        turnsAmount++; // else


        // check what is in enemy's primary grid in hit position
        switch (hitOnEnemyBoard) {
            case GameBoard.EMPTY_SYMBOL:
                handlePlayerMissed(pointToHit, currentPlayer);
                result = HitStatus.MISS;
                enemyPrimaryGrid.setPointInBoard(pointToHit, GameBoard.MISS_SYMBOL);
                break;
            case GameBoard.SHIP_SYMBOL:
                handleEnemyWasHit(enemy, pointToHit);
                result = handlePlayerShipWasHit(pointToHit, enemy);
                break;
            case GameBoard.MINE_SYMBOL:
                result = handlePlayerHitMine(pointToHit, currentPlayer);
                break;
        }
        currentPlayer.endTurn(System.currentTimeMillis() - turnStartTime);

        return result;
    }

    public Player getOtherPlayer(Player player) {
        if (player.equals(player1)) {
            return player2;
        } else {
            return player1;
        }
    }

    public void handleEnemyWasHit(Player enemy, Point hitPoint) {
        currentPlayer.increaseHitAmount();
        currentPlayer.getTrackingGrid().setPointInBoard(hitPoint, GameBoard.HIT_SYMBOL);
    }

    public void swapCurrentPlayer() {
        currentPlayer.endTurn(getTimeElapsedMilliSecs());
        currentPlayer = getOtherPlayer(currentPlayer);
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTimeElapsedMilliSecs() {
        return System.currentTimeMillis() - startTime;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getTurnsAmount() {
        return turnsAmount;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public boolean canMineBePlaced(Point toPlace) {
        GameBoard primaryGrid = currentPlayer.getPrimaryGrid();

        return GameBoard.isRectangleEmpty(toPlace, 1, 1, Ship.Direction.LEFT, primaryGrid);
    }

    public void placeMine(Point toPlace) {
        GameBoard primaryGrid = currentPlayer.getPrimaryGrid();

        if (!canMineBePlaced(toPlace)) {
            throw new UnsupportedOperationException("Couldn't place mine at: " + toPlace);
            //return false;
        }

        Mine mine = (Mine) NavalFactory.getGamePiece(NavalFactory.GamePieceType.MINE, toPlace, 1);

        primaryGrid.setPointInBoard(toPlace, GameBoard.MINE_SYMBOL);
        currentPlayer.addMine(mine);
        currentPlayer.setMinesToPlace(currentPlayer.getAmountOfMinesToPlace() - 1);
    }


    public Game.HitStatus handlePlayerHitMine(Point hitPoint, Player player) {
        HitStatus result = null;
        player.increaseHitAmount(); // (anyway count as a hit)

        char pointOnPlayerBoard = player.getPrimaryGrid().getPointInBoard(hitPoint);

        switch (pointOnPlayerBoard) {
            case GameBoard.EMPTY_SYMBOL:
                modifyBoardsAfterMineHit(player, hitPoint, GameBoard.MISS_SYMBOL);
                result = HitStatus.MINE_MISSED;
                break;
            case GameBoard.SHIP_SYMBOL:
                modifyBoardsAfterMineHit(player, hitPoint, GameBoard.HIT_SYMBOL);
                result = handlePlayerShipWasHit(hitPoint, player);
                break;
            case GameBoard.MINE_SYMBOL:
                modifyBoardsAfterMineHit(player, hitPoint, GameBoard.MISS_SYMBOL);
                player.removeMine(hitPoint);
                result = HitStatus.MINE_HIT_MINE;
                break;
        }

        return result;
    }

    private void modifyBoardsAfterMineHit(Player player, Point hitPoint, char symbolInBoard) {
        Player enemy = getOtherPlayer(player);
        player.getTrackingGrid().setPointInBoard(hitPoint, GameBoard.MISS_SYMBOL);
        enemy.getPrimaryGrid().setPointInBoard(hitPoint, GameBoard.MISS_SYMBOL);
        swapCurrentPlayer();
        HitStatus hitStatus = generateOppositeAttack(hitPoint);
        enemy.removeMine(hitPoint);
    }

    public HitStatus generateOppositeAttack(Point pointToHit) {
        HitStatus result = null;

        Player enemy = getOtherPlayer(currentPlayer);
        GameBoard enemyPrimaryGrid = enemy.getPrimaryGrid();

        char hitOnEnemyBoard = enemyPrimaryGrid.getBoard()[pointToHit.getX()][pointToHit.getY()];
        char hitOnTrackingBoard = currentPlayer.getTrackingGrid().getBoard()[pointToHit.getX()][pointToHit.getY()];

        if (hitOnTrackingBoard != GameBoard.EMPTY_SYMBOL) {
            return HitStatus.ALREADY_TRIED_POINT;
        }

        // check what is in enemy's primary grid in hit position
        switch (hitOnEnemyBoard) {
            case GameBoard.EMPTY_SYMBOL:
                handlePlayerMissed(pointToHit, currentPlayer);
                result = HitStatus.MISS;
                enemyPrimaryGrid.setPointInBoard(pointToHit, GameBoard.MISS_SYMBOL);
                break;
            case GameBoard.SHIP_SYMBOL:
                handleEnemyWasHit(enemy, pointToHit);
                result = handlePlayerShipWasHit(pointToHit, enemy);
                break;
            case GameBoard.MINE_SYMBOL:
                result = handlePlayerHitMine(pointToHit, currentPlayer);
                break;
        }

        return result;
    }

    public HitStatus handlePlayerShipWasHit(Point hitPoint, Player player) {
        HitStatus status = HitStatus.HIT;

        // update ships
        Ship ship = player.findShipInPoint(hitPoint);
        ship.takeDamage(hitPoint);
        if (!ship.isActive()) {
            player.removeShip(ship);
            status = HitStatus.HIT_AND_SINK;

            // add ship's score to player
            Player enemy = getOtherPlayer(player);
            enemy.addScore(ship.getScore());

            // check if this player lost
            if (player.getShipsAmount() == 0) {
                getOtherPlayer(player).setHasWon(true);
            }
        }

        // update primaryGrid
        player.getPrimaryGrid().setPointInBoard(hitPoint, GameBoard.HIT_SYMBOL);

        return status;
    }


    public void handlePlayerMissed(Point missPoint, Player player) {
        player.setMissAmount(player.getMissAmount() + 1);
        player.addTurn();
        player.getTrackingGrid().setPointInBoard(missPoint, GameBoard.MISS_SYMBOL);
    }


    public Player setPlayerByType(Player player, Player.Type playerType) {
        if (player1.getPlayerType() == playerType) {
            player1 = Player.shallowClone(player);
            return player1;
        } else {
            player2 = Player.shallowClone(player);
            return player2;
        }
    }


    public Player getPlayerByType(Player.Type playerType) {
        if (playerType == Player.Type.PLAYER_ONE) {
            return player1;
        } else {
            return player2;
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public enum HitStatus {
        HIT(GameBoard.HIT_SYMBOL),
        MISS(GameBoard.EMPTY_SYMBOL),
        HIT_AND_SINK(GameBoard.HIT_SYMBOL),
        MINE_HIT_SHIP(GameBoard.MINE_SYMBOL),
        MINE_MISSED(GameBoard.MINE_SYMBOL),
        MINE_HIT_MINE(GameBoard.MINE_SYMBOL),
        ALREADY_TRIED_POINT('A');
        // Pyramid :)
        char hitSymbol;

        HitStatus(char hitSymbol) {
            this.hitSymbol = hitSymbol;
        }
    }
}