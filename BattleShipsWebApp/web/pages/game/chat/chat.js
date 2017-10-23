// auto-scroll from https://stackoverflow.com/questions/25505778/automatically-scroll-down-chat-div

const messages = document.getElementById('messages');

function onSendMessage() {
    
}

function appendMessage(username, message) {
    const messageText = username + ": " + message;
    const messageBody = document.getElementsByClassName('message')[0];
    const newMessage = message.cloneNode(messageBody);
    newMessage.textContent = messageText;
    messages.appendChild(newMessage);
}

function getMessages() {
    // Prior to getting your messages.
    shouldScroll = messages.scrollTop + messages.clientHeight === messages.scrollHeight;
    /*
     * Get your messages, we'll just simulate it by appending a new one syncronously.
     */
    appendMessage();
    // After getting your messages.
    if (!shouldScroll) {
        scrollToBottom();
    }
}

function scrollToBottom() {
    messages.scrollTop = messages.scrollHeight;
}
