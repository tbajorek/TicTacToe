/**
 * Simple function which guarantees that all opened alerts are closed before opening a new alert
 * @param message Message string
 */
function gameAlert(message) {
    vex.closeAll();
    vex.dialog.alert(message);
}