// auto-scroll from https://stackoverflow.com/questions/25505778/automatically-scroll-down-chat-div

function onSendMessage(message) {

    ajaxSendMessageToServer(message);
    //$("#messages").text('');
}

function ajaxSendMessageToServer(message) {
    $.ajax({
        url: '/game/chat/sendMessage',
        data: {
            "CHAT_MESSAGE_ATTRIBUTE": message
        },
        type: 'GET',
        success: function () {
        }
    });
}

function ajaxGetMessagesFromServer() {
    $.ajax({
        url: CHAT_GET_MESSAGES_URI,
        dataType: 'json',
        type: 'GET',
        success: function (messages) {
            refreshMessages(messages);
        }
    });
}

function refreshMessages(messagesArray) {
    var messagesDiv = $("#messages");
    messagesDiv.text('');

    scrollToBottom(messagesDiv);
    $.each(messagesArray || [], function (index, valueArray) {
        $.each(valueArray || [] , function (index2, message) {
            appendMessage(message);
        });
    });


}

function appendMessage(message) {
    const messageBody = "<p>" + message.username + ": " + message.chatString + "</p>";
    $("#messages").append(messageBody);
}


function scrollToBottom(messageDiv) {
   $(messageDiv).scrollTop = $(messageDiv)[0].scrollHeight;
}
