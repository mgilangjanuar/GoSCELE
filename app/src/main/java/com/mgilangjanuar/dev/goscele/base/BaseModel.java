package com.mgilangjanuar.dev.goscele.base;

import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class BaseModel extends Model {

    public From find() {
        return new Select()
                .from(getClass());
    }
}
