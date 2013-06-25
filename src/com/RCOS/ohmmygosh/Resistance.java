package com.RCOS.ohmmygosh;

/**
 * Created by steve on 5/24/13.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.*;
import com.RCOS.ohmmygosh.MainActivity;
import junit.runner.Version;



public class Resistance extends Activity {

    private Spinner band1spr, band2spr, band3spr, band4spr;
    private double band1val, band2val, band3val, band4val;
    private TextView result;
    private String path;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bands);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        path = extras.getString(Intent.EXTRA_TEXT);
        if (path != null) {
            //Toast.makeText(getApplicationContext(), "path: " + path, Toast.LENGTH_LONG).show();
            ImageView imageView2 = (ImageView) findViewById(R.id.imgView2);
            Bitmap bitmap = decodeBitmapFromPath(path, 300, 300);
            imageView2.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(getApplicationContext(), "path not recognized", Toast.LENGTH_SHORT).show();
        }
        //initDebug();
    }

    protected double BandToVal(Spinner band, int bandnum) {
        double val = 0;
        String color = band.getSelectedItem().toString();
        if (bandnum==1||bandnum==2) {
            if (color.equals("Black"))
                val = 0;
            else if (color.equals("Brown"))
                val = 1;
            else if (color.equals("Red"))
                val = 2;
            else if (color.equals("Orange"))
                val = 3;
            else if (color.equals("Yellow"))
                val = 4;
            else if (color.equals("Green"))
                val = 5;
            else if (color.equals("Blue"))
                val = 6;
            else if (color.equals("Violet"))
                val = 7;
            else if (color.equals("Gray"))
                val = 8;
            else if (color.equals("White"))
                val = 9;
            else
                val = -1;
        }
        else if (bandnum==3) {
            if (color.equals("Black"))
                val = 1;
            else if (color.equals("Brown"))
                val = 10;
            else if (color.equals("Red"))
                val = 100;
            else if (color.equals("Orange"))
                val = 1000;
            else if (color.equals("Yellow"))
                val = 10000;
            else if (color.equals("Green"))
                val = 100000;
            else if (color.equals("Blue"))
                val = 1000000;
            else if (color.equals("Violet"))
                val = 10000000;
            else if (color.equals("Gray"))
                val = 100000000;
            else if (color.equals("White"))
                val = 1000000000;
            else if (color.equals("Gold"))
                val = 0.1;
            else if (color.equals("Silver"))
                val = 0.01;
            else
                val = -1;
        }
        else if (bandnum==4) {
            if (color.equals("Gold"))
                val = 5;
            else if (color.equals("Silver"))
                val = 10;
        }

        return val;
    }

    public void calculate(View view) {
        band1spr = (Spinner) findViewById(R.id.band1);
        band1val = BandToVal(band1spr, 1);
        band2spr = (Spinner) findViewById(R.id.band2);
        band2val = BandToVal(band2spr, 2);
        band3spr = (Spinner) findViewById(R.id.band3);
        band3val = BandToVal(band3spr, 3);
        band4spr = (Spinner) findViewById(R.id.band4);
        band4val = BandToVal(band4spr, 4);
        double ohms = ((band1val*10) + band2val)*band3val;
        result = (TextView)findViewById(R.id.result);
        result.setText(ohms + "+-" + band4val + "%");
    }
}
