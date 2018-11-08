package de.paulstelzer;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.net.Uri;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.graphics.Point;

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
            cordova.getThreadPool().execute(() -> runTextRecognition(callbackContext, img, "", false));
            return true;
        } else if (action.equals("getTextCloud")) {
            final String img = args.getString(0);
            final String lang = args.getString(1);
            Log.d(TAG, "LANGUAGE=" + lang);
            cordova.getThreadPool().execute(() -> runTextRecognition(callbackContext, img, lang, true));
            return true;
        }
        return false;
    }

    private JSONObject mapBoundingBox(Rect rect) throws JSONException {
      JSONObject oBloundingBox = new JSONObject();
      oBloundingBox.put("left", rect.left);
      oBloundingBox.put("right", rect.right);
      oBloundingBox.put("top", rect.top);
      oBloundingBox.put("bottom", rect.bottom);
      return oBloundingBox;
    }

    private JSONArray mapPoints(Point[] points) throws JSONException {
      JSONArray aPoints = new JSONArray();
      for (Point point: points) {
        JSONObject oPoint =  new JSONObject();
        oPoint.put("x", point.x);
        oPoint.put("y", point.y);
        aPoints.put(oPoint);
      }
      return aPoints;
    }

    private void runTextRecognition(final CallbackContext callbackContext, final String img, final String language, final Boolean onCloud) {
        Uri imgSource = Uri.parse(img);

        try {
            FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(cordova.getContext(), imgSource);
            FirebaseVisionTextRecognizer textRecognizer;

            if(onCloud) {
                textRecognizer = this.getTextRecognitionCloud(language);
            } else {
                textRecognizer = this.getTextRecognitionDevice();
            }

            textRecognizer.processImage(image).addOnSuccessListener(texts -> {
              try {
                JSONObject json = new JSONObject();
                JSONArray blocks = new JSONArray();

                json.put("text", texts.getText());
                json.put("textBlocks", blocks);

                for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()) {
                    Log.d(TAG, block.getText());
                    JSONObject oBlock = new JSONObject();
                    JSONArray lines = new JSONArray();
                    oBlock.put("text", block.getText());
                    oBlock.put("confidence", block.getConfidence());
                    oBlock.put("boundingBox", mapBoundingBox(block.getBoundingBox()));
                    oBlock.put("cornerPoints", mapPoints(block.getCornerPoints()));
                    oBlock.put("lines", lines);
                    blocks.put(oBlock);

                    for (FirebaseVisionText.Line line : block.getLines()) {
                      JSONObject oLine = new JSONObject();
                      oLine.put("text", line.getText());
                      oLine.put("confidence", line.getConfidence());
                      oLine.put("boundingBox", mapBoundingBox(line.getBoundingBox()));
                      oLine.put("cornerPoints", mapPoints(line.getCornerPoints()));
                      lines.put(oLine);
                    }
                }
                callbackContext.success(json);
              } catch(JSONException e) {
                e.printStackTrace();
                callbackContext.error(e.getMessage());
              }
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

    private FirebaseVisionTextRecognizer getTextRecognitionDevice() {
        return FirebaseVision.getInstance().getOnDeviceTextRecognizer();
    }

    private FirebaseVisionTextRecognizer getTextRecognitionCloud( final String language) {
        if (!language.isEmpty()) {
            FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                    .setLanguageHints(Arrays.asList(language)).build();

            return FirebaseVision.getInstance()
                    .getCloudTextRecognizer(options);
        } else {
            return FirebaseVision.getInstance().getCloudTextRecognizer();
        }
    }

}
