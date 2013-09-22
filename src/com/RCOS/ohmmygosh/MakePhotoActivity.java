package com.RCOS.ohmmygosh;

/**
 * Created by steve on 7/8/13.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MakePhotoActivity extends Activity {

    private boolean checkCameraHW(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // device has camera
            return true;
        } else {
            // device doesn't have camera
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e) {
            // camera not available
        }
        return c;
    }

    public void onClick() {

    }

}
