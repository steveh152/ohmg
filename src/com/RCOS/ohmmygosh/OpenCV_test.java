package com.RCOS.ohmmygosh;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.Mat;

import android.app.Activity;

/**
 * Created by steve on 7/21/13.
 */
public class OpenCV_test extends Activity implements CvCameraViewListener {

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(Mat inputFrame) {
		// TODO Auto-generated method stub
		return null;
	}
}
