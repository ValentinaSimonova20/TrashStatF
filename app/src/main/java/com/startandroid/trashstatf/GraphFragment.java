package com.startandroid.trashstatf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    private BarChart mChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("График");
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_graph,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart = (BarChart)getView().findViewById(R.id.chart1);

        mChart.setMaxVisibleValueCount(20);

        setData(6);//сколько столбцов


    }

    public void setData(int count){
        ArrayList<BarEntry> yValeus = new ArrayList<>();

        for(int i=0; i<count;i++){
            float val1 = (float)(Math.random()*count)+20;
            float val2 = (float)(Math.random()*count)+20;
            float val3 = (float)(Math.random()*count)+20;

            yValeus.add(new BarEntry(i, new float[]{val1,val2,val3}));
        }

        BarDataSet set1;

        set1 = new BarDataSet(yValeus,"Statisticst of USA");
        set1.setDrawIcons(false);
        set1.setStackLabels(new String[]{"Children","Adults","elders"});
        set1.setColors(ColorTemplate.JOYFUL_COLORS);

        BarData data = new BarData(set1);
        data.setValueFormatter(new MyValueFormatter());


        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
        mChart.getDescription().setEnabled(false);



    }
}
