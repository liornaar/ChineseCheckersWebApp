var nameListInterval;
var refreshRate = 4000;
var name;

$(document).ready(function () {

    $(".ajax-loader").animate({opacity: 1}, 400);
    $("#updating").animate({opacity: 1}, 400);
    nameListInterval = setInterval(getNamesList, refreshRate);

    $('#sumbitName').click(function () {
        $.ajax({
            url: "playerJoinServlet",
            type: "POST",
            data: {playerName: name},
            success: function (data) {
                window.location = 'WaitingRoom.html';
            },
            error: function (error) {
                //TODO: do something here
            }
        });
        return false;
    });
});


//TODO: clean here

function getNamesList() {

    $.ajax({
        url: "playerJoinServlet",
        type: "GET",
        success: function (data) {
            var players = JSON.parse(data);
            if (players.length === 0) {
                window.location = 'index.html';
            }
            createNamesList(data);
        },
        error: function (error) {

        }
    });
    return false;
}

function createNamesList(data) {
    var players = JSON.parse(data);
    var names = players.length;
    $('#playerList').empty();
    $('#sumbitName').prop('disabled', true);
    for (var i = 0; i < names; i++) {
        $('#playerList').append("<div><label>" + players[i] + "</label> <input value='" + players[i] + "' id='" + players[i] + "' type='radio'></div>");
        $('#' + players[i]).click(selectName);
    }
    $(".ajax-loader").animate({opacity: 0}, 200);
    $("#updating").animate({opacity: 0}, 200);

//    if(name !== undefined) {
//        $('#'+name).prop('checked', true);
//    }
}

function selectName() {
    if ($('input[type=radio]:checked').size() > 1) {
        $('input[type=radio]').prop('checked', false);
        $('#sumbitName').prop('disabled', true);
    }
    else {
        name = $(this).val();
        $('#sumbitName').prop('disabled', false);
    }
}

