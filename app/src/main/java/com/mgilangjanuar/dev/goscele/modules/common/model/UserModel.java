package com.mgilangjanuar.dev.goscele.modules.common.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "user")
public class UserModel extends BaseModel {

    @Column(name = "desc")
    public String name;

    @Column(name = "username")
    public String username;

    @Column(name = "password")
    public String password;

}
