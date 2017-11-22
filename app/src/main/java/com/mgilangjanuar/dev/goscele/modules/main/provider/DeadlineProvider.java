package com.mgilangjanuar.dev.goscele.modules.main.provider;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDeadlineRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDeadlineDetailListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDeadlineListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.CourseModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDeadlineDaysModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDeadlineModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class DeadlineProvider {

    public static class MonthView extends BaseProvider {

        private long time;
        private ScheduleDeadlineListener listener;

        public MonthView(long time, ScheduleDeadlineListener listener) {
            this.time = time;
            this.listener = listener;
        }

        @Override
        public void run() {
            execute(".hasevent a");
        }

        @Override
        public String url() {
            return Constant.BASE_URL + "?time=" + time;
        }

        @Override
        public Map<String, String> cookies() {
            return CookieModel.getCookiesMap();
        }

        @Override
        protected void onPostExecute(List<Elements> elementses) {
            super.onPostExecute(elementses);
            try {
                ScheduleDeadlineDaysModel model = new ScheduleDeadlineDaysModel();
                model.month = new Date(time * 1000).getMonth();

                List<Integer> result = new ArrayList<>();
                Elements elements = elementses.get(0);
                for (Element e: elements) {
                    result.add(Integer.parseInt(e.text()));
                }

                model.setDays(result);
                model.save();

                listener.onRetrieveDeadlineDays(model);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }

    public static class DayView extends BaseProvider {

        private long time;
        private ScheduleDeadlineDetailListener listener;

        public DayView(long time, ScheduleDeadlineDetailListener listener) {
            this.time = time;
            this.listener = listener;
        }

        @Override
        public void run() {
            execute(".event");
        }

        @Override
        public String url() {
            return Constant.BASE_URL + "calendar/view.php?view=day&time=" + time;
        }

        @Override
        public Map<String, String> cookies() {
            return CookieModel.getCookiesMap();
        }

        @Override
        protected void onPostExecute(List<Elements> elementses) {
            super.onPostExecute(elementses);
            try {
                Elements elements = elementses.get(0);
                List<ScheduleDeadlineModel> result = new ArrayList<>();

                for (final Element e: elements) {

                    final CourseModel model = new CourseModel();
                    model.name = e.select(".card-header .course a").text();
                    model.url = e.select(".card-header .course a").attr("href");

                    result.add(new ScheduleDeadlineModel() {{
                        title = e.select(".card-header h3.referer a").text();
                        url = e.select(".card-header h3.referer a").attr("href");
                        time = e.select(".card-header .date").text();
                        description = e.select(".description").html();
                        courseModel = model;
                    }});
                }

                ScheduleDeadlineRecyclerViewAdapter adapter = new ScheduleDeadlineRecyclerViewAdapter(new Date(time * 1000), result);
                listener.onRetrieveDeadlineDetail(adapter);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }
}
