package com.startandroid.trashstatf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatisticsFragment extends Fragment {

    private DatabaseHelper dbHelper;

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
        SharedPreferences loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userLogin = loginPref.getString("UsersLogin","");
        String userLstId = dbHelper.getUserLstId(userLogin);
        TextView txtViewPlastic = getView().findViewById(R.id.textViewPlastics);
        txtViewPlastic.setText(dbHelper.viewStat("Пластики",userLstId).toString());


        //Отображение информации по упаковкам в виде - номер переработки - количество

        TextView txtViewPaper = getView().findViewById(R.id.textViewPaper);
        txtViewPaper.setText(dbHelper.viewStat("Бумага",userLstId).toString());

        TextView txtViewMetalls = getView().findViewById(R.id.textViewMetals);
        txtViewMetalls.setText(dbHelper.viewStat("Металлы",userLstId).toString());

        TextView txtViewOrg = getView().findViewById(R.id.textViewOrg);
        txtViewOrg.setText(dbHelper.viewStat("Органические материалы",userLstId).toString());

        TextView txtViewComp = getView().findViewById(R.id.textViewComp);
        txtViewComp.setText(dbHelper.viewStat("Композиционный материалы",userLstId).toString());

        TextView txtViewGlass = getView().findViewById(R.id.textViewGlass);
        txtViewGlass.setText(dbHelper.viewStat("Стекло",userLstId).toString());




    }
}
