package com.lostinkaos.android.notesquirrel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;


public class ImageActivity extends ActionBarActivity implements PointCollectorListener {

    private static final String PASSWORD_SET = "PASSWORD_SET";
    private static final int POINT_CLOSENESS = 40;
    private PointCollector pointCollector = new PointCollector();
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        android.support.v7.app.ActionBar bar = getSupportActionBar();

        if( bar != null ) {
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayShowTitleEnabled(false);
        }

        addTouchListener();

        pointCollector.setListener(this);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Bundle extras = getIntent().getExtras();
        if( extras != null ) {
            Boolean resetPasspoints = extras.getBoolean(MainActivity.RESET_PASSPOINTS);
            if( resetPasspoints ) {
                editor.putBoolean(PASSWORD_SET, false).commit();
            }
            String newImage = extras.getString(MainActivity.NEW_IMAGE);
            Log.d(MainActivity.DEBUGTAG, "New Image: " + newImage);
            if( newImage != null ) {
                setImage(newImage);
            } else {
                setImage(null);
            }
        } else {
            setImage(null);
        }

        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false);

        if( !passpointsSet ) {
            showSetPasspointsPrompt();
        }
    }

    private void setImage(String path) {
        ImageView imageView = (ImageView) findViewById(R.id.touch_image);

        if( path == null ) {
            Drawable image = getResources().getDrawable(R.drawable.image_default);
            imageView.setImageDrawable(image);
        } else {
            Log.d(MainActivity.DEBUGTAG, "Uri: " + Uri.parse(path));
            imageView.setImageURI(Uri.parse(path));
        }
    }

    private void showSetPasspointsPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setTitle("Create Your Passpoint Sequence");
        builder.setMessage("Touch four points on the image to set the passpoint sequence. You must click the same points in the future to gain access to your notes.");

        AlertDialog dlg = builder.create();

        dlg.show();
    }

    private void addTouchListener() {
        ImageView image = (ImageView) findViewById(R.id.touch_image);

        image.setOnTouchListener(pointCollector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
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

    private void savePasspoints(final List<Point> points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.storing_data));

        final AlertDialog dlg = builder.create();
        dlg.show();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                db.storePoints(points);
                Log.d(MainActivity.DEBUGTAG, "Points collected: " + points.size());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PASSWORD_SET, true);
                editor.apply();

                pointCollector.clear();
                dlg.dismiss();
            }
        };

        task.execute();
    }

    private void verifyPasspoints(final List<Point> touchedPoints) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Checking passpoints...");
        final AlertDialog dlg = builder.create();
        dlg.show();

        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {

                List<Point> savedPoints = db.getPoints();

                Log.d(MainActivity.DEBUGTAG, "Located points: " + savedPoints.size());

                if(savedPoints.size() != PointCollector.NUM_POINTS || touchedPoints.size() != PointCollector.NUM_POINTS) {
                    return false;
                }

                for(int i = 0; i < PointCollector.NUM_POINTS; i++) {
                    Point savedPoint = savedPoints.get(i);
                    Point touchedPoint = touchedPoints.get(i);

                    int xDiff = savedPoint.x - touchedPoint.x;
                    int yDiff = savedPoint.y - touchedPoint.y;

                    int distSquared = xDiff * xDiff + yDiff * yDiff;

                    Log.d(MainActivity.DEBUGTAG, "Distance squared: " + distSquared);

                    if( distSquared > POINT_CLOSENESS * POINT_CLOSENESS ) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean pass) {

                dlg.dismiss();
                pointCollector.clear();

                if( pass ) {
                    Intent i = new Intent(ImageActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(ImageActivity.this, "Access Denied", Toast.LENGTH_LONG).show();
                }

            }
        };

        task.execute();
    }

    @Override
    public void pointsCollected(final List<Point> points) {

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false);

        if(!passpointsSet) {
            Log.d(MainActivity.DEBUGTAG, "Saving passpoints...");
            savePasspoints(points);
        } else {
            Log.d(MainActivity.DEBUGTAG, "Verifying passpoints...");
            verifyPasspoints(points);
        }
    }
}
