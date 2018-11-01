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

## API Methods
### Text recognition

##### **`getText(img, options, success, error): void`**
Text recognition on device

#####  **`getTextCloud(img, options, success, error): void`**
Text recognition on Cloud - Much better results, but you need an active paid plan (Blaze Plan) and activate it on Google Cloud. Parameter are the same like getText

| parameter   | type                        | description  |
| ----------- |-----------------------------|--------------|
| `image`     | `string`                    | Image path on device (file://)  |
| `options`   | `Object`                    | Configuration (only used for cloud) |
| `success` | `(message: Object)=>void` | Success callback |
| `error`   | `(message: Object)=>void` | Error callback |

**`options`**

| name       | type    | default | description            |
| -----------|---------|---------|------------------------|
| `language` | `string` |  ''  |   [Language Code](https://firebase.google.com/docs/ml-kit/android/recognize-text#1-run-the-text-recognizer)    |

**`success`**

| name        | type    |  description           |
| ------------|---------|------------------------|
| `text`      | `string`| Complete text          |
| `textBlocks`| `Array` | Text Blocks ([Model](https://github.com/paulstelzer/cordova-plugin-ml-kit/tree/master/model/ml-kit.model.ts))  |


### Face detection

### Barcode scanning

### Image labeling

### Landmark recognition

### Custom model inference