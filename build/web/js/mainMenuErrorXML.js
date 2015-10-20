
$(document).ready(function () {
    document.getElementById("loadbtn").disabled = true;
    $("#newGamebtn").show();
    $("#loadGamebtn").show();
    $('#loadFile').on("change", function () {
        initLoadButton();
    });
});

function initLoadButton() {
    document.getElementById("loadbtn").disabled = true;
    if ($('#loadFile').val() === undefined || $('#loadFile').val() === "") {
        $('#loadbtn').prop("disabled", true);
    }
    else {
        $('#loadbtn').prop("disabled", false);
    }

}


$(document).on({
    mouseenter: function () {
        $(this).stop(true, false).animate({width: "10em"}, 500);
        $(this).find('span').stop(true, false).animate({opacity: "1"}, 800);

    },
    mouseleave: function () {
        $(this).stop(true, false).animate({width: "5em"}, 500);
        $(this).find('span').stop(true, false).animate({opacity: "0"}, 200);
    }
}, '.gamebtn');

$(document).on({
    click: function () {
        $.ajax({
            url: "NewGameServlet",
            timeout: 2000,
            error: function () {
                alert("in error");
                $("#inGameSettings").show();
                console.error("Failed to submit");
            },
            success: function (data) {
                //  data.redirect contains the string URL to redirect to
                window.location.href = data;
            }
        });
        return false;
    }}, '#newGamebtn');

$(document).on({
    click: function () {
        $('#loadGameDiv').show();
        $('#loadGamebtn').blur();
    }

}, '#loadGamebtn');

