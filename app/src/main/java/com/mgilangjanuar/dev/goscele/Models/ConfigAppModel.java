package com.mgilangjanuar.dev.goscele.Models;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class ConfigAppModel {
    public static String BASE_URL = "https://scele.cs.ui.ac.id/";
    public static String SIAK_BASE_URL = "https://academic.ui.ac.id/";

    public static String urlTo(String path) {
        return urlTo(path, false);
    }

    public static String urlTo(String path, boolean isSiak) {
        return (isSiak ? SIAK_BASE_URL : BASE_URL) + path;
    }
}
