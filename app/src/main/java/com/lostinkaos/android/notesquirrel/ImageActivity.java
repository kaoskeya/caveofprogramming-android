package com.lostinkaos.android.notesquirrel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.List;


public class ImageActivity extends ActionBarActivity implements PointCollectorListener {

    private PointCollector pointCollector = new PointCollector();
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        addTouchListener();
        showPrompt();

        pointCollector.setListener(this);
    }

    private void showPrompt() {
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

    @Override
    public void pointsCollected(final List<Point> points) {
//        Log.d(MainActivity.DEBUGTAG, "Points collected: " + points.size());

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
//                    e.printStackTrace();
                }

                db.storePoints(points);
                Log.d(MainActivity.DEBUGTAG, "points saved");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                dlg.dismiss();
            }
        };

        task.execute();

//        List<Point> list = db.getPoints();

//        for(Point point: list) {
//            Log.d(MainActivity.DEBUGTAG, String.format("Got Point (%d, %d)", point.x, point.y));
//        }
    }
}
