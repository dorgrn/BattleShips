function createBoardsBySize() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            myFunction(this);
        }
    };
    xhttp.open("GET", "battleShip_5_basic_advance.xml", true);
    xhttp.send();
}

function myFunction(xml) {
    var i, j;
    var xmlDoc = xml.responseXML;
    var boardSize = xmlDoc.getElementsByTagName("boardSize")[0].childNodes[0].nodeValue;
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