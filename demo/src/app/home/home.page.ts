import { Component } from '@angular/core';
import { Camera, CameraOptions } from '@ionic-native/camera/ngx';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  constructor(
    private camera: Camera
  ) {

  }

  makePhoto() {
    const options: CameraOptions = {
      quality: 100,
      destinationType: this.camera.DestinationType.FILE_URI,
      encodingType: this.camera.EncodingType.JPEG,
      mediaType: this.camera.MediaType.PICTURE,
      sourceType: this.camera.PictureSourceType.PHOTOLIBRARY,
      targetWidth: 600,
      targetHeight: 400
    };

    this.camera.getPicture(options).then((imageData) => {
      console.log('imageData', imageData);
      this.getText(imageData);
    }, (err) => {
      // Handle error
      console.log('ERROR Camera', err);
    });
  }

  getText(file) {
    if (window['MlKitPlugin']) {
      console.log('Get Text');
      window['MlKitPlugin'].getText(file, {},
        (success) => {
          console.log('GET TEXT SUCCESS', success);
        },
        (error) => {
          console.log('GET TEXT ERROR', error);
        });

      window['MlKitPlugin'].getTextCloud(file, { language: 'de' },
        (success) => {
          console.log('GET TEXT CLOUD SUCCESS', success);
        },
        (error) => {
          console.log('GET TEXT CLOUD ERROR', error);
        });
    }
  }
}
