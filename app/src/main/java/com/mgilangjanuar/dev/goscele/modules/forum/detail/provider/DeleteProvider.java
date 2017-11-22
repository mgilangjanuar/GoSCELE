package com.mgilangjanuar.dev.goscele.modules.forum.detail.provider;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumDeleteListener;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.jsoup.Connection;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class DeleteProvider extends BaseProvider {

    private String url;
    private ForumDeleteListener listener;

    public DeleteProvider(String url, ForumDeleteListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void run() {
        execute("body");
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        try {
            Elements elements = elementses.get(0);
            String sesskey = elements.select("input[desc=sesskey]").attr("value");
            String delete = elements.select("input[desc=delete]").attr("value");
            String confirm = elements.select("input[desc=confirm]").attr("value");
            String submit = "Continue";

            new DeletingPostProvider(sesskey, delete, confirm, submit).run();
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }

    public final class DeletingPostProvider extends BaseProvider {

        private String sesskey;
        private String delete;
        private String confirm;
        private String submit;

        public DeletingPostProvider(String sesskey, String delete, String confirm, String submit) {
            this.sesskey = sesskey;
            this.delete = delete;
            this.confirm = confirm;
            this.submit = submit;
        }

        @Override
        public void run() {
            execute("");
        }

        @Override
        public String url() {
            return Constant.BASE_URL + "mod/forum/post.php";
        }

        @Override
        public Connection.Method method() {
            return Connection.Method.POST;
        }

        @Override
        public Map<String, String> cookies() {
            return CookieModel.getCookiesMap();
        }

        @Override
        public String[] data() {
            return new String[]{"sesskey", sesskey, "delete", delete, "confirm", confirm, "submit", submit};
        }

        @Override
        protected void onPostExecute(List<Elements> elementses) {
            listener.onDeleted();
        }
    }
}
