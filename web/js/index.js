$(function () {
    $.ajax({
        url: 'index',
        success: function (data) {
            directGameStatus(data);
        },
        error: function (error) {
            $("#error").show();
        }
    });
});


function directGameStatus(gameStatus) {
    var status = JSON.parse(gameStatus);

    var inGame = (status.inGame === true || status.inGameSettings === true || status.waitingForPlayers === true);
    if (!inGame) {
        $('#bodyContainer').load('mainMenu_1.html', function () {
            onLoad();
        });

    } else {
        if (status.inGame) {
            $("#inGame").show();
        }
        else if (status.waitingForPlayers) {
            if (status.gameWasLoaded) {
                window.location = 'loadGamePlayersList.html';
            }
            else {
                window.location = 'playerNameInput.html';
            }
        }

        else if (status.inGameSettings) {
            window.location = 'newGameOptions.html';
          //  $("#inGameSettings").show();
            //if in Loby redirect to loby
        }
    }
}