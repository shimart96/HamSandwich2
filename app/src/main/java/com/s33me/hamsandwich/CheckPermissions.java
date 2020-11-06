package com.s33me.hamsandwich;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class CheckPermissions {
    Context c;
    Activity a;

    private final static int REQUEST_PERMISSIONS = 20;
    public CheckPermissions(Context c, Activity a) {
        this.c = c;
        this.a = a;

    }

    public void CheckAll(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(c, Manifest.permission.INTERNET)
                    + ContextCompat.checkSelfPermission(c, Manifest.permission.RECORD_AUDIO)
                    + ContextCompat.checkSelfPermission(c, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(a, new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            } else {

            }
        }
    }
}
// Originally in each Activity  as function checkPermission();
//
//private void checkPermission() {
//    	/*
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
//				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//						Uri.parse("package:" + getPackageName()));
//
//				startActivity(intent);
//			}
//
//		}
//		*/
//      if (ContextCompat.checkSelfPermission(ExportCsv.this, Manifest.permission.RECORD_AUDIO)
//                    + ContextCompat.checkSelfPermission(ExportCsv.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//                    != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(ExportCsv.this, new String[]{
//                    Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
//                    //return true;
//                    } else {
//                    //return false;
//                    }
//
//                    }
//