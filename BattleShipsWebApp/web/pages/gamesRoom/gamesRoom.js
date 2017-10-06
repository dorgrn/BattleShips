// took from https://stackoverflow.com/questions/3028490/calling-a-java-servlet-from-javascript?rq=1
var USERS_SERVLET_URI = '/gamesRoom/users';
var GAME_RECORDS_SERVLET_URI = '/gamesRoom/gameRecords';
var UPLOAD_XML_URI = '/readResource/readxml';
var GET_USERNAME_URI = '/gamesRoom/currentUser';
var LOGOUT_SERVLET_URI = '/registration/logout';
var USERNAME_ATTRIBUTE = 'username';
var CALLER_URI_ATTRIBUTE = 'CALLER_URI';
var INTERVAL_LENGTH = 2000;

var currentUserName = "";

function getCurrentUserName() {
    $.ajax({
        url: GET_USERNAME_URI,
        type: 'GET',
        contentType: 'application/json; charset=utf-8',
        success: function (users) {
            $.each(users || [], function (index, username) {
                console.log("current username: " + username);
                currentUserName = username;
                console.log("new clal:" + currentUserName);
            });
        }
    });
}

function refreshGamesList(games) {
    //clear all current users
    $("#table_games").find("tr:gt(0)").remove();


    var rows = "";

    $.each(games || [], function (index, game) {
        console.log("Adding game #" + index + ": " + game.gameName);
        rows += "<tr><td>" + game.gameName + "</td>" +
            "<td>" + game.creator.username + "</td>" +
            "<td>" + game.boardSize + "</td>" +
            "<td>" + game.gameStatus + "</td>";
    });

    $(rows).appendTo("#table_games tbody");
}

function refreshUsersList(users) {
    //clear all current users
    $("#table_users").find("tr:gt(0)").remove();


    // rebuild the list of users: scan all users and add them to the list of users
    var rows = "";
    $.each(users || [], function (index, username) {
        console.log("Adding user #" + index + ": " + username);
        rows += "<tr><td>" + username + "</td></tr>";
    });

    $(rows).appendTo("#table_users tbody");
}

function placeCurrentUserName() {
    $("#usernameNav").text(currentUserName);
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
    //console.log("called refresh lists");
    placeCurrentUserName();
    ajaxUsersList();
    ajaxGamesList();
}

function logout() {
    $.ajax({
        type : 'POST',
        url: LOGOUT_SERVLET_URI,
        data: {
            USERNAME_ATTRIBUTE: getCurrentUserName(),
            CALLER_URI_ATTRIBUTE: window.location.href
        },
        success: function (respond) {
        }
    });
}

$(window).on('load', function () {
    setInterval(function () {
        refreshLists()
    }, INTERVAL_LENGTH);
});