package com.mgilangjanuar.dev.goscele.Models;

/**
 * Created by muhammadgilangjanuar on 5/15/17.
 */

public class CourseModel {
    public String url;
    public String name;

    @Override
    public String toString() {
        return "CourseModel{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());

    }
}
