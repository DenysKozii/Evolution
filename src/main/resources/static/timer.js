var x = setInterval(function () {
    if (document.location.href === "http://localhost:8080/game/new/1") {
        document.location.href = "http://localhost:8080/game";
    }
    if (document.location.href === "http://localhost:8080/game") {
        document.location.href = "http://localhost:8080/game";
    }
}, 3000);