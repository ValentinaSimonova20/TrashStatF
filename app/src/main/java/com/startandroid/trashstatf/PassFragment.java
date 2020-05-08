package com.startandroid.trashstatf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

public class PassFragment extends Fragment {

    DatabaseHelper dbHelper;

    MaterialEditText email;
    MaterialEditText pass;
    Button authButton;
    SharedPreferences loginPref;


    RelativeLayout root2;

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
        email = getView().findViewById(R.id.emailField);
        pass = getView().findViewById(R.id.passField);

        authButton = getView().findViewById(R.id.authButton);

        root2 = getView().findViewById(R.id.root_element2);

        View.OnClickListener listenerButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root2, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if(TextUtils.isEmpty(pass.getText().toString())){
                    Snackbar.make(root2, "Введите пароль", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                if(dbHelper.authUser(email.getText().toString(),pass.getText().toString())){
                    Snackbar.make(root2, "Вы авторизовались!", Snackbar.LENGTH_SHORT).show();

                    //запоминаем авторизовавшегося пользователя
                    loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = loginPref.edit();
                    ed.putString("UsersLogin",email.getText().toString());
                    ed.commit();


                    return;
                }
                else {
                    Snackbar.make(root2, "Вы не авторизовались!", Snackbar.LENGTH_SHORT).show();
                    return;
                }



            }
        };
        authButton.setOnClickListener(listenerButton);

    }
}
