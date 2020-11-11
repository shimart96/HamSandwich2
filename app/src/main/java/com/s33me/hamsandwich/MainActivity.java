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
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


public class MainActivity extends BaseMenu {
	//Base Menu extends AppCompatActivity and contains the menu options
	//Common to all activities
	private Button submit, record;
	private EditText call, name, freq;
	private DBManager dbManager;
	private String currentDate;
	private ListView listView;
	private SimpleCursorAdapter adapter;
	private Cursor cursor;
	private long long_id;
	private SpeechRecognizer speech = null;
	private Intent recognizerIntent;
	final String[] from = new String[] { DatabaseHelper._ID,
		DatabaseHelper.DATE, DatabaseHelper.CALL, DatabaseHelper.NAME, DatabaseHelper.FREQ };
	// in call_record.xml
	final int[] to = new int[] { R.id.rec_id, R.id.rec_date, R.id.rec_call, R.id.rec_name, R.id.rec_freq };
	private Context ctx;
	//AlertDialog.Builder builder;
	private final static int REQUEST_PERMISSIONS = 20;
	private ImageView bolt;
	private boolean editflag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ctx = this;
        //ctx.getApplicationContext();
        CheckPermissions cp = new CheckPermissions(ctx, MainActivity.this);
        cp.CheckAll(ctx);
		submit = (Button) findViewById(R.id.submitBtn);
		record = (Button) findViewById(R.id.recordBtn);
		call = (EditText) findViewById(R.id.call);
		name = (EditText) findViewById(R.id.name);
		freq = (EditText) findViewById(R.id.freq);
		bolt = (ImageView) findViewById(R.id.bolt);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = sdf.format(new Date());
		dbManager = new DBManager(this);
		dbManager.open();
		cursor = dbManager.fetch();
		Toast.makeText(MainActivity.this, cursor.getCount() + " contacts", Toast.LENGTH_LONG).show();
		listView = findViewById(R.id.list_view);
		listView.setEmptyView(findViewById(R.id.empty));

		adapter = new SimpleCursorAdapter(this, R.layout.call_record, cursor, from, to);
		adapter.notifyDataSetChanged();

		listView.setAdapter(adapter);
		// Add delete option when clicking on list view
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
						int Col1 = cursor.getColumnIndex(DatabaseHelper._ID);
						long_id = cursor.getLong(Col1);
						//confirmDelete(long_id);
						optionDialogue(long_id);
					}
				});
		//dbManager.close();
		submit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						final String d = currentDate;
						final String c = call.getText().toString();

						if (!c.equals("")) {
							if (name.getText().toString().equals("")) {
								name.setText("none");
							}
							if (freq.getText().toString().equals("")) {
								freq.setText("none");
							}
							final String n = name.getText().toString();
							final String f = freq.getText().toString();
							if (editflag == false) {
								dbManager.insert(d, c, n, f);
							} else {
								dbManager.update(long_id, d, c, n, f);
								editflag = false;
							}
							Toast.makeText(MainActivity.this, "Saving Contact", Toast.LENGTH_SHORT).show();
							refreshList();
							animateBolt();
							bolt.setVisibility(View.VISIBLE);

							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									bolt.setVisibility(View.GONE);
								}
							}, 3000);

						} else {
							Toast.makeText(MainActivity.this, "Nothing entered.", Toast.LENGTH_SHORT).show();
						}
					}

				});
		final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


		final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
				Locale.getDefault());


		mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
			@Override
			public void onReadyForSpeech(Bundle bundle) {

			}

			@Override
			public void onBeginningOfSpeech() {

			}

			@Override
			public void onRmsChanged(float v) {

			}

			@Override
			public void onBufferReceived(byte[] bytes) {

			}

			@Override
			public void onEndOfSpeech() {

			}

			@Override
			public void onError(int i) {

			}

			@Override
			public void onResults(Bundle bundle) {
				//getting all the matches
				ArrayList<String> matches = bundle
						.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

				//displaying the first match
				if (matches != null)
					call.setText(matches.get(0));
			}

			@Override
			public void onPartialResults(Bundle bundle) {

			}

			@Override
			public void onEvent(int i, Bundle bundle) {

			}
		});
		findViewById(R.id.recordBtn).setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
					case MotionEvent.ACTION_UP:
						//when the user removed the finger
						mSpeechRecognizer.stopListening();
						call.setHint("Call");
						break;

					case MotionEvent.ACTION_DOWN:
						//finger is on the button
						mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
						call.setText("");
						call.setHint("Listening...");
						break;
				}
				return false;
			}
		});


    }
    public void optionDialogue(long pass_id) {
		final Long rec_id = pass_id;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Tap an option");
		String[] options = {"Edit", "Delete", "Cancel"};
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog1, int which) {
				switch(which) {
					case 0:
							editflag = true;
							Cursor id_cursor = dbManager.selectById(rec_id);
							int call_column = id_cursor.getColumnIndex(DatabaseHelper.CALL);
							String selected_call = id_cursor.getString(call_column);
							call.setText(selected_call);
							int name_column = id_cursor.getColumnIndex(DatabaseHelper.NAME);
							String selected_name = id_cursor.getString(name_column);
							name.setText(selected_name);
							int freq_column = id_cursor.getColumnIndex(DatabaseHelper.FREQ);
							String selected_freq = id_cursor.getString(freq_column);
							freq.setText(selected_freq);

					break;
					case 1:

						confirmDelete(rec_id);

					break;
					case 2:
						dialog1.cancel();
					break;
					default:
						return;
				}
			}
		});
		AlertDialog alert = builder.create();
		//Setting the title manually
		alert.show();
	}
    public void confirmDelete(long pass_id) {
    	final long del_id = pass_id;
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage("Do you want to delete this record ?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						deleteRecord(del_id);
						Toast.makeText(getApplicationContext(),"Deleted record for "+ String.valueOf(del_id),
								Toast.LENGTH_SHORT).show();
						refreshList();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//  Action for 'NO' Button
						dialog.cancel();
						Toast.makeText(getApplicationContext(),"Cancelled",
								Toast.LENGTH_SHORT).show();
					}
				});
		//Creating dialog box
		AlertDialog alert = builder1.create();
		//Setting the title manually
		alert.setTitle("Delete Record");
		alert.show();
	}
	public void deleteRecord(long del_id) {
		dbManager.delete(del_id);

	}
	public void refreshList() {
		//dbManager = new DBManager(this);
		//dbManager.open();
		cursor = dbManager.fetch();
		listView = findViewById(R.id.list_view);
		listView.setEmptyView(findViewById(R.id.empty));

		adapter = new SimpleCursorAdapter(this, R.layout.call_record, cursor, from, to);
		adapter.notifyDataSetChanged();
		listView.setAdapter(adapter);
		Toast.makeText(MainActivity.this, cursor.getCount() + " contacts", Toast.LENGTH_LONG).show();
		// Add delete option when clicking on list view
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
				int Col1 = cursor.getColumnIndex(DatabaseHelper._ID);
				long_id = cursor.getLong(Col1);
				//confirmDelete(long_id);
				optionDialogue(long_id);
			}
		});
		//dbManager.close();
		call.setText("");
		call.setHint("Call");
		name.setText("");
		name.setHint("Name");
		freq.setText("");
		freq.setHint("Freq");
	}
	public void animateBolt() {
		bolt.setVisibility(View.VISIBLE);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				bolt.setVisibility(View.GONE);
			}
		}, 1000);
	}

}
