package com.mgilangjanuar.dev.goscele.modules.forum.detail.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "forum_comment")
public class ForumCommentModel extends BaseModel {

    @Column(name = "content")
    public String content;

    @Column(name = "author")
    public String author;

    @Column(name = "date")
    public String date;

    @Column(name = "deleteUrl")
    public String deleteUrl;

    @Column(name = "forum")
    public ForumDetailModel forum;
}
