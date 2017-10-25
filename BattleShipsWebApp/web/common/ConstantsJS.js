// URIs
const USERS_SERVLET_URI = '/gamesRoom/users';
const GAME_RECORDS_SERVLET_URI = '/gamesRoom/gameRecords';
const UPLOAD_XML_URI = '/readResource/readxml';
const GET_USERNAME_URI = '/gamesRoom/currentUser';
const LOGOUT_SERVLET_URI = '/registration/logout';
const GAMES_ROOM_URI = '/pages/gamesRoom/gamesRoom.html';
const SIGN_UP_URI = '/pages/signup/signup.html';
const ADD_USER_URI = '/gamesRoom/gameRecords/addUser';
const GAME_URI = '/pages/game/game.html';
const UPDATE_GAME_STATUS_URI = '/gamesRoom/updateGameStatus';
const CHAT_SEND_MESSAGE_URI = "/game/chat/sendMessage";
const CHAT_GET_MESSAGES_URI = "/game/chat/getMessages";

// ATTRIBUTES
const USERNAME_ATTRIBUTE = "USERNAME";
const CALLER_URI_ATTRIBUTE = 'CALLER_URI';
const GAME_NAME_ATTRIBUTE = 'GAME_NAME';
const USER_ROLE_ATTRIBUTE = 'USER_ROLE';
const REDIRECT_ATTRIBUTE = 'REDIRECT';
const ERROR_ATTRIBUTE = 'ERROR_NAME';
const GAME_JSON = 'GAME_JSON';
const CHAT_MESSAGE_ATTRIBUTE = "CHAT_MESSAGE_ATTRIBUTE" ;
const MAKE_TURN_URI = '/gamesRoom/makeTurn';
const PLACE_MINE_URI = '/gamesRoom/placeMine';

// CONSTS
// USER_ROLE
const USER_PARTICIPANT = 'PARTICIPANT';
const USER_WATCHER = 'WATCHER';

// GAME_STATUS
const ONE_PLAYER = 'ONE_PLAYER';
const EMPTY_GAME = 'EMPTY';
const FULL_GAME = 'FULL';

// PLAYER TYPE
const PLAYER_ONE = 'PLAYER_ONE';
const PLAYER_TWO = 'PLAYER_TWO';