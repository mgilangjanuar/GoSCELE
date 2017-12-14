package com.mgilangjanuar.dev.goscele.modules.forum.list.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "forum")
public class ForumModel extends BaseModel {

    @Column(name = "listUrl")
    public String listUrl;

    @Column(name = "forumTitle")
    public String forumTitle;

    @Column(name = "url")
    public String url;

    @Column(name = "title")
    public String title;

    @Column(name = "author")
    public String author;

    @Column(name = "repliesNumber")
    public String repliesNumber;

    @Column(name = "lastUpdate")
    public String lastUpdate;

}
