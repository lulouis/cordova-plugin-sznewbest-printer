var exec        = require('cordova/exec'),
    channel     = require('cordova/channel'),
    ua          = navigator.userAgent.toLowerCase(),
    isIOS       = ua.indexOf('ipad') > -1 || ua.indexOf('iphone') > -1,
    isIPAD      = ua.indexOf('ipad') > -1,
    isMac       = ua.indexOf('macintosh') > -1,
    isWin       = window.Windows !== undefined,
    isAndroid   = !isWin && ua.indexOf('android') > -1,
    isWinPC     = isWin && Windows.System.Profile.AnalyticsInfo.versionInfo.deviceFamily.includes('Desktop'),
    isDesktop   = isMac || isWinPC;

var SZnewbestPrinter = function() {};

//support to send DP command
SZnewbestPrinter.prototype.printGBKText = function (str, success, error) {
    exec(success, error, 'SZnewbestPrinter', 'printGBKText', [str]);
};

SZnewbestPrinter.prototype.printBarCode = function (str, success, error) {
    exec(success, error, 'SZnewbestPrinter', 'printBarCode', [str]);
};

SZnewbestPrinter.prototype.printTemplate = function (str, success, error) {
    exec(success, error, 'SZnewbestPrinter', 'printTemplate', [str]);
};

if (!window.plugins) {
    window.plugins = {};
}
  
if (!window.plugins.SZnewbestPrinter) {
    window.plugins.SZnewbestPrinter = new SZnewbestPrinter();
}



module.exports = new SZnewbestPrinter();