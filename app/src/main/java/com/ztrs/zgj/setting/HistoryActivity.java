package com.ztrs.zgj.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ztrs.zgj.R;
import com.ztrs.zgj.databinding.ActivityHistoryBinding;
import com.ztrs.zgj.main.BaseActivity;
import com.ztrs.zgj.utils.ScaleUtils;

public class HistoryActivity extends BaseActivity {

    ActivityHistoryBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding  = DataBindingUtil.setContentView(this, R.layout.activity_history);
        binding.rlTitle.tvTitle.setText("塔机监控历史回放");
        binding.rlStart.setOnClickListener(v -> showStartCalendar());
    }

    private void showStartCalendar(){
        View inflate = LayoutInflater.from(this).inflate(R.layout.history_start_pop, null);
        CalendarView calendarView = inflate.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

            }
        });
        PopupWindow popupWindow = new PopupWindow(inflate,
                ScaleUtils.dip2px(this,300),
                ScaleUtils.dip2px(this,300));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(binding.rlStart);
    }
}
