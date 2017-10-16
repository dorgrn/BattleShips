const INTERVAL_LENGTH = 5000;

var intervalRefreshLists = null;

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

function stopListsRefresh() {
    if (intervalRefreshLists !== null) {
        clearInterval(intervalRefreshLists);
    }
}

function refreshGamesList(games) {
    //clear all current users
    var tableGame = $("#table_games");
    tableGame.find("tr:gt(0)").remove(); // remove everything not in header of table

    var rows = "";

    //add rows to table
    $.each(games || [], function (index, game) {
        //console.log("Adding game #" + index + ": " + game.gameName);
        rows += "<tr><td>" + game.gameName + "</td>" +
            "<td>" + game.creator.username + "</td>" +
            "<td>" + game.boardSize + "</td>" +
            "<td>" + translateGameStatus(game.gameStatus) + "</td>" +
            "<td>" + createJoinGameLink(game) + "</td></tr>";

        tableGame.on('click', '.joinLink', function () {
            joinGame(game);
        });

        tableGame.on('click', '.watchLink', function () {
            watchGame(game);
        });

    });


    $(rows).appendTo("#table_games tbody");
}

function createJoinGameLink(game) {
    var result = "";
    switch (game.gameStatus) {
        case ONE_PLAYER:
            result += "<a href='' onclick='return false;' class='joinLink'>"
                + "Play" + "</a>";
            break;
        case FULL_GAME:
            result += "<a href='' onclick='return false;' class='watchLink'>"
                + "Watch" + "</a>";
            break;
        case EMPTY_GAME:
            result += "-";
            break;
        default:
            break;
    }


    //console.log("In crate game result is" + result);

    return result;
}

// this function adds user to the given game
function ajaxJoinOrWatch(userRole, game) {
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
            success: function (data, textStatus, request) {
                //console.log("GOT TO RELOAD!"); // debug
                if (request.getResponseHeader(REDIRECT_ATTRIBUTE) !== null){

                    window.location.replace(request.getResponseHeader(REDIRECT_ATTRIBUTE));
                }
            }
        }
    );
}

function joinGame(game) {
    // check that game can be joined
    if (game.gameStatus !== ONE_PLAYER) {
        return;
    }

    ajaxJoinOrWatch(USER_PARTICIPANT, game);
}

function watchGame(game) {
    // check that game can be watched
    if (game.gameStatus !== FULL_GAME) {
        return;
    }

    ajaxJoinOrWatch(USER_WATCHER, game);
}

function translateGameStatus(gameStatus) {
    switch (gameStatus) {
        case ONE_PLAYER:
            return "One player waiting";
            break;
        case EMPTY_GAME:
            return "Empty";
            break;
        case FULL_GAME:
            return "Game is full";
            break;
    }

    return "";
}

function refreshUsersList(users) {
    //clear all current users
    $("#table_users").find("tr:gt(0)").remove();


    // rebuild the list of users: scan all users and add them to the list of users
    var rows = "";
    $.each(users || [], function (index, username) {
        //console.log("Adding user #" + index + ": " + username);
        rows += "<tr><td>" + username + "</td></tr>";
    });

    $(rows).appendTo("#table_users tbody");
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