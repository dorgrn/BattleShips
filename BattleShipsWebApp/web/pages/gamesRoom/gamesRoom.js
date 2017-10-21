const INTERVAL_LENGTH = 5000;

var intervalRefreshLists = null;

function joinCreatedGame(gameName){
    console.log("helllo");
}

function ajaxCurrentUserName() {
    $.ajax({
        url: GET_USERNAME_URI,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (users) {
            $.each(users || [], function (index, username) {
                $("#usernameNav").text(username);
            });
        }
    });
}

// this function adds user to the given game
function ajaxJoinOrWatch(userRole, game) {
    console.log("got to join or wtch");
    $.ajax({
            type: 'GET',
            url: ADD_USER_URI,
            dataType: 'html',
            data: { // should match Constants
                "USERNAME": $("#usernameNav").text(),
                "GAME_NAME": game.gameName,
                "USER_ROLE": userRole
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status !== null && jqXHR.status === 400){
                    if (jqXHR.getResponseHeader(ERROR_ATTRIBUTE)) {
                        alert(jqXHR.getResponseHeader(ERROR_ATTRIBUTE));
                    }
                }
            },
            success: function() {
                var newGameStatus = getGameStatusAfterJoin(game.gameStatus);
                ajaxUpdateGameStatus(game, newGameStatus);
            }
        }
    );
}

function ajaxUpdateGameStatus(game, newGameStatus) {
    $.ajax({
        type: 'POST',
        url: UPDATE_GAME_STATUS_URI,
        dataType: 'html',
        data: { // should match Constants
            "GAME_NAME": game.gameName,
            "GAME_STATUS": newGameStatus
        },
        success: function (data, textStatus, request) {
            console.log("got to success in ajax gameStatus");
        }
    });
}

function getGameStatusAfterJoin(gameStatus) {
    switch (gameStatus){
        case EMPTY_GAME:
            return ONE_PLAYER;
            break;
        case ONE_PLAYER:
            return FULL_GAME;
            break;
        case FULL_GAME:
            throw "Can't add player to full game";
            break;
    }

    return null;
}

function getGameStatusAfterExit(gameStatus) {
    switch (gameStatus){
        case EMPTY_GAME:
            throw "Can't remove player from empty game";
            break;
        case ONE_PLAYER:
            return EMPTY_GAME;
            break;
        case FULL_GAME:
            return ONE_PLAYER;
            break;
    }

    return null;
}

function joinGame(game) {
    // check that game can be joined
    if (game.gameStatus !== ONE_PLAYER) {
        return;
    }

    console.log("joined to game" + game);
    ajaxJoinOrWatch(USER_PARTICIPANT, game);
}

function watchGame(game) {
    // check that game can be watched
    if (game.gameStatus !== FULL_GAME) {
        return;
    }

    ajaxJoinOrWatch(USER_WATCHER, game);
}

function ajaxUsersList() {
    $.ajax({
        url: USERS_SERVLET_URI,
        dataType: 'json',
        success: function (users) {
            refreshUsersList(users);
        }
    });
}

function ajaxGamesList() {
    $.ajax({
        url: GAME_RECORDS_SERVLET_URI,
        dataType: 'json',
        success: function (games) {
            refreshGamesList(games);
        }
    });
}

function refreshLists() {
    ajaxUsersList();
    ajaxGamesList();
}

// logout assumes #usernamenav is set to username
function logout() {
    $.ajax({
        type: 'GET',
        url: LOGOUT_SERVLET_URI,
        data: {
            "username": $("#usernameNav").text(),
            "CALLER_URI": GAMES_ROOM_URI
        },
        success: function (response) {
            window.location.replace(SIGN_UP_URI);
        }
    });
}

$(window).on('load', function () {
    ajaxCurrentUserName();

    intervalRefreshLists = setInterval(function () {
        refreshLists()
    }, INTERVAL_LENGTH);
});