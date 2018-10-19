# Cordova Plugin ML Kit

Implements ML Kit as Cordova plugin on iOS and Android.

## Installation

``cordova plugin add cordova-plugin-ml-kit``

## Features

At the moment only Text Recognition on Android is supported! So it would be great if you could add a PR to add more features. This plugin requires ``cordova-plugin-firebase``!

| Feature                | Android | Android (Cloud) | iOS | iOS (Cloud) |
|------------------------|---------|-----------------|-----|-------------|
| Text recognition       | [x]     | [x]             | [ ] | [ ]         |
| Face detection         | [ ]     |                 | [ ] |             |
| Barcode scanning       | [ ]     |                 | [ ] |             |
| Image labeling         | [ ]     | [ ]             | [ ] | [ ]         |
| Landmark recognition   |         | [ ]             |     | [ ]         |
| Custom model inference | [ ]     |                 | [ ] |             |

Some features of ML Kit are only available on device others only on cloud. Please see https://firebase.google.com/docs/ml-kit/ for more information!

### Text recognition

img - file:// on device

options is under development

success returns an array with all text

``getText(img, options, success, error) {}``

On-Device

``getTextCloud(img, options, success, error) {}``

Cloud

