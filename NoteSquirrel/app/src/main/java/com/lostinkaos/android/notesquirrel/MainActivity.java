package com.lostinkaos.android.notesquirrel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    public static final String DEBUGTAG = "NOTESQUIRREL";
    public static final String TEXTFILE = "notesquirrel2.txt";
    public static final String FILESAVED = "FileSaved";
    public static final String RESET_PASSPOINTS = "ResetPasspoints";

    private File image;
    private static final int PHOTO_TAKEN_REQUEST = 0;
    private static final int BROWSE_GALLERY_REQUEST = 1;

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

    private void replaceImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.replace_image, null);
        builder.setTitle(R.string.replace_lock_image);
        builder.setView(v);

        final AlertDialog dlg = builder.create();
        dlg.show();

        final Button takePhoto = (Button) v.findViewById(R.id.take_photo);
        final Button browseGallery = (Button) v.findViewById(R.id.browse_gallery);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });

        browseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseGallery();
            }
        });

    }

    private void browseGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, BROWSE_GALLERY_REQUEST);
    }

    private void takePhoto() {
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        image = new File(picturesDirectory, "passpoints_image");

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
        startActivityForResult(i, PHOTO_TAKEN_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if( requestCode == BROWSE_GALLERY_REQUEST ) {
            Toast.makeText(this, "Gallery result: " + data.getData(), Toast.LENGTH_LONG ).show();
        }

        if( requestCode == PHOTO_TAKEN_REQUEST ) {
            if( image == null ) {
                Toast.makeText(this, "Unable to display image", Toast.LENGTH_LONG).show();
                Log.d(DEBUGTAG, "Photo: " + image.getPath());
            }
        }

        resetPasspoints();

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void resetPasspoints() {
        Intent i = new Intent(MainActivity.this, ImageActivity.class);
        i.putExtra(RESET_PASSPOINTS, true);
        startActivity(i);
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
                    Toast.makeText(MainActivity.this, getString(R.string.toast_cant_save), Toast.LENGTH_LONG).show();
                    Log.d(DEBUGTAG, "Unable to save file");
                }
            }
        });

        Button lockBtn = (Button) findViewById( R.id.lock );

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
        switch (id) {
            case R.id.menu_passpoints_reset:
                resetPasspoints();
                return true;
            case R.id.menu_replace_image:
                replaceImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
