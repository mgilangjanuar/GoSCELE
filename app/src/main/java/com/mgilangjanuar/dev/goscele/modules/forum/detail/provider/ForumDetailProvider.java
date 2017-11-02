package com.mgilangjanuar.dev.goscele.modules.forum.detail.provider;

import android.content.Context;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.adapter.CommentRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumCommentListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumDeleteListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumPostListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.model.ForumCommentModel;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.model.ForumDetailModel;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ForumDetailProvider extends BaseProvider {

    private Context context;
    private String url;
    private ForumCommentListener commentListener;
    private ForumPostListener postListener;
    private ForumDeleteListener forumDeleteListener;

    public ForumDetailProvider(Context context, String url, ForumCommentListener commentListener, ForumPostListener postListener, ForumDeleteListener forumDeleteListener) {
        this.context = context;
        this.url = url;
        this.commentListener = commentListener;
        this.postListener = postListener;
        this.forumDeleteListener = forumDeleteListener;
    }

    @Override
    public void run() {
        execute("#region-main");
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

            ForumDetailModel forumDetailModel = new ForumDetailModel();
            forumDetailModel.url = url;
            forumDetailModel.title = elements.select(".subject").get(0).text();
            forumDetailModel.author = elements.select(".author a").get(0).text();
            forumDetailModel.date = elements.select(".author").get(0).text().replace("by " + forumDetailModel.author + " - ", "");
            forumDetailModel.content = elements.select(".maincontent").get(0).html() + "\n\n" + elements.select(".forumpost").get(0).select(".attachments").html();
            forumDetailModel.deleteUrl = elements.select(".forumpost").get(0).select(".commands a:contains(Delete)").attr("href");
            forumDetailModel.save();

            postListener.onRetrievePost(forumDetailModel);

            for (Element e : elements.select(".indent")) {
                ForumCommentModel forumCommentModel = new ForumCommentModel();
                forumCommentModel.author = e.select(".author a").get(0).text();
                forumCommentModel.date = e.select(".author").get(0).text().replace("by " + e.select(".author a").get(0).text() + " - ", "");
                forumCommentModel.content = e.select(".maincontent").get(0).html() + "\n\n" + e.select(".forumpost").get(0).select(".attachments").html();
                forumCommentModel.deleteUrl = e.select(".commands a:contains(Delete)").attr("href");
                forumCommentModel.forum = forumDetailModel;
                forumCommentModel.save();
            }

            commentListener.onRetrieveComments(new CommentRecyclerViewAdapter(context, forumDetailModel.comments(), forumDeleteListener));
        } catch (Exception e) {
            postListener.onError(e.getMessage());
            commentListener.onError(e.getMessage());
        }
    }
}
