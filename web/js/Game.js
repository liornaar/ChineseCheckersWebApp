var buttonsBoard;
var boardJSON;
var pointsJSON;
var currentPlayerName = "none";

function createInitBoard() {
    var buttonsInARow = [1, 2, 3, 4, 13, 12, 11, 10, 9, 10, 11, 12, 13, 4, 3, 2, 1];
    buttonsBoard = [];
    var boardDataIndex = 0;
    updateButtonsFromBoard();
    updatePointsFromBoard();
    var boardData = JSON.parse(boardJSON);
    var pointsData = JSON.parse(pointsJSON);
    for (var i = 0; i < 17; i++) {
        buttonsBoard[i] = [];
        var row = $('<div>', {class: 'cell', align: 'center'});
        for (var j = 0; j < buttonsInARow[i]; j++) {
            row.append($('<button>', {class: boardData[boardDataIndex], id: boardDataIndex, onclick: "onMarblePicked(this)", "row": pointsData[boardDataIndex]["x"], "col": pointsData[boardDataIndex]["y"]}));
            row.append(" ");
            buttonsBoard[i].push(row);
            boardDataIndex++;
        }
    }
    updateCurrentPlayerName();
}

$(function () {
                $.ajaxSetup({cache: false});
              setInterval(refreshBoard, 750);
              setInterval(updateCurrentPlayerName, 750);
              setInterval(checkIfGameEnded, 1800);
              
              //setInterval(playAIMove, 1000);            
            });

function onMarblePicked(marble) {
    if (marble.getAttribute("class") === "POS_MOVE")
    {
        var parameters = {"row": marble.getAttribute("row"), "col": marble.getAttribute("col")};

        $.ajax({
            data: parameters,
            url: "GameServlet?action=moveMarble",
            async: false,
            timeout: 2000
        }).done(function (response) {
            if(response === "skipped" || response === "gameOver"){
                checkGameState(response);
            }else{
                playAIMove();             
               // $('#countDown').countdown('option', {until: +25});
            }
        });
    }
    else
    {
        var parameters = {"row": marble.getAttribute("row"), "col": marble.getAttribute("col")};

        $.ajax({
            data: parameters,
            url: "GameServlet?action=pickMarble",
            async: false,
            timeout: 2000
        });
    }
}

function updatePointsFromBoard() {
    $.ajax({
        url: "GameServlet?action=updatePointsFromBoard",
        timeout: 2000,
        cache: false,
        async: false,
        contentType: 'application/text/plain'
    }).done(function (response) {
        pointsJSON = response;
    });
}

function updateButtonsFromBoard() {
    $.ajax({
        url: "GameServlet?action=updateButtonsFromBoard",
        timeout: 2000,
        cache: false,
        async: false,
        contentType: 'application/text/plain'
    }).done(function (response) {
        boardJSON = response;
    });
}

function printInitBoard() {
    for (var i = 0; i < 17; i++) {
        for (var j = 0; j < 25; j++) {
            $(".checkersButtonsBoard").append(buttonsBoard[i][j]);
        }
    }
    $('#countDown').countdown({until: +25, onExpiry: resetCD, compact: true, format: 'S', description: ''});
   // $('#countDown').countdown({until: +25, compact: true, format: 'S', description: ''});
}
         
function playAIMove()
{
    $.ajax({
        url: "GameServlet?action=doAIMove",
        timeout: 2000,
        cache: false,
        async: false
    }).done(function (response) {
//        refreshBoard();
//       
//        if (response === "anotherAIMove") {
//            updateCurrentPlayerName();
//            playAIMove();
//        } else {
            checkGameState(response);
   //     }
    });
}
            
$("#finishTurnButton").on("click", function () {

                    $.ajax({
                    url: "GameServlet?action=finishTurn",
                    cache: false,
                    async: false,
                    timeout: 2000
                });
                $("#playerSkippedLabel").html(" ");
                playAIMove();
            });
            
$("#quitButton").on("click", function () {

                    $.ajax({
                    url: "GameServlet?action=quit",
                    cache: false,
                    async: false,
                    timeout: 2000
                }).done(function (response){
                    if(response === "switched"){
                    //    $('#countDown').countdown('option', {until: +25}); 
                    }
                    checkGameState(response);
                });
                playAIMove();
            });       

function updateCurrentPlayerName()
{
    var myUrl = "GameServlet?action=updateCurrentPlayerName";
    
    $.ajax({
        url: myUrl,
        timeout: 2000,
        cache: false,
        async: false,
        contentType: 'application/text/plain'
    }).done(function (response) {
   //     document.getElementById('currentPlayerTurnName').textContent = response;
        $("#currentPlayerTurnName").html(response);
        if (currentPlayerName !== response) {
            $('#countDown').countdown('option', {until: +25});
            currentPlayerName = response;
        }
    }).fail(function( jqXHR, textStatus, errorThrown ) {
        var x=7;
    });
}        

 function refreshBoard()
            {
                updateButtonsFromBoard();
                var boardData = JSON.parse(boardJSON);
                var pointsData = JSON.parse(pointsJSON);
                var row;
                var col;

                for (var i = 0; i < 121; i++) {
                    row = pointsData[i]["x"];
                    col = pointsData[i]["y"];
                       $("button[row='" + (row) + "'][col='" + (col) + "']").removeClass().addClass(boardData[i]);
                }
           }
 
 function checkGameState(currentGameState){
     if(currentGameState === "gameOver"){
         window.location.href = "winner.html";
     }else if(currentGameState === "skipped"){
         $("#playerSkippedLabel").html("A skip move is just made, press the 'finish turn' button to end turn.");
     }     
 }
 
 function checkIfGameEnded()
{
    $.ajax({
        url: "GameServlet?action=checkIfGameEnded",
        timeout: 2000,
        cache: false,
        async: false
    }).done(function (response) {
        checkGameState(response);
    });
}

function resetCD() {
    $('#countDown').countdown('option', {until: +25});
}
         
createInitBoard();
printInitBoard();







