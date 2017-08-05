package com.mgilangjanuar.dev.goscele.Fragments.Schedules;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.ScheduleAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.SchedulePresenter;
import com.mgilangjanuar.dev.goscele.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeadlineFragment extends Fragment implements SchedulePresenter.ScheduleServicePresenter {

    @BindView(R.id.calendar_view)
    MaterialCalendarView materialCalendarView;
    @BindView(R.id.swipe_refresh_schedule)
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    TextView tvTitleSlidingUpPanel;
    TextView tvStatus;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ImageView iViewDetailDescription;

    private SchedulePresenter schedulePresenter;
    private boolean isCannotChangeMonth;
    private String date;

    public static DeadlineFragment newInstance(RecyclerView recyclerView, TextView tvTitleSlidingUpPanel, TextView tvStatus, SlidingUpPanelLayout slidingUpPanelLayout, ImageView iViewDetailDescription) {
        DeadlineFragment fragment = new DeadlineFragment();
        fragment.recyclerView = recyclerView;
        fragment.tvTitleSlidingUpPanel = tvTitleSlidingUpPanel;
        fragment.tvStatus = tvStatus;
        fragment.slidingUpPanelLayout = slidingUpPanelLayout;
        fragment.iViewDetailDescription = iViewDetailDescription;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deadline, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        schedulePresenter = new SchedulePresenter(getActivity());
        (new Thread(() -> setupSchedule())).start();
    }

    @Override
    public void setupSchedule() {
        final ScheduleAdapter adapter = schedulePresenter.buildScheduleAdapter();

        try {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                tvTitleSlidingUpPanel.setText("Loading...");
                recyclerView.setVisibility(RecyclerView.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                setDate(schedulePresenter.getDate());
                if (adapter.getItemCount() == 0) {
                    tvStatus.setText(getActivity().getResources().getString(R.string.empty_text));
                    tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    tvStatus.setVisibility(TextView.GONE);
                }
            });
        } catch (NullPointerException e) {
        }
        slidingUpPanelLayout = setupSlidingUpPanelLayout();
        setupMaterialCalendarView(recyclerView, slidingUpPanelLayout);
    }

    private void addDecoratorMaterialCalendarView() {
        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return schedulePresenter.isCurrentMonth(day.getDate().getTime() / 1000)
                        && schedulePresenter.getCalendarEventModel().listEvent.contains(day.getDay())
                        && !schedulePresenter.convertTimeToString(schedulePresenter.getCurrentTime())
                        .equals((new SimpleDateFormat("EEEE, dd MMM yyyy")).format(day.getDate().getTime()));
            }

            @Override
            public void decorate(DayViewFacade view) {
                if (getContext() == null) return;
                view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), android.R.color.white)));
                view.setSelectionDrawable(ContextCompat.getDrawable(getContext(), R.drawable.round));
            }
        });

        materialCalendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return schedulePresenter.convertTimeToString(schedulePresenter.getCurrentTime())
                        .equals((new SimpleDateFormat("EEEE, dd MMM yyyy")).format(day.getDate().getTime()));
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.color_accent)));
            }
        });
    }

    private void setupMaterialCalendarView(final RecyclerView recyclerView, final SlidingUpPanelLayout slidingUpPanelLayout) {
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.setOnDateChangedListener((widget, date, selected) -> updateAdapter(recyclerView, date.getDate().getTime() / 1000, slidingUpPanelLayout));

        schedulePresenter.buildCalendarEventModel();
        try {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> addDecoratorMaterialCalendarView());
        } catch (NullPointerException e) {
        }

        final String title = tvTitleSlidingUpPanel.getText().toString();
        materialCalendarView.setOnMonthChangedListener((widget, date) -> {
            schedulePresenter.time2 = date.getDate().getTime() / 1000;
            materialCalendarView.removeDecorators();
            tvTitleSlidingUpPanel.setText("Please wait...");

            if (isCannotChangeMonth) return;
            isCannotChangeMonth = true;

            (new Thread(() -> {
                schedulePresenter.buildCalendarEventModel();
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    addDecoratorMaterialCalendarView();
                    isCannotChangeMonth = false;
                    tvTitleSlidingUpPanel.setText(title);
                });
            })).start();
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            materialCalendarView.removeDecorators();
            new Handler().postDelayed(() -> (new Thread(() -> {
                schedulePresenter.buildCalendarEventModel();
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    addDecoratorMaterialCalendarView();
                    swipeRefreshLayout.setRefreshing(false);
                });
            })).start(), 1000);
        });
    }

    private void setDate(String date) {
        tvTitleSlidingUpPanel.setText(date + "  (" + schedulePresenter.getListScheduleModel().scheduleModelList.size() + ")");
    }

    private void updateAdapterHelper(final RecyclerView recyclerView, long time, final SlidingUpPanelLayout slidingUpPanelLayout, final boolean isShowDialog) {
        if (isShowDialog) {
            schedulePresenter.showProgressDialog();
        }
        tvStatus.setVisibility(TextView.VISIBLE);

        schedulePresenter.time = time;
        (new Thread(() -> {
            final ScheduleAdapter scheduleAdapter = schedulePresenter.buildScheduleAdapterForce();
            final String date = schedulePresenter.getDateForce();
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                setDate(date);
                recyclerView.setAdapter(scheduleAdapter);
                if (scheduleAdapter.getItemCount() == 0) {
                    tvStatus.setText(getActivity().getResources().getString(R.string.empty_text));
                    tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    tvStatus.setVisibility(TextView.GONE);
                }
                if (isShowDialog) {
                    schedulePresenter.dismissProgressDialog();
                }
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            });
        })).start();
    }

    private void updateAdapter(final RecyclerView recyclerView, long time, SlidingUpPanelLayout slidingUpPanelLayout) {
        updateAdapterHelper(recyclerView, time, slidingUpPanelLayout, true);
    }

    private SlidingUpPanelLayout setupSlidingUpPanelLayout() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> tvTitleSlidingUpPanel.setText(schedulePresenter.getDate() + "  (" + schedulePresenter.getListScheduleModel().scheduleModelList.size() + ")"));
        }

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, final float slideOffset) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    if (slideOffset > 0.5) {
                        iViewDetailDescription.setImageResource(R.drawable.ic_sliding_down);
                    } else {
                        iViewDetailDescription.setImageResource(R.drawable.ic_sliding_up);
                    }
                });
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            }
        });

        return slidingUpPanelLayout;
    }
}
