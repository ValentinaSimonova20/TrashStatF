package com.startandroid.trashstatf;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {

    DatabaseHelper dbHelper;
    TextView txtViewPlastic;
    TextView txtViewMetalls;
    TextView txtViewPaper;
    TextView txtViewOrg;
    TextView txtViewComp;
    TextView txtViewGlass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Статистика");
        dbHelper = new DatabaseHelper(getActivity());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_statistics,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtViewPlastic = getView().findViewById(R.id.textViewPlastics);
        txtViewPlastic.setText(dbHelper.viewStat("Пластики").toString());


        //Отображение информации по упаковкам в виде - номер переработки - количество

        txtViewPaper = getView().findViewById(R.id.textViewPaper);
        txtViewPaper.setText(dbHelper.viewStat("Бумага").toString());

        txtViewMetalls = getView().findViewById(R.id.textViewMetals);
        txtViewMetalls.setText(dbHelper.viewStat("Металлы").toString());

        txtViewOrg = getView().findViewById(R.id.textViewOrg);
        txtViewOrg.setText(dbHelper.viewStat("Органические материалы").toString());

        txtViewComp = getView().findViewById(R.id.textViewComp);
        txtViewComp.setText(dbHelper.viewStat("Композиционный материалы").toString());

        txtViewGlass = getView().findViewById(R.id.textViewGlass);
        txtViewGlass.setText(dbHelper.viewStat("Стекло").toString());




    }
}
