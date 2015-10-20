var redirect;
var gameOn;
var gameOnInterval;


$(document).ready(function () {
    gameOnInterval = setInterval(getGameOn, 2000);
});


function getGameOn () {
    $.ajax({
        url: 'waiting',
        success: function (data) {
            canGameStart(data);
        },
        error: function (error) {
            $("#error").show();
        }
    });
}

function canGameStart(data) {
    gameOn = JSON.parse(data);
    console.error(data);
    redirect = setInterval(changeWindowLocation, 1000);
    
}

function changeWindowLocation() {
    if (gameOn) {
        clearInterval(redirect);
        clearInterval(gameOnInterval);
        window.location = 'game.html';
    }
}