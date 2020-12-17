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

public class AdviceFragment extends Fragment {
    DatabaseHelper dbHelper;
    TextView txt1;
    SharedPreferences loginPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Советы");
        setHasOptionsMenu(true);
        dbHelper = new DatabaseHelper(getActivity());
        return inflater.inflate(R.layout.fragment_advice,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt1 = getView().findViewById(R.id.textViewRes);
        loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        String userLogin = loginPref.getString("UsersLogin","");
        String user_lst_id = dbHelper.getUserLst_id(userLogin);
        //txt1.setText(userLogin+" "+user_lst_id);



    }
}
