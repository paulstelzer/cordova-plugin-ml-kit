package de.paulstelzer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import android.net.Uri;
import android.content.Context;
import android.support.annotation.NonNull;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import java.io.IOException;
import java.util.Arrays;

public class MlKitPlugin extends CordovaPlugin {
    private static final String TAG = "MlKitPlugin";
    private static Context context;

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        context = cordova.getActivity().getApplicationContext();
    }

    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        if (action.equals("getText")) {
            final String img = args.getString(0);
            cordova.getThreadPool().execute(() -> runTextRecognition(callbackContext, img));
        } else if (action.equals("getTextCloud")) {
            final String img = args.getString(0);
            cordova.getThreadPool().execute(() -> runTextRecognitionCloud(callbackContext, img, "de"));
        }
        return true;
    }

    private void runTextRecognition(final CallbackContext callbackContext, final String img) {
        Uri imgSource = Uri.parse(img);

        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(cordova.getContext(), imgSource);
            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            textRecognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText texts) {
                    JSONArray json = new JSONArray();

                    for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()) {
                        Log.d(TAG, block.getText());
                        json.put(block.getText());
                    }
                    callbackContext.success(json);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Task failed with an exception
                    e.printStackTrace();
                    callbackContext.error(e.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

    private void runTextRecognitionCloud(final CallbackContext callbackContext, final String img,
            final String language) {
        Uri imgSource = Uri.parse(img);

        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(cordova.getContext(), imgSource);

            FirebaseVisionCloudTextRecognizerOptions options = null;
            if (!"".equals(language)) {
                options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                        .setLanguageHints(Arrays.asList(language)).build();
            }

            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getCloudTextRecognizer(options);
            textRecognizer.processImage(image).addOnSuccessListener(texts -> {
                JSONArray json = new JSONArray();

                for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()) {
                    Log.d(TAG, block.getText());
                    json.put(block.getText());
                }
                callbackContext.success(json);
            }).addOnFailureListener(e -> {
                // Task failed with an exception
                e.printStackTrace();
                callbackContext.error(e.getMessage());
            });
        } catch (IOException e) {
            e.printStackTrace();
            callbackContext.error(e.getMessage());
        }
    }

}
