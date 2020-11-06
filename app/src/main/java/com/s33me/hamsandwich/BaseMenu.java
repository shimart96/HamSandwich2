package com.s33me.hamsandwich;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class BaseMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item1:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);

                return true;
            case R.id.item4:
                Intent intent4 = new Intent(this, ExportCsv.class);
                startActivity(intent4);
                return true;
            case R.id.item2:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.item3:
                finishAndRemoveTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
