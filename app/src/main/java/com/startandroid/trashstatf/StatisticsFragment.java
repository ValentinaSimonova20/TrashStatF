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

    DatabaseHelper dbHelper;
    TextView txtViewPlastic;
    TextView txtViewMetalls;
    TextView txtViewPaper;
    TextView txtViewOrg;
    TextView txtViewComp;
    TextView txtViewGlass;
    SharedPreferences loginPref;

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
        loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userLogin = loginPref.getString("UsersLogin","");
        String user_lst_id = dbHelper.getUserLst_id(userLogin);
        txtViewPlastic = getView().findViewById(R.id.textViewPlastics);
        txtViewPlastic.setText(dbHelper.viewStat("Пластики",user_lst_id).toString());


        //Отображение информации по упаковкам в виде - номер переработки - количество

        txtViewPaper = getView().findViewById(R.id.textViewPaper);
        txtViewPaper.setText(dbHelper.viewStat("Бумага",user_lst_id).toString());

        txtViewMetalls = getView().findViewById(R.id.textViewMetals);
        txtViewMetalls.setText(dbHelper.viewStat("Металлы",user_lst_id).toString());

        txtViewOrg = getView().findViewById(R.id.textViewOrg);
        txtViewOrg.setText(dbHelper.viewStat("Органические материалы",user_lst_id).toString());

        txtViewComp = getView().findViewById(R.id.textViewComp);
        txtViewComp.setText(dbHelper.viewStat("Композиционный материалы",user_lst_id).toString());

        txtViewGlass = getView().findViewById(R.id.textViewGlass);
        txtViewGlass.setText(dbHelper.viewStat("Стекло",user_lst_id).toString());




    }
}
