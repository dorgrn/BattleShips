const INTERVAL_LENGTH = 5000;

const HIT_SYMBOL = 'H';
const MISS_SYMBOL = 'L';
const EMPTY_SYMBOL = '-';
const SHIP_SYMBOL = 'S';
const MINE_SYMBOL = 'M';

var intervalRefreshLists = null;

$(window).on('load', function () {
    startGame();

    intervalRefreshLists = setInterval(function () {
        // add any other updates you want here...
        ajaxUpdateCurrentGame();
    }, INTERVAL_LENGTH);
});

function startGame() {
    ajaxGetCurrentUserName();
    ajaxGetCurrentUserType();
    ajaxGetAndInitCurrentGame();
}

function ajaxGetCurrentUserName() {
    $.ajax({
        url: '/gamesRoom/currentUser',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (users) {
            $.each(users || [], function (index, username) {
                $("#myPlayerName").text(username);
                sessionStorage.setItem("PLAYER_NAME", username);
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


function ajaxGetAndInitCurrentGame() {
    $.ajax({
        url: '/game/currentGameRecord',
        type: 'GET',
        cache: false,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function () {
            // console.log("before");
        },
        success: function (game) {
            //console.log(game);
            initializeGame(game);
        }
    });
}

function ajaxUpdateCurrentGame() {
    $.ajax({
        url: '/game/currentGameRecord',
        type: 'GET',
        cache: false,
        contentType: 'application/json; charset=utf-8',
        beforeSend: function () {
            // console.log("before");
        },
        success: function (gameRecord) {
            updateDataOnScreen(gameRecord);
        }
    });
}

function initializeGame(gameRecord) {
    localStorage.setItem("GAME_JSON", JSON.stringify(gameRecord));
    createBoardsBySize(gameRecord);
    ajaxGetCurrentUserName();
    updateDataOnScreen(gameRecord);
}

function createBoardsBySize(gameRecord) {
    createBattleshipBoard(gameRecord);
    createTrackingBoard(gameRecord);
}

function createBattleshipBoard(gameRecord) {
    var boardSize = gameRecord.boardSize;
    var i, j, currentChar, idField, classField;
    var multiplyConst = 0.02 * (20 - boardSize) + 1;
    var buttonSize = 150 * multiplyConst / boardSize;
    var paddingField = 'padding:' + buttonSize + 'px ';
    var table = "";

    for (i = 0; i < boardSize; i++) {
        table += "<tr>";
        for (j = 0; j < boardSize; j++) {
            idField = i + " " + j;
            currentChar = getCurrentCharFromBoard("primaryGrid", gameRecord, i, j);
            classField = setClassFieldByCurrentCharacter(currentChar);
            table += "<th style='padding:0'" + ">" + "<button " + "id=\u0022" + idField + "\u0022 class=\u0022" + classField +
                "\u0022 style=" + paddingField + " disabled" + "></button>" + "</th>";
            if (j === boardSize - 1) {
                table += "</tr>";
            }
        }
    }
    document.getElementById("battleshipBoard").innerHTML = table;
}

function createTrackingBoard(gameRecord) {
    var boardSize = gameRecord.boardSize;
    var i, j, currentChar, idField, classField;
    var multiplyConst = 0.02 * (20 - boardSize) + 1;
    var buttonSize = 150 * multiplyConst / boardSize;
    var paddingField = 'padding:' + buttonSize + 'px ';
    var table = "";
    for (i = 0; i < boardSize; i++) {
        table += "<tr>";
        for (j = 0; j < boardSize; j++) {
            idField = i + " " + j;
            currentChar = getCurrentCharFromBoard("trackingGrid", gameRecord, i, j);
            classField = setClassFieldByCurrentCharacter(currentChar);
            table += "<th style='padding:0'" + ">" + "<button " + "id=\u0022" + idField + "\u0022 class=\u0022" + classField +
                "\u0022 style=" + paddingField + "></button>" + "</th>";
            if (j === boardSize - 1) {
                table += "</tr>";
            }
        }
    }
    document.getElementById("traceBoard").innerHTML = table;
}

function getCurrentCharFromBoard(boardType, gameRecord, i, j) {
    if (boardType === "trackingGrid") {
        if (gameRecord.game.player1.playerType === document.getElementById("myPlayerType").innerHTML) { // If user is player1
            return gameRecord.game.player1.trackingGrid.board[i][j];
        }
        else { // If user is player2
            return gameRecord.game.player2.trackingGrid.board[i][j];
        }
    }
    else { // If primaryGrid
        if (gameRecord.game.player1.playerType === document.getElementById("myPlayerType").innerHTML) { // If user is player1
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

function updateDataOnScreen(gameRecord) {
    updateStatistics(gameRecord);
    updateGeneralDataLine(gameRecord);
    updateOpponentName(gameRecord.participants);
    updateWatchersTable(gameRecord.watchers);
    updateCommentsLine(gameRecord.participants.length);
    updateShipsData(gameRecord.game);
}

function updateStatistics(gameRecord) {
    var currentGame = gameRecord.game;
    $("#currentPlayer").text(("PLAYER_ONE" === currentGame.currentPlayer.playerType ?
        gameRecord.creator.username : (gameRecord.participants[0]["username"] === gameRecord.creator.username ?
            gameRecord.participants[0]["username"] : gameRecord.participants[1]["username"])) + "'s Turn");
    $("#playerScore").text("Player Score: " + currentGame.currentPlayer.score);
    $("#turnsCompleted").text("Turn Number: " + currentGame.currentPlayer.turnNumber);
    $("#totalHits").text("Total Hits: " + currentGame.currentPlayer.hitAmount);
    $("#totalMisses").text("Total Misses: " + currentGame.currentPlayer.missAmount);
    $("#timePerMove").text("Time Per Move: " + currentGame.currentPlayer.totalTurnTime);
    $("#minesLeft").text("Mines Left: " + currentGame.currentPlayer.amountOfMinesToPlace);
}

function updateGeneralDataLine(gameRecord) {
    var currentGame = gameRecord.game;
    var tab = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";
    var currentTime = new Date().getTime();
    $("#generalData").text(gameRecord.participants[0]["username"] + "'s Score: " +
        currentGame.player2.score + tab +
        (gameRecord.participants.length === 1 ? "" : gameRecord.participants[1]["username"] + "'s Score: " +
            gameRecord.game.player1.score + tab) +
        "Time Passed: " + (currentTime - currentGame.startTime) / 1000 +
        tab + "Total Turns Completed: " + (gameRecord.turnsAmount === undefined ? 0 : gameRecord.turnsAmount));
}

function updateOpponentName(participants) {
    if (participants.length === 1) {
        $("#opponentName").text("No opponent yet");
    }
    else { // 2 participants
        if (document.getElementById("myPlayerName").innerHTML === participants[0]["username"]) {
            $("#opponentName").text(participants[1]["username"]);
        }
        else {
            $("#opponentName").text(participants[0]["username"]);
        }
    }
}

function updateWatchersTable(watchers) {
    var table = "";
    for (var i = 0; i < watchers.length; i++) {
        table += "<tr><th>" + watchers[i]["username"] + "</th></tr>";
    }
    $("#watchersTable").text(table);
}

function updateCommentsLine(numOfParticipants) {
    if (numOfParticipants === 1) {
        $("#commentsLine").text("Waiting for another player...");
    }
    else {
        $("#commentsLine").text("Game starts. Good luck!");
    }
}

function updateShipsData(currentGame) {
    var shipsText = "Player1's Battleships Left Details: ";
    for (var i = 0; i < currentGame.player1.ships.length; i++) {
        shipsText += currentGame.player1.ships[i].shipType.category + ", length: " + currentGame.player1.ships[i].shipType.length +
            ", score: " + currentGame.player1.ships[i].shipType.score + " ||| ";
    }
    $(".myBattleships").text(shipsText);

    shipsText = "Player2's Battleships Left Details: ";
    for (i = 0; i < currentGame.player2.ships.length; i++) {
        shipsText += currentGame.player2.ships[i].shipType.category + ", length: " + currentGame.player2.ships[i].shipType.length +
            ", score: " + currentGame.player2.ships[i].shipType.score + " ||| ";
    }
    $(".opponentBattleships").text(shipsText);
}

function backToGamesRoom() {
    $.ajax({
        type: 'GET',
        url: GAMES_ROOM_URI,
        success: function () {
            window.location.replace(GAMES_ROOM_URI);
        }
    });
}

function logout() {
    $.ajax({
        type: 'GET',
        url: SIGN_UP_URI,
        data: {
            "CALLER_URI": GAME_URI
        },
        success: function () {
            window.location.replace(SIGN_UP_URI);
        }
    });
}