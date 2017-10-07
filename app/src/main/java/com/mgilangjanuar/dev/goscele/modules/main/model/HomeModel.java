package com.mgilangjanuar.dev.goscele.modules.main.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "post_home")
public class HomeModel extends BaseModel {

    @Column(name = "url")
    public String url;

    @Column(name = "title")
    public String title;

    @Column(name = "author")
    public String author;

    @Column(name = "date")
    public String date;

    @Column(name = "content")
    public String content;

    public HomeModel() {
        super();
    }
}
