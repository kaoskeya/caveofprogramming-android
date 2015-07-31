package com.lostinkaos.android.fragmentsintroduction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {

    private CourseList courses = new CourseList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CourseListFragment courseListFragment = (CourseListFragment) getFragmentManager().findFragmentById(R.id.list);

        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(this, R.layout.course_list_item, courses);

        courseListFragment.setListAdapter(adapter);

        final ProductFragment productFragment = (ProductFragment) getFragmentManager().findFragmentById(R.id.product);

        productFragment.setCourse(courses.get(1));

        courseListFragment.setOnCourseItemClickListener(new CourseListFragment.OnCourseItemClickListener() {
            @Override
            public void onCourseItemClicked(int position) {
                productFragment.setCourse(courses.get(position));
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
