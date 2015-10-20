
function onLoad() {
    $("#h1").fadeIn(1000);
    $("#newGamebtn").delay(400).fadeIn(400);
    $("#loadGamebtn").delay(600).fadeIn(400);
    $("#newGamebtn").animate({width: "10em"}, 500).queue(function() {
     $("#newGamebtn").find('span').animate({opacity: "1"}, 200); 
     $(this).dequeue();
   });            
    
    $("#loadGamebtn").animate({width: "10em"}, 500).queue(function() {
     $("#loadGamebtn").find('span').animate({opacity: "1"}, 200); 
     $(this).dequeue();
   });  
    document.getElementById("loadbtn").disabled = true; 
    $('#loadFile').on("change", function() {initLoadButton(); });
}

function initLoadButton() {
    document.getElementById("loadbtn").disabled = true; 
    if ($('#loadFile').val() === undefined || $('#loadFile').val() === "") {
        $('#loadbtn').prop("disabled", true);
    }
    else {
        $('#loadbtn').prop("disabled", false);
    }

}

//
//$(document).on({
//    mouseenter: function () {
//        $(this).stop(true, false).animate({width: "10em"}, 500);
//        $(this).find('span').stop(true, false).animate({opacity: "1"}, 800);
// 
//    },
//    mouseleave: function () {
//        $(this).stop(true, false).animate({width: "5em"}, 500);
//        $(this).find('span').stop(true, false).animate({opacity: "0"}, 200);
//    }
//}, '.gamebtn');

$(document).on({
    click: function () {
        $.ajax({
            url: "NewGameServlet",
            timeout: 2000,
            error: function () {
                window.location.href = 'index.html';
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

