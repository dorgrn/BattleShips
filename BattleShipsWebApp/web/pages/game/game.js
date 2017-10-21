window.onload = startGame();

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
            });
        }
    });
}


function ajaxGetAndInitCurrentGame() {
    $.ajax({
        url: '/gamesRoom/currentGame',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (game) {
            initializeGame(JSON.parse(game));
        }
    });
}

function initializeGame(gameRecord) {
    createBoardsBySize(gameRecord.boardSize);
    ajaxGetCurrentUserName();
    updateDataOnScreen(gameRecord, $("#myPlayerName").text);
}

function createBoardsBySize(boardSize) {
    var i, j;
    var multiplyConst = 0.02 * (20 - boardSize) + 1;
    var buttonSize = 150 * multiplyConst / boardSize;
    var paddingField = 'padding:' + buttonSize + 'px ';
    var table = "";
    for (i = 0; i < boardSize; i++) {
        table += "<tr>";
        for (j = 0; j < boardSize; j++) {
            table += "<th style='padding:0'" + ">" + "<button " + "style=" + paddingField + "></button>" + "</th>";
            if (j === boardSize - 1) {
                table += "</tr>";
            }
        }
    }
    document.getElementById("battleshipBoard").innerHTML = table;
    document.getElementById("traceBoard").innerHTML = table;
}

//TODO: break this function into smaller ones
function updateDataOnScreen(gameRecord, username) {
    var currentGame = gameRecord.game;
    console.log(currentGame);

    // use of GameRecordsServlet json:
    $("#playerScore").text("Player Score: " + currentGame.currentPlayer.score);
    $("#turnsCompleted").text("Turn Number: " + currentGame.currentPlayer.turnNumber);
    $("#totalHits").text("Total Hits: " + currentGame.currentPlayer.hitAmount);
    $("#totalMisses").text("Total Misses: " + currentGame.currentPlayer.missAmount);
    $("#timePerMove").text("Time Per Move: " + currentGame.currentPlayer.totalTurnTime);
    $("#minesLeft").text("Mines Left: " + currentGame.currentPlayer.amountOfMinesToPlace);
    var tab = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";
    var currentTime = new Date().getTime();
    $("#generalData").text(gameRecord.participants[0]["username"] + "'s Score: " +
        currentGame.player2.score + tab +
        (gameRecord.participants.length === 1 ? "" : gameRecord.participants[1]["username"] + "'s Score: " +
            gameRecord.game.player1.score + tab) +
        "Time Passed: " + (currentTime - currentGame.startTime) / 1000 +
        tab + "Total Turns Completed: " + gameRecord.turnsAmount);
    updateOpponentName(gameRecord.participants);
    updateWatchersTable(gameRecord.watchers);
    updateCommentsLine(gameRecord.participants.length);
    updateShipsData(gameRecord);
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

function updateShipsData(game) {
    //// todo: implement...
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