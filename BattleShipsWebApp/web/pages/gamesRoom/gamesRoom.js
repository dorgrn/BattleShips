const INTERVAL_LENGTH = 5000;

var intervalRefreshLists = null;

$(window).on('load', function () {
    ajaxCurrentUserName();

    intervalRefreshLists = setInterval(function () {
        refreshLists()
    }, INTERVAL_LENGTH);
});

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
function ajaxJoinOrWatch(userRole, playerType, game) {
    //console.log("got to join or wtch");
    $.ajax({
            type: 'GET',
            url: ADD_USER_URI,
            dataType: 'html',
            data: { // should match Constants
                "USERNAME": $("#usernameNav").text(),
                "PLAYER_TYPE": playerType,
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
                window.location.replace(GAME_URI);
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
            //console.log("got to success in ajax gameStatus");
        }
    });
}

function joinGame(game) {
    // check that game can be joined
    switch (game.gameStatus){
        case FULL_GAME:
            throw "cant join a full game";
            break;
        case EMPTY_GAME:
            ajaxJoinOrWatch(USER_PARTICIPANT, PLAYER_ONE, game);
            break;
        case ONE_PLAYER:
            ajaxJoinOrWatch(USER_PARTICIPANT, PLAYER_TWO, game);
            break;
        default:
            throw "unexpected value in joinGame";
            break;
    }

    console.log("joined to game" + game);
}

function watchGame(game) {
    // check that game can be watched
    switch (game.gameStatus){
        case FULL_GAME:
            throw "cant join a full game";
            break;
        case EMPTY_GAME:
            ajaxJoinOrWatch(USER_WATCHER, PLAYER_ONE, game);
            break;
        case ONE_PLAYER:
            ajaxJoinOrWatch(USER_WATCHER, PLAYER_TWO, game);
            break;
        default:
            throw "unexpected value in watchGame";
            break;
    }

    console.log("watcher added to game" + game);
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