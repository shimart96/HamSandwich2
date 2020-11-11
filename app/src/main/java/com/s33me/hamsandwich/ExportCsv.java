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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.*;
import java.util.*;
import android.database.*;
import android.content.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        String FILENAME = "HamSandwich.csv";
        String csvRecord = "";

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
            dbManager.open()
            ;

            send.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    final String address = email.getText().toString();
                    if (!address.equals("")) {
                        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G '-' HH:mm:ss z");
                       // String currentDateandTime = sdf.format(new Date());
                       // String filename = FILENAME+currentDateandTime+".csv";
                        String filename = FILENAME;
                        cursor = dbManager.fetch();
                        try {
                            File outFile = new File(folder, filename);
                            FileWriter fileWriter = new FileWriter(outFile);
                            BufferedWriter outRecord = new BufferedWriter(fileWriter);
                            cursor = dbManager.fetch();
                            if (cursor != null) {
                                cursor.moveToFirst();
                                csvRecord = cursor.getString(1) + "," +
                                        cursor.getString(2) + "," +
                                        cursor.getString(3) + "," +
                                        cursor.getString(4) + ","
                                        + "\n";
                                //outRecord.newLine();
                                outRecord.write(csvRecord);
                                while (cursor.moveToNext()) {
                                    csvRecord = cursor.getString(1) + "," +
                                            cursor.getString(2) + "," +
                                            cursor.getString(3) + "," +
                                            cursor.getString(4) + ","
                                            + "\n";
                                    //outRecord.newLine();
                                    outRecord.write(csvRecord);
                                }
                                cursor.close();
                            }
                            outRecord.close();
                            sendEmail(address);
                        } catch (Exception ex) {
                            Toast.makeText(ExportCsv.this, "Problem saving file"+ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(ExportCsv.this, "Sending contact list to : "+address, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ExportCsv.this, "Enter an email.", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
        public void sendEmail(String address) {
            String email, subject, message;
            try {
                email = address;
                subject = "Ham Sandwich Contact List";
                message = "Tap the attach icon, set file manager to view Internal Storage and then attach the file from /Internal storage/Android/data/com.s33me.hamsandwich/files/Documents/HamSandwich.csv";
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
            } catch (Throwable t) {
                Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

