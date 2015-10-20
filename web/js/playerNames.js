var duplicateName = "duplicate_name",
        userFail = "user_fail";

$(document).ready(function () {

    showGameSettings();

    var submit = document.getElementById("sumbitName");
    var nameBox = document.getElementById("playerName");

    nameBox.addEventListener('keyup', function () {
        $("#playerName").css('border-color', '').stop();
    });

    submit.addEventListener('click', function () {

        var playerName = $("#playerName").val();
        if (!playerName.trim()) {
            $("#playerName").css('border-color', 'red').stop();
        }

        else {
            var parameters = $("form").serialize();

            $.ajax({
                data: parameters,
                url: "playerJoinServlet",
                timeout: 2000,
                error: function (x) {
                    if (x.responseText === duplicateName) {
                        $('#dupName').show();
                    }
                    else if (x.responseText === userFail) {
                        $('#tooLate').show();
                        $('#sumbitName').hide();
                        setTimeout("window.location = 'index.html';", 2000);
                    }
                },
                success: function (data) {
                    window.location.href = data;
                }
            });
            return false;
        }
    }, false);


});

function showGameSettings() {
    $.ajax({
        url: 'index',
        success: function (data) {
            showSettings(data);
        },
        error: function (error) {
            console.log("cant find game data");
        }
    });
}


function showSettings(data) {
    var game = JSON.parse(data);
    $("#playersInfo").html(game.numberOfTotalPlayers);
    $("#humansInfo").html(game.numberOfPlayersToJoin);
    $("#colorsInfo").html(game.numberOfColorsForEachPlayer);
    $("#remainingInfo").html(game.numberOfPlayersToJoin - game.playersJoined);

}




