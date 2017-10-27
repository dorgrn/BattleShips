const INTERVAL_LENGTH = 1000;

const HIT_SYMBOL = 'H';
const MISS_SYMBOL = 'L';
const EMPTY_SYMBOL = '-';
const SHIP_SYMBOL = 'S';
const MINE_SYMBOL = 'M';

var gameActive = false;
var gameStarted = false;
var intervalRefreshLists = null;

$(window).on('load', function () {
    startGame();

    intervalRefreshLists = setInterval(function () {
        // add any updates here...
        ajaxGetMessagesFromServer();
        ajaxUpdateCurrentGame();
    }, INTERVAL_LENGTH);
});

function startGame() {
    ajaxGetCurrentUserName();
    ajaxGetCurrentUserType();
    ajaxGetAndInitCurrentGame();
    gameStarted = true;
}

function ajaxGetCurrentUserName() {
    $.ajax({
        url: '/gamesRoom/currentUser',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (users) {
            $.each(users || [], function (index, username) {
                $("#myPlayerName").text(username);
            });
        }
    });
}

function ajaxGetCurrentUserType() {
    $.ajax({
        url: '/gamesRoom/currentUserType',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (users) {
            $.each(users || [], function (index, userType) {
                $("#myPlayerType").text(userType);
                sessionStorage.setItem("PLAYER_TYPE", userType);
            });
        }
    });
}

function ajaxGetCurrentGameAndUpdateBoards(btn) {
    $.ajax({
        url: '/game/currentGameRecord',
        type: 'GET',
        cache: false,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function () {
        },
        success: function (game) {
            generateAttackAndUpdateScreen(game, btn);
        }
    });
}

function ajaxUpdateCurrentGame() {
    $.ajax({
        url: '/game/currentGameRecord',
        type: 'GET',
        cache: false,
        contentType: 'application/json; charset=utf-8',
        success: function (gameRecord) {
            initializeGame(gameRecord);
        }
    });
}

function isItMyTurn(gameRecord) {
    var currentPlayer = gameRecord.game.currentPlayer.playerType;
    var myPlayer = document.getElementById("myPlayerName").innerHTML === gameRecord.creator["username"] ? "PLAYER_ONE" : "PLAYER_TWO";
    return (currentPlayer === myPlayer);
}

function getCurrentCharFromBoard(boardType, gameRecord, i, j) {
    if (boardType === "trackingGrid") {
        if (document.getElementById("myPlayerName").innerHTML === gameRecord.creator["username"]) { // if user is player1
            return gameRecord.game.player1.trackingGrid.board[i][j];
        }
        else { // If user is player2
            return gameRecord.game.player2.trackingGrid.board[i][j];
        }
    }
    else { // If primaryGrid
        if (document.getElementById("myPlayerName").innerHTML === gameRecord.creator["username"]) { // if user is player1
            return gameRecord.game.player1.primaryGrid.board[i][j];
        }
        else { // If user is player2
            return gameRecord.game.player2.primaryGrid.board[i][j];
        }
    }
}

function setClassFieldByCurrentCharacter(currentChar) {
    switch (currentChar) {
        case SHIP_SYMBOL:
            return "shipImg";
        case EMPTY_SYMBOL:
            return "emptyImg";
        case HIT_SYMBOL:
            return "hitImg";
        case MISS_SYMBOL:
            return "missImg";
        case MINE_SYMBOL:
            return "mineImg";
    }
}

function buttonOnClick(btn) {
    ajaxGetCurrentGameAndUpdateBoards(btn); // Calls 'generateAttackAndUpdateScreen' in success
}

function generateAttackAndUpdateScreen(gameRecord, btn) {
    ajaxMakeTurn(gameRecord, btn.id);
}

function ajaxMakeTurn(gameRecord, buttonID) {
    $.ajax({
        type: 'POST',
        url: MAKE_TURN_URI,
        dataType: 'html',
        data: { // should match Constants
            "GAME_NAME": gameRecord.gameName,
            "BUTTON_ID": buttonID
        },
        success: function () {
            ajaxUpdateCurrentGame();
        }
    });
}

function checkForWinner() {
    ajaxFindGameWinner();
}

function ajaxRetireFromGame() {
    $.ajax({
        type: 'GET',
        url: '/game/retireFromGame',
        dataType: 'html',
        success: function () {

        }
    });
}

function updateDataOnScreen(gameRecord) {
    updateGameActiveState(gameRecord.participants.length);
    updateStatistics(gameRecord);
    updateGeneralDataLine(gameRecord);
    updateOpponentName(gameRecord.participants);
    updateWatchersTable(gameRecord.watchers);
    updateCommentsLine(gameRecord.participants.length);
    updateShipsData(gameRecord.game);
    updateMineButton(gameRecord, gameRecord.game.currentPlayer.amountOfMinesToPlace, gameRecord.participants.length);
    if (gameStarted){
        checkForWinner();
    }
}

function updateGameActiveState(amountOfParticipants) {
    gameActive = (amountOfParticipants === 2);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev, btn) {
    ev.preventDefault();
    ajaxGetCurrentGameAndPlaceMine(btn);
}

function ajaxGetCurrentGameAndPlaceMine(btn) {
    $.ajax({
        url: '/game/currentGameRecord',
        type: 'GET',
        cache: false,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function () {
        },
        success: function (game) {
            ajaxPlaceMine(game, btn.id);
        }
    });
}

function ajaxPlaceMine(gameRecord, buttonID) {
    $.ajax({
        type: 'POST',
        url: '/gamesRoom/placeMine',
        dataType: 'html',
        data: { // should match Constants
            "GAME_NAME": gameRecord.gameName,
            "BUTTON_ID": buttonID
        },
        success: function () {
            ajaxUpdateCurrentGame();
        }
    });
}

function showPlayerWon(username) {
    if (!gameActive){
        return;
    }

    if (username === $("#myPlayerName")) {
        alert("Congratulations! " + username + " you Won!");
    }
    else {
        alert("Sorry, you lose.");
    }
}

function ajaxFindGameWinner() {
    $.ajax({
        type: 'GET',
        url: '/game/findWinner',
        cache: false,
        success: function (winnerPlayer) {
            if (winnerPlayer && gameStarted) {
                handleGameOver(winnerPlayer);
            }
        }
    });
}

function retireFromGame() {
    alert("You retired! Returning to games room!");
    ajaxRetireFromGame();
    handleGameOver(null);

}

function ajaxResetGameRecord() {
    $.ajax({
        type: 'GET',
        url: '/game/resetGameRecordServlet',
        success: function () {
            startGame();
            window.location.replace(GAMES_ROOM_URI);
        }
    });
}

function handleGameOver(winnerPlayer) {
    if (!gameStarted){
        return;
    }
    gameActive = false;

    if (winnerPlayer !== null) {
        showPlayerWon(winnerPlayer);
    }

    alert("Press OK to return to the Games Room");
    ajaxResetGameRecord();
}