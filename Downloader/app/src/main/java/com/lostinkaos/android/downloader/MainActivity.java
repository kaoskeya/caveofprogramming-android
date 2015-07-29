package com.lostinkaos.android.downloader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... voids) {
                        try {
                            return downloadHTML();
                        } catch (IOException e) {
                            Log.d("CooLog", e.toString());
                        }

                        return "Can't reach server";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        TextView textView = (TextView) findViewById(R.id.text_view);
                        textView.setText("");

                        try {
                            JSONArray users = (JSONArray) new JSONTokener(s).nextValue();
                            for(int i=0; i<users.length(); i++ ) {
                                JSONObject user = (JSONObject) users.get(i);
                                textView.append( user.get("name").toString() + "\n" );
                            }
                        } catch (JSONException e) {
                            Log.d("CooLog", e.toString());
                            e.printStackTrace();
                        }

                        super.onPostExecute(s);
                    }
                }.execute();

            }
        });

    }

    private String downloadHTML() throws IOException {
        URL url = new URL("http://jsonplaceholder.typicode.com/users");

        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        String lines = "";


        while((line = br.readLine()) != null) {
            lines += line;
//            Log.d("CooLog", line);
        }

        br.close();
        isr.close();
        is.close();

        return lines;
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
