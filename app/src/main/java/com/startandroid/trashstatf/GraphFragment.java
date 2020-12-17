package com.startandroid.trashstatf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    private BarChart mChart;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("График");
        dbHelper = new DatabaseHelper(getActivity());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_graph,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChart = getView().findViewById(R.id.chart1);

        mChart.setMaxVisibleValueCount(20);

        setData(6);//сколько столбцов


    }

    private void setData(int count){
        SharedPreferences loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userLogin = loginPref.getString("UsersLogin","");
        String userLstId = dbHelper.getUserLstId(userLogin);
        ArrayList<BarEntry> yValeus = new ArrayList<>();

        String[] typeOfPack =getContext().getResources().getStringArray(R.array.typeOfPack);

        String[] typeOfPack2 = {"Пластики", "Стекло", "Металлы","Комп мат","Бумага","Орг мат"};

        //Отображение на графике информации об использовании упаковки(6 столбцов для каждого типа. в каждом столбце отображаются разными цветами количество разных кодов переработки)
        for(int i=0; i<count;i++){

            String[] tokens = dbHelper.viewStat(typeOfPack[i],userLstId).toString().split("\n");
            float[] result = new float[tokens.length];
            for(int j=0;j<tokens.length;j++){
                if(tokens[j].split(" ")[0].equals("")){
                    result[j] = 0;
                }
                else{
                    result[j]=Float.parseFloat(tokens[j].split(" ")[0]);
                }

            }




            yValeus.add(new BarEntry(i, result));
        }

        BarDataSet set1;

        set1 = new BarDataSet(yValeus,"Использование упаковок");
        set1.setDrawIcons(false);
        set1.setColors(ColorTemplate.JOYFUL_COLORS);

        BarData data = new BarData(set1);
        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
        mChart.getDescription().setEnabled(false);
        mChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(typeOfPack2));
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setTextSize(12f);




    }
}
