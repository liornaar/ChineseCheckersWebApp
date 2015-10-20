var aliveInterval;
var userClose;

$(function () {
    
    $('#numOfPlayers').click(function (e) {
        var numOfPlayers = parseInt($("#numOfPlayers").val());

        $("#numOfColorsForEach").empty();
        $("#numOfColorsForEach").append(new Option(1, 1));
        if (numOfPlayers === 2) {

            $("#numOfColorsForEach").append(new Option(2, 2));
            $("#numOfColorsForEach").append(new Option(3, 3));
        }
        else if (numOfPlayers === 3) {
            $("#numOfColorsForEach").append(new Option(2, 2));
        }

        $("#numOfHumanPlayers").empty();
        for (i = 1; i <= numOfPlayers; i++)
        {
            $("#numOfHumanPlayers").append(new Option(i, i));
        }

    });
    $('#numOfPlayers').trigger("click");
});



//window.onunload = function () {
//    userClose = true;
//          $.ajax({
//                        url: "index",
//                        timeout: 2000,
//                        data: {close: userClose},
//                        error: function () {
//                            console.log("bye bye");
//                        },
//                        success: function (data) {                           
//                            console.log("bye");
//                        }
//                    });
//};

$("#createGameButton").on("click", function () {
    clearInterval(aliveInterval);
                    var parameters = $("form").serialize();

                    $.ajax({
                        data: parameters,
                        url: "NewGameOptionServlet",
                        timeout: 2000,
                        error: function () {
                            console.error("Failed to submit");
                        },
                        success: function (data) {                           
           //  data.redirect contains the string URL to redirect to
                        if(data !== "error"){
                          window.location.href = data;
                      }else{
                          alert("error, game is already created. You are redirected to enter the game, if it's not already started!");
                          window.location.href = "index.html";
                      }
                        }
                    });
                return false;
            });



