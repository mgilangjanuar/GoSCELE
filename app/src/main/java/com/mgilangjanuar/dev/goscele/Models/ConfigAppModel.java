package com.mgilangjanuar.dev.goscele.Models;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class ConfigAppModel {
    public static String BASE_URL = "https://scele.cs.ui.ac.id/";

    public static String urlTo(String path) {
        return BASE_URL + path;
    }
}
