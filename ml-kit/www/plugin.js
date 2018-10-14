
var exec = require('cordova/exec');
var PLUGIN_NAME = 'MlKitPlugin';

module.exports = {
  getText: function (img, success, error) {
    exec(success, error, PLUGIN_NAME, 'getText', [img]);
  },
  getTextCloud: function (img, success, error) {
    exec(success, error, PLUGIN_NAME, 'getTextCloud', [img]);
  }
};