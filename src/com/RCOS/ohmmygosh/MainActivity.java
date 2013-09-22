package com.RCOS.ohmmygosh;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
    private static int CAPTURE_IMAGE_ACTIVITY_REQ = 1;
    private String picturePath = null;
    private ImageView imageView;
    private Uri fileUri = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

    // supposed to be used to see if camera app can be launched
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    // create file name for picture taken by camera and save to a directory
    private File getOutputPhotoFile() {
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getPackageName());
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Toast.makeText(getApplicationContext(), "can't make directory", Toast.LENGTH_LONG).show();
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        return new File(directory.getPath()+File.separator+"RES_"+timeStamp+".jpg");
    }

    // function call to select image from gallery
    public void onButtonClick(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    // function call to start default camera app to take picture
    public void onCameraClick(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getOutputPhotoFile();
        fileUri = Uri.fromFile(getOutputPhotoFile());
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQ);
    }

    // function call to start new activity with camera API and OpenCV
    
    public void scan(View view) {
    	Toast.makeText(getApplicationContext(), "do stuff", Toast.LENGTH_SHORT).show();
        //Intent scan = new Intent(this, OpenCV_example.class);
        //startActivity(scan);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    protected int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float)height/(float)reqHeight);
            final int widthRatio = Math.round((float)width/(float)reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    protected Bitmap decodeBitmapFromPath(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }



	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

        // this happens if image is selected from gallery
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			imageView = (ImageView) findViewById(R.id.imgView);
            Bitmap image = decodeBitmapFromPath(picturePath, 300, 300);
			imageView.setImageBitmap(image);
		}
        // this happens if image is taken with camera
        else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
            if (resultCode == RESULT_OK) {
                Uri photoUri = null;
                if (data == null) {
                    Toast.makeText(this, "Image saved successfully",
                            Toast.LENGTH_LONG).show();
                    photoUri = fileUri;
                } else {
                    photoUri = data.getData();
                    Toast.makeText(this, "Image saved successfully" +
                            data.getData(), Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
            }
            Cursor cursor = getContentResolver()
                    .query(fileUri, null, null, null, null);
            if (cursor == null) // Source is Dropbox or other similar local file path
                picturePath = fileUri.getPath();
            else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                picturePath = cursor.getString(idx);
            }
            imageView = (ImageView) findViewById(R.id.imgView);
            Bitmap pic = decodeBitmapFromPath(picturePath, 300, 300);
            imageView.setImageBitmap(pic);
        }
	}

    public void find_resist(View view) {
        if (picturePath != null) {
            imageView.setImageResource(0);
            Intent event = new Intent(this, Resistance.class);
            event.setType("text/plain");
            event.putExtra(android.content.Intent.EXTRA_TEXT, picturePath);
            startActivity(event);
        }
        else {
            Toast.makeText(getApplicationContext(), "select/take image first", Toast.LENGTH_SHORT).show();
        }
	}

}
