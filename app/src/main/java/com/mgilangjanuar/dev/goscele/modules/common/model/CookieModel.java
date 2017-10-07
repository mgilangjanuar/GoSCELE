package com.mgilangjanuar.dev.goscele.modules.common.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "cookie")
public class CookieModel extends BaseModel {
    @Column(name = "key")
    public String key;

    @Column(name = "value")
    public String value;

    public static Map<String, String> getCookiesMap() {
        Map<String, String> result = new HashMap<>();
        List<CookieModel> list = new CookieModel().find().execute();
        for (CookieModel cookieModel : list) {
            result.put(cookieModel.key, cookieModel.value);
        }
        return result;
    }
}
