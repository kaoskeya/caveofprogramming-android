package com.lostinkaos.android.fragmentsintroduction;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by keya on 31/7/15.
 */
public class CourseListFragment extends ListFragment {

    private OnCourseItemClickListener onCourseItemClickListener;

    public interface OnCourseItemClickListener {
        public void onCourseItemClicked(int position);
    }

    public void setOnCourseItemClickListener(OnCourseItemClickListener onCourseItemClickListener) {
        this.onCourseItemClickListener = onCourseItemClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Data initialised in MainActivity
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        if( onCourseItemClickListener != null ) {
            onCourseItemClickListener.onCourseItemClicked(position);
        }

        /*
        Course course = courses.get(position);

        Toast.makeText(getActivity(), course.getDescription(), Toast.LENGTH_LONG).show();
        */
    }
}
