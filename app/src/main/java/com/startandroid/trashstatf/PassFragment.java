package com.startandroid.trashstatf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PassFragment extends Fragment {

    DatabaseHelper dbHelper;
    TextView txt1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Авторизация");
        dbHelper = new DatabaseHelper(getActivity());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_pass,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt1 = getView().findViewById(R.id.result);
        txt1.setText(dbHelper.viewStat("Стекло").toString());
    }
}
