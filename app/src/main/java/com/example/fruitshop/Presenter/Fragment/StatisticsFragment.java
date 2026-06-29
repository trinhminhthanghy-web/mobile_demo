package com.example.fruitshop.Presenter.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fruitshop.Application.Model.RevenueByDate;
import com.example.fruitshop.Application.ViewModel.OrderViewModel;
import com.example.fruitshop.Infrastructure.Data.AppDatabase;
import com.example.fruitshop.Infrastructure.Data.DAO.StatisticsDao;
import com.example.fruitshop.Infrastructure.Tool.Extension;
import com.example.fruitshop.Presenter.Adapter.TopProductAdapter;
import com.example.fruitshop.R;
import com.example.fruitshop.databinding.FragmentStatisticsBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class StatisticsFragment extends Fragment {
    FragmentStatisticsBinding binding;
    OrderViewModel orderViewModel;
    StatisticsDao statisticsDao;
    int selectFilter = 1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        statisticsDao = AppDatabase.getDatabase(requireContext()).statisticsDao();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadChart();
        handleFilter();
        Extension.observeOnce(statisticsDao.getTotalRevenue(getStartDate(),getEndDate()),getViewLifecycleOwner(),data->{
            if(data == null){
                binding.txtTotalRevenue.setText("0");
            }else{
                binding.txtTotalRevenue.setText(String.valueOf(data));
            }
        });

        Extension.observeOnce(statisticsDao.getOrderCount(getStartDate(),getEndDate()),getViewLifecycleOwner(),data->{
            if(data == null){
                binding.txtOrderCount.setText("0");
            }else{
                binding.txtOrderCount.setText(String.valueOf(data));
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false));
        statisticsDao.getTopSellingProducts().observe(getViewLifecycleOwner(),topSellingProducts -> {
            binding.recyclerView.setAdapter(new TopProductAdapter(topSellingProducts));
        });
    }

    public void handleFilter(){
        binding.btnDay.setOnClickListener(v->{
            binding.btnMonth.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnMonth.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnWeek.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnWeek.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnYear.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnYear.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnDay.setBackgroundResource(R.drawable.btn_primary);
            binding.btnDay.setTextColor(Color.parseColor("#FF000000"));
            selectFilter = 1;
            loadChart();
        });

        binding.btnWeek.setOnClickListener(v->{
            binding.btnMonth.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnMonth.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnDay.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnDay.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnYear.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnYear.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnWeek.setBackgroundResource(R.drawable.btn_primary);
            binding.btnWeek.setTextColor(Color.parseColor("#FF000000"));
            selectFilter = 2;
            loadChart();
        });

        binding.btnMonth.setOnClickListener(v->{
            binding.btnWeek.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnWeek.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnDay.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnDay.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnYear.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnYear.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnMonth.setBackgroundResource(R.drawable.btn_primary);
            binding.btnMonth.setTextColor(Color.parseColor("#FF000000"));
            selectFilter = 3;
            loadChart();
        });

        binding.btnYear.setOnClickListener(v->{
            binding.btnMonth.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnMonth.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnDay.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnDay.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnWeek.setBackgroundResource(R.drawable.btn_secondary);
            binding.btnWeek.setTextColor(Color.parseColor("#AAAAAA"));
            binding.btnYear.setBackgroundResource(R.drawable.btn_primary);
            binding.btnYear.setTextColor(Color.parseColor("#FF000000"));
            selectFilter = 4;
            loadChart();
        });
    }



    private void loadChart(){
        statisticsDao.getRevenueByDate(getStartDate(),getEndDate()).observe(getViewLifecycleOwner(),revenueByDates->{
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            int index = 0;
            for (RevenueByDate revenueByDate :
                    revenueByDates) {
                entries.add(new BarEntry(index, (float) revenueByDate.revenue));
                try {
                    labels.add(new SimpleDateFormat("dd/MM", Locale.getDefault()).format(sdf.parse(revenueByDate.date)));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                index++;
            }

            BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
            dataSet.setColor(getResources().getColor(R.color.primary_500));
            dataSet.setValueTextColor(getResources().getColor(R.color.black));

            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.8f);
            binding.barChart.setData(barData);
            XAxis xAxis = binding.barChart.getXAxis();
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return labels.get((int) value);
                }
            });
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1f);
            xAxis.setGranularityEnabled(true);

            binding.barChart.getDescription().setEnabled(false);
            binding.barChart.getAxisRight().setEnabled(false);
            binding.barChart.getAxisLeft().setAxisMinimum(0f);
            binding.barChart.animateY(1000);
            binding.barChart.invalidate();
        });
    }

    public String getStartDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        switch (selectFilter) {
            case 1: // Hôm nay
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case 2: // Tuần này
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case 3: // Tháng này
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            case 4: // Năm nay
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                break;
            default:
                throw new IllegalArgumentException("SelectFilter không hợp lệ");
        }

        return sdf.format(calendar.getTime());
    }

    public String getEndDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        switch (selectFilter) {
            case 1: // Hôm nay
                break; // Dùng giờ hiện tại
            case 2: // Tuần này
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                break;
            case 3: // Tháng này
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                break;
            case 4: // Năm nay
                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 999);
                break;
            default:
                throw new IllegalArgumentException("SelectFilter không hợp lệ");
        }

        return sdf.format(calendar.getTime());
    }
}