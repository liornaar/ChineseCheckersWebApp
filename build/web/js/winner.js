$("#backToMainButton").on("click", function () {
                window.location.href = "index.html";
            });

function getWinner() {
    $.ajax({
            url: "Winner?action=getName",
            async: false,
            timeout: 2000
        }).done(function (response) {
             $("#currentPlayerTurnName").html(response);
        });
        
        $.ajax({
            url: "Winner?action=getMoves",
            async: false,
            timeout: 2000
        }).done(function (response) {
             $("#numOfMoves").html(response);
        });   
}

getWinner();
