// took from https://stackoverflow.com/questions/3028490/calling-a-java-servlet-from-javascript?rq=1

var xhr = new XMLHttpRequest();
xhr.onreadystatechange = function () {
    if (xhr.readyState === 4){
        var data = xhr.responseText;
        alert(data);
    }
}

xhr.open('GET', '/',false);
