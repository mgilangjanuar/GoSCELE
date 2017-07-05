package com.mgilangjanuar.dev.goscele.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.ScheduleAdapter;
import com.mgilangjanuar.dev.goscele.MainActivity;
import com.mgilangjanuar.dev.goscele.Presenters.SchedulePresenter;
import com.mgilangjanuar.dev.goscele.Presenters.SettingPresenter;
import com.mgilangjanuar.dev.goscele.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.SimpleDateFormat;

public class ScheduleFragment extends Fragment implements SettingPresenter.SettingServicePresenter {

    SchedulePresenter schedulePresenter;
    MaterialCalendarView materialCalendarView;
    boolean isCannotChangeMonth;

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_schedule);
        toolbar.setTitle(getActivity().getResources().getString(R.string.title_fragment_schedule));
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupContents(view);
            }
        })).start();
    }

    @Override
    public void setupContents(final View view) {
        if (getActivity() == null) {
            return;
        }
        schedulePresenter = new SchedulePresenter(getActivity(), view);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_schedule);

        final ScheduleAdapter adapter = schedulePresenter.buildScheduleAdapterForce();
        final TextView status = (TextView) view.findViewById(R.id.text_status_schedule);

        try {
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                    setDate(view, schedulePresenter.getDate());
                    if (adapter.getItemCount() == 0) {
                        status.setText(getActivity().getResources().getString(R.string.empty_text));
                        status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                    } else {
                        status.setVisibility(TextView.GONE);
                    }
                }
            });
        } catch (NullPointerException e) {
        }

        SlidingUpPanelLayout slidingUpPanelLayout = setupSlidingUpPanelLayout(view);
        setupMaterialCalendarView(view, recyclerView, slidingUpPanelLayout);
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

    private void setDate(View view, String date) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_schedule);
        toolbar.setTitle(date);
        TextView title = (TextView) view.findViewById(R.id.title_slidingup_panel_schedule);
        title.setText("Schedules (" + schedulePresenter.getListScheduleModel().scheduleModelList.size() + ")");
    }

    private void updateAdapterHelper(final View view, final RecyclerView recyclerView, long time, final SlidingUpPanelLayout slidingUpPanelLayout, final boolean isShowDialog) {
        if (isShowDialog) {
            schedulePresenter.showProgressDialog();
        }
        final TextView status = (TextView) view.findViewById(R.id.text_status_schedule);
        status.setVisibility(TextView.VISIBLE);

        schedulePresenter.time = time;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                final ScheduleAdapter scheduleAdapter = schedulePresenter.buildScheduleAdapterForce();
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setDate(view, schedulePresenter.getDateForce());
                        recyclerView.setAdapter(scheduleAdapter);
                        if (scheduleAdapter.getItemCount() == 0) {
                            status.setText(getActivity().getResources().getString(R.string.empty_text));
                            status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                        } else {
                            status.setVisibility(TextView.GONE);
                        }
                        if (isShowDialog) {
                            schedulePresenter.dismissProgressDialog();
                        }
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                    }
                });
            }
        })).start();
    }

    private void updateAdapter(final View view, final RecyclerView recyclerView, long time, SlidingUpPanelLayout slidingUpPanelLayout) {
        updateAdapterHelper(view, recyclerView, time, slidingUpPanelLayout, true);
    }

    private void setupMaterialCalendarView(final View view, final RecyclerView recyclerView, final SlidingUpPanelLayout slidingUpPanelLayout) {
        materialCalendarView = (MaterialCalendarView) view.findViewById(R.id.calendar_view);
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                updateAdapter(view, recyclerView, date.getDate().getTime() / 1000, slidingUpPanelLayout);
            }
        });

        schedulePresenter.buildCalendarEventModel();
        try {
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addDecoratorMaterialCalendarView();
                }
            });
        } catch (NullPointerException e) {
        }


        final String titleToolbar = ((MainActivity) getActivity()).getSupportActionBar().getTitle().toString();
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, final CalendarDay date) {
                schedulePresenter.time2 = date.getDate().getTime() / 1000;
                materialCalendarView.removeDecorators();
                ((MainActivity) getActivity()).getSupportActionBar().setTitle("Please wait...");

                if (isCannotChangeMonth) {
                    return;
                }
                isCannotChangeMonth = true;

                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        schedulePresenter.buildCalendarEventModel();
                        if (getActivity() == null) {
                            return;
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addDecoratorMaterialCalendarView();
                                ((MainActivity) getActivity()).getSupportActionBar().setTitle(titleToolbar);
                                isCannotChangeMonth = false;
                            }
                        });
                    }
                })).start();
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_schedule);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                materialCalendarView.removeDecorators();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                schedulePresenter.buildCalendarEventModel();
                                if (getActivity() == null) {
                                    return;
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addDecoratorMaterialCalendarView();
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        })).start();
                    }
                }, 1000);
            }
        });
    }

    private SlidingUpPanelLayout setupSlidingUpPanelLayout(final View view) {
        final SlidingUpPanelLayout slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, final float slideOffset) {
                final ImageView imageView = (ImageView) view.findViewById(R.id.img_detail_description);
                final TextView title = (TextView) view.findViewById(R.id.title_slidingup_panel_schedule);
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (slideOffset > 0.5) {
                            imageView.setImageResource(R.drawable.ic_sliding_down);
                            title.setText(schedulePresenter.getDate());
                        } else {
                            imageView.setImageResource(R.drawable.ic_sliding_up);
                            title.setText("Schedules (" + schedulePresenter.getListScheduleModel().scheduleModelList.size() + ")");
                        }
                    }
                });
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                return;
            }
        });

        return slidingUpPanelLayout;
    }
}
