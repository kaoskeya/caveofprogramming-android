package com.lostinkaos.android.fragmentsintroduction;

import java.util.ArrayList;

/**
 * Created by keya on 31/7/15.
 */
public class CourseList extends ArrayList<Course> {

    public CourseList() {
        add(new Course(
                R.drawable.one,
                "Open SQL Editor",
                "This is one cool course"));
        add(new Course(
                R.drawable.two,
                "Your Product",
                "This is one more cool course"));
        add(new Course(
                R.drawable.three,
                "PSD",
                "This is one even more cool course"));
    }

}
