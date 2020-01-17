// Total seconds to wait
var seconds = 12;
function countdown() {
    seconds = seconds - 1;
    if (seconds < 0) {
        window.location = "/CarteFidelites/"+document.getElementById("cardId").value;
    } else {
        // Update remaining seconds
        document.getElementById("countdown").innerHTML = seconds;
        // Count down using javascript
        window.setTimeout("countdown()", 1000);
    }
}
// Run countdown function
countdown();