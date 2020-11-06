package com.s33me.hamsandwich;
import android.Manifest;
import android.app.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.*;
import android.view.View.*;
import android.view.*;
import java.text.*;
import java.util.*;
import android.database.*;
import android.content.*;

import android.support.v7.app.AppCompatActivity;

    public class ExportCsv extends BaseMenu {
        //Base Menu extends AppCompatActivity and contains the menu options
        //Common to all activities
        private Button send;
        private EditText email;
        private DBManager dbManager;
        private SimpleCursorAdapter adapter;
        private Cursor cursor;
        private long long_id;

        final String[] from = new String[] { DatabaseHelper._ID,
                DatabaseHelper.DATE, DatabaseHelper.CALL, DatabaseHelper.NAME, DatabaseHelper.FREQ };
        // in call_record.xml
        final int[] to = new int[] { R.id.rec_id, R.id.rec_date, R.id.rec_call, R.id.rec_name, R.id.rec_freq };
        private Context ctx;
        AlertDialog.Builder builder;
        private final static int REQUEST_PERMISSIONS = 20;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.export);
            ctx = this;
            CheckPermissions cp = new CheckPermissions(ctx, ExportCsv.this);
            cp.CheckAll(ctx);
            send = (Button) findViewById(R.id.sendBtn);
            email = (EditText) findViewById(R.id.email);
            dbManager = new DBManager(this);
            dbManager.open();
            cursor = dbManager.fetch();
            send.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {


                    final String e = email.getText().toString();
                    if (!e.equals("")) {

                        Toast.makeText(ExportCsv.this, "Sending contact list to : "+e, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ExportCsv.this, "Enter an email.", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }

    }

