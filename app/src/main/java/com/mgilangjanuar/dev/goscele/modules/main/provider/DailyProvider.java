package com.mgilangjanuar.dev.goscele.modules.main.provider;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyDetailRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDailyDetailListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDailyListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleCourseModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyGroupModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class DailyProvider {

    private ScheduleDailyListener dailyListener;

    private ScheduleDailyDetailListener dailyDetailListener;

    public DailyProvider(ScheduleDailyListener dailyListener, ScheduleDailyDetailListener dailyDetailListener) {
        this.dailyListener = dailyListener;
        this.dailyDetailListener = dailyDetailListener;
    }

    public void create() {
        new Auth(dailyListener, dailyDetailListener).run();
    }

    public static class Auth extends BaseProvider {

        private ScheduleDailyListener dailyListener;

        private ScheduleDailyDetailListener dailyDetailListener;

        public Auth(ScheduleDailyListener dailyListener, ScheduleDailyDetailListener dailyDetailListener) {
            this.dailyListener = dailyListener;
            this.dailyDetailListener = dailyDetailListener;
        }

        @Override
        public void run() {
            execute("");
        }

        @Override
        public String url() {
            return Constant.BASE_URL_SIAK + Constant.ROUTE_LOGIN_SIAK;
        }

        @Override
        public Connection.Method method() {
            return Connection.Method.POST;
        }

        @Override
        public String[] data() {
            UserModel userModel = new UserModel().find().executeSingle();
            return new String[]{"u", userModel.username, "p", userModel.password};
        }

        @Override
        protected void onPostExecute(List<Elements> elements) {
            super.onPostExecute(elements);
            new ChangeRole(dailyListener, dailyDetailListener, cookies).run();
        }
    }

    public static class ChangeRole extends BaseProvider {

        private ScheduleDailyListener dailyListener;
        private ScheduleDailyDetailListener dailyDetailListener;
        private Map<String, String> cookiesSiak;

        public ChangeRole(ScheduleDailyListener dailyListener, ScheduleDailyDetailListener dailyDetailListener, Map<String, String> cookiesSiak) {
            this.dailyListener = dailyListener;
            this.dailyDetailListener = dailyDetailListener;
            this.cookiesSiak = cookiesSiak;
        }

        @Override
        public void run() {
            execute("");
        }

        @Override
        public String url() {
            return Constant.BASE_URL_SIAK + "Authentication/ChangeRole";
        }

        @Override
        public Map<String, String> cookies() {
            return cookiesSiak;
        }

        @Override
        protected void onPostExecute(List<Elements> elements) {
            super.onPostExecute(elements);
            new Schedule(dailyListener, cookies()).run();
            new Class(dailyDetailListener, cookies()).run();

        }
    }

    public static class Class extends BaseProvider {

        private ScheduleDailyDetailListener listener;
        private Map<String, String> cookiesSiak;

        public Class(ScheduleDailyDetailListener listener, Map<String, String> cookiesSiak) {
            this.listener = listener;
            this.cookiesSiak = cookiesSiak;
        }

        @Override
        public void run() {
            execute(".box tbody tr");
        }

        @Override
        public Map<String, String> cookies() {
            return cookiesSiak;
        }

        @Override
        public String url() {
            return Constant.BASE_URL_SIAK + "CoursePlan/CoursePlanViewClass";
        }

        @Override
        protected void onPostExecute(List<Elements> elementses) {
            super.onPostExecute(elementses);
            try {
                Elements elements = elementses.get(0);
                List<ScheduleCourseModel> models = new ArrayList<>();

                int i = 0;
                for (Element e: elements) {
                    if (i > 1) {
                        ScheduleCourseModel model = new ScheduleCourseModel();
                        model.code = e.select("td:eq(1) > a").text();
                        model.courseName = e.select("td:eq(2) > a").text();
                        model.courseClass = e.select("td:eq(2) > span").text();
                        model.credits = e.select("td:eq(3)").text();
                        model.lecturer = e.select("td:eq(7)").text();
                        model.save();
                        models.add(model);
                    }
                    i++;
                }

                ScheduleDailyDetailRecyclerViewAdapter adapter = new ScheduleDailyDetailRecyclerViewAdapter(models);
                listener.onRetrieveCourseDetail(adapter);
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }

    public static class Schedule extends BaseProvider {

        private ScheduleDailyListener listener;
        private Map<String, String> cookiesSiak;

        public Schedule(ScheduleDailyListener listener, Map<String, String> cookiesSiak) {
            this.listener = listener;
            this.cookiesSiak = cookiesSiak;
        }

        @Override
        public void run() {
            execute(".box.cal tbody tr:eq(1) td");
        }

        @Override
        public Map<String, String> cookies() {
            return cookiesSiak;
        }

        @Override
        public String url() {
            return Constant.BASE_URL_SIAK + "CoursePlan/CoursePlanViewSchedule";
        }

        @Override
        protected void onPostExecute(List<Elements> elementses) {
            try {
                super.onPostExecute(elementses);
                Elements elements = elementses.get(0);
                int i = 0;
                List<ScheduleDailyGroupModel> results = new ArrayList<>();
                for (Element e: elements) {
                    if (i != 0 && i != 7) {
                        String day = null;
                        switch (i) {
                            case 1:
                                day = "Monday";
                                break;
                            case 2:
                                day = "Tuesday";
                                break;
                            case 3:
                                day = "Wednesday";
                                break;
                            case 4:
                                day = "Thursday";
                                break;
                            case 5:
                                day = "Friday";
                                break;
                            case 6:
                                day = "Saturday";
                                break;
                        }
                        ScheduleDailyGroupModel dailyGroupModel = new ScheduleDailyGroupModel();
                        dailyGroupModel.day = day;
                        dailyGroupModel.save();
                        for (Element e1: e.select(".sch-inner")) {
                            ScheduleDailyModel dailyModel = new ScheduleDailyModel();
                            dailyModel.time = e1.select("h3").text();
                            dailyModel.desc = e1.select(".desc p").text();
                            dailyModel.scheduleDailyGroupModel = dailyGroupModel;
                            dailyModel.save();
                        }
                        results.add(dailyGroupModel);
                    }
                    i++;
                }
                listener.onRetrieveDailySchedule(new ScheduleDailyRecyclerViewAdapter(results));
            } catch (Exception e) {
                listener.onError(e.getMessage());
            }
        }
    }
}
