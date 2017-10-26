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
            initializeGame(game);
        }
    });
}

function stopListsRefresh() {
    if (intervalRefreshLists !== null) {
        clearInterval(intervalRefreshLists);
    }
}

function initializeGame(gameRecord) {
    createBoardsBySize(gameRecord);
    ajaxGetCurrentUserName();
    updateDataOnScreen(gameRecord);
}

function createBoardsBySize(gameRecord) {
    createPrimaryGrid(gameRecord);
    createTrackingGrid(gameRecord);
}

function createPrimaryGrid(gameRecord) {
    var boardSize = gameRecord.boardSize;
    var i, j, currentChar, idField, classField;
    var multiplyConst = 0.02 * (20 - boardSize) + 1;
    var buttonSize = 150 * multiplyConst / boardSize;
    var paddingField = 'padding:' + buttonSize + 'px ';
    var table = "";

    for (i = 0; i < boardSize; i++) {
        table += "<tr>";
        for (j = 0; j < boardSize; j++) {
            idField = i + " " + j + " trackingGrid " +
                (document.getElementById("myPlayerName").innerHTML === gameRecord.creator["username"] ? "player1" : "player2");
            currentChar = getCurrentCharFromBoard("primaryGrid", gameRecord, i, j);
            classField = setClassFieldByCurrentCharacter(currentChar);
            table +=
                "<th style='padding:0'" + ">" + "<button " + "id=\u0022" + idField +
                "\u0022 ondrop='drop(event,this)'" + " ondragover='allowDrop(event)'" +
                "\u0022 class=\u0022" + classField + "\u0022 style=" + paddingField + " disabled" + "></button>" + "</th>";
            if (j === boardSize - 1) {
                table += "</tr>";
            }
        }
    }
    document.getElementById("battleshipBoard").innerHTML = table;
}

function createTrackingGrid(gameRecord) {
    var boardSize = gameRecord.boardSize;
    var i, j, currentChar, idField, classField;
    var multiplyConst = 0.02 * (20 - boardSize) + 1;
    var buttonSize = 150 * multiplyConst / boardSize;
    var paddingField = 'padding:' + buttonSize + 'px ';
    var table = "";
    for (i = 0; i < boardSize; i++) {
        table += "<tr>";
        for (j = 0; j < boardSize; j++) {
            idField = i + " " + j + " trackingGrid " +
                (document.getElementById("myPlayerName").innerHTML === gameRecord.creator["username"] ? "player1" : "player2");
            currentChar = getCurrentCharFromBoard("trackingGrid", gameRecord, i, j);
            classField = setClassFieldByCurrentCharacter(currentChar);
            table += addTrackingBoardButton(classField, paddingField, gameRecord, idField);
            if (j === boardSize - 1) {
                table += "</tr>";
            }
        }
    }
    document.getElementById("traceBoard").innerHTML = table;
}

function addTrackingBoardButton(classField, paddingField, gameRecord, idField) {
    if (classField === "emptyImg" && gameRecord.participants.length === 2 && isItMyTurn(gameRecord)) {
        return ("<th style='padding:0'" + ">" + "<button " + "type=\u0022button\u0022 " + "onclick=\u0022buttonOnClick(this)" +
            "\u0022 id=\u0022" + idField + "\u0022 class=\u0022" + classField + "\u0022 style=" + paddingField +
            "></button>" + "</th>");
    } else {
        return ("<th style='padding:0'" + ">" + "<button " + "type=\u0022button\u0022 " + "onclick=\u0022buttonOnClick(this)" +
            "\u0022 id=\u0022" + idField + "\u0022 class=\u0022" + classField + "\u0022 style=" + paddingField +
            " disabled" + "></button>" + "</th>");
    }
}


function updateStatistics(gameRecord) {
    var currentGame = gameRecord.game;
    var currentPlayerTotalTurns = currentGame.currentPlayer.hitAmount + currentGame.currentPlayer.missAmount;
    $("#currentPlayer").text("PLAYER_ONE" === currentGame.currentPlayer.playerType ?
        (gameRecord.creator.username + "'s Turn") :
        (gameRecord.participants[0]["username"] === gameRecord.creator.username ?
            (gameRecord.participants[1]["username"]) : (gameRecord.participants[0]["username"])) + "'s Turn");
    $("#playerScore").text("Player Score: " + currentGame.currentPlayer.score);
    $("#turnsCompleted").text("Total Turns: " + currentPlayerTotalTurns);
    $("#totalHits").text("Total Hits: " + currentGame.currentPlayer.hitAmount);
    $("#totalMisses").text("Total Misses: " + currentGame.currentPlayer.missAmount);
    var averageTimePerMove = currentGame.currentPlayer.totalTurnTime /
        currentPlayerTotalTurns / 1000;
    averageTimePerMove = (averageTimePerMove.toString() === "NaN") ? 0 : averageTimePerMove;
    $("#timePerMove").text("Time Per Move: " + Math.round(averageTimePerMove) + " seconds");
    $("#minesLeft").text("Mines Left: " + currentGame.currentPlayer.amountOfMinesToPlace);
}

function updateGeneralDataLine(gameRecord) {
    var currentGame = gameRecord.game;
    var tab = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";
    var currentTime = new Date().getTime();
    var totalTurns = currentGame.players[0].hitAmount + currentGame.players[0].missAmount +
        currentGame.players[1].hitAmount + currentGame.players[1].missAmount;
    var timePassedInSec = Math.floor((currentTime - currentGame.startTime) / 1000);
    var timeModulo = timePassedInSec%60 < 10 ? ("0" + timePassedInSec%60) : (timePassedInSec%60);
    var totalTimeFormat;

    if (timePassedInSec < 60){
        totalTimeFormat = "0:" + timeModulo;
    } else {
        totalTimeFormat = Math.floor(timePassedInSec/60) + ":" + timeModulo;
    }

    $("#generalData").text(gameRecord.participants[0]["username"] + "'s Score: " +
        currentGame.player2.score + tab +
        (gameRecord.participants.length === 1 ? "" : gameRecord.participants[1]["username"] + "'s Score: " +
            gameRecord.game.player1.score + tab) + "Time Passed: " + totalTimeFormat +
        tab + "Total Turns Completed: " + totalTurns);
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

function updateMineButton(gameRecord, minesLeft, numOfPlayers) {
    var btn;
    if (numOfPlayers === 2){
        if (isItMyTurn(gameRecord) && minesLeft > 0)
            btn = "<button id=\u0022insertMineButton\u0022 draggable='true' ondragstart='drag(event)'></button>";
        else
            btn = "<button id=\u0022insertMineButton\u0022 style='opacity: 0.5' disabled></button>";
        document.getElementById("mineButton").innerHTML = btn;
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