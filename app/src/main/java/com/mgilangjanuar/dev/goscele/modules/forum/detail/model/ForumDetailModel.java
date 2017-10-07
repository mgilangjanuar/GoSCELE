package com.mgilangjanuar.dev.goscele.modules.forum.detail.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "forum_detail")
public class ForumDetailModel extends BaseModel {

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

    @Column(name = "delete_url")
    public String deleteUrl;

    public List<ForumCommentModel> comments() {
        return getMany(ForumCommentModel.class, "forum");
    }
}
