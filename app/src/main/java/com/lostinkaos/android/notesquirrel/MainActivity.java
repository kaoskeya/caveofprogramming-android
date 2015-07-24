package com.lostinkaos.android.notesquirrel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    public static final String DEBUGTAG = "NOTESQUIRREL";
    public static final String TEXTFILE = "notesquirrel.txt";
    public static final String FILESAVED = "FileSaved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSaveButtonListener();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean fileSaved = prefs.getBoolean(FILESAVED, false);
        if( fileSaved ) {
            loadSavedFile();
        }
    }

    private void loadSavedFile() {
        try {
            FileInputStream fis = openFileInput(TEXTFILE);

            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));

            EditText editText = (EditText) findViewById( R.id.text );

            String line;

            while((line = reader.readLine()) != null) {
                editText.append(line);
                editText.append("\n");
            }

            fis.close();
        } catch (Exception e) {
            Log.d(DEBUGTAG, "Unable to read file");
        }
    }

    private void addSaveButtonListener() {
        Button saveBtn = (Button)findViewById( R.id.save );

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText editText = (EditText) findViewById( R.id.text );

                String text = editText.getText().toString();

                try {
                    FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_PRIVATE);
                    fos.write(text.getBytes());
                    fos.close();

                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(FILESAVED, true);
                    editor.apply();
                } catch (Exception e) {
                    Log.d(DEBUGTAG, "Unable to save file");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
