window.onload = startGame();

function startGame() {
    $.ajax({
        url: '/gamesRoom/gameRecords',
        dataType: 'json',
        success: function (games) {
            ajaxGetCurrentUserName();
            ajaxGetAndInitCurrentGame(games);
        }
    });
}

function ajaxGetCurrentUserName() {
    $.ajax({
        url: GET_USERNAME_URI,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (users) {
            $.each(users || [], function (index, username) {
                $("#myPlayerName").text(username);
            });
        }
    });
}

function ajaxGetAndInitCurrentGame(games) {
    $.ajax({
        url: '/gamesRoom/currentGameName',
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (gameName) {
            initializeGame(games, gameName);
        }
    });
}

function initializeGame(games, gameName) {
    $.each(games || [], function (index, game) {
        $.ajax({
            url: '/gamesRoom/currentGameName',
            dataType: 'json',
            success: function () {
                if (game.gameName === gameName){
                    createBoardsBySize(game.boardSize);
                    updateDataOnScreen(games, gameName);
                }
            }
        });
    });
}

function createBoardsBySize(boardSize) {
    var i, j;
    var multiplyConst = 0.02*(20-boardSize) + 1;
    var buttonSize = 150*multiplyConst/boardSize;
    var paddingField = 'padding:' + buttonSize + 'px ';
    var table = "";
    for (i=0; i<boardSize; i++){
        table += "<tr>";
        for(j=0; j<boardSize; j++){
            table += "<th style='padding:0'" + ">" + "<button " + "style=" + paddingField + "></button>" + "</th>";
            if (j === boardSize - 1) { table += "</tr>"; }
        }
    }
    document.getElementById("battleshipBoard").innerHTML = table;
    document.getElementById("traceBoard").innerHTML = table;
}

function updateDataOnScreen(games, gameName) {
    $.each(games || [], function (index, game) {
        $.ajax({
            url: '/gamesRoom/gameRecords',
            dataType: 'json',
            success: function () {
                if (game.gameName === gameName){
                    // use of GameRecordsServlet json:
                    $("#currentPlayer").text("Current Player: " + game.game.currentPlayer.playerType);
                    $("#playerScore").text("Player Score: " + game.game.currentPlayer.score);
                    $("#turnsCompleted").text("Turn Number: " + game.game.currentPlayer.turnNumber);
                    $("#totalHits").text("Total Hits: " + game.game.currentPlayer.hitAmount);
                    $("#totalMisses").text("Total Misses: " + game.game.currentPlayer.missAmount);
                    $("#timePerMove").text("Time Per Move: " + game.game.currentPlayer.totalTurnTime);
                    $("#minesLeft").text("Mines Left: " + game.game.currentPlayer.amountOfMinesToPlace);
                    var tab = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";
                    var currentTime = new Date().getTime();
                    $("#generalData").text(game.participants[0]["username"] + "'s Score: " + game.game.player2.score + tab + (game.participants.length === 1 ? "" : game.participants[1]["username"] + "'s Score: " + game.game.player1.score + tab) + "Time Passed: " + (currentTime - game.game.startTime)/1000 + tab + "Total Turns Completed: " + game.game.turnsAmount);
                    updateOpponentName(game.participants);
                    updateWatchersTable(game.watchers);
                    updateCommentsLine(game.participants.length);
                    updateShipsData(game);
                }
            }
        });
    });
}

function updateOpponentName(participants) {
    if (participants.length === 1){
        $("#opponentName").text("No opponent yet");
    }
    else{ // 2 participants
        if (document.getElementById("myPlayerName").innerHTML === participants[0]["username"]){
            $("#opponentName").text(participants[1]["username"]);
        }
        else{
            $("#opponentName").text(participants[0]["username"]);
        }
    }
}

function updateWatchersTable(watchers) {
    var table = "";
    for(var i=0; i<watchers.length; i++){
        table += "<tr><th>" + watchers[i]["username"] + "</th></tr>";
    }
    $("#watchersTable").text(table);
}

function updateCommentsLine(numOfParticipants) {
    if (numOfParticipants === 1){
        $("#commentsLine").text("Waiting for another player...");
    }
    else{
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