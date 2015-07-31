package com.lostinkaos.android.fragmentsintroduction;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by keya on 31/7/15.
 */
public class CourseListFragment extends ListFragment {

    private CourseList courses = new CourseList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity(), R.layout.course_list_item, courses);

        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Course course = courses.get(position);

        Toast.makeText(getActivity(), course.getDescription(), Toast.LENGTH_LONG).show();
    }
}
