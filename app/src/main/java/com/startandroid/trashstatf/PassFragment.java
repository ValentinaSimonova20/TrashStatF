package com.startandroid.trashstatf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.rengwuxian.materialedittext.MaterialEditText;

public class PassFragment extends Fragment {

    private DatabaseHelper dbHelper;

    private MaterialEditText email;
    private MaterialEditText pass;
    private SharedPreferences loginPref;


    private RelativeLayout root2;

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

        Button authButton = getView().findViewById(R.id.authButton);

        root2 = getView().findViewById(R.id.root_element2);

        View.OnClickListener listenerButton = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    Snackbar.make(root2, "Введите вашу почту", com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }


                if(TextUtils.isEmpty(pass.getText().toString())){
                    Snackbar.make(root2, "Введите пароль", com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT).show();
                    return;
                }


                if(dbHelper.authUser(email.getText().toString(),pass.getText().toString())){
                    Snackbar.make(root2, "Вы авторизовались!", com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT).show();

                    //запоминаем авторизовавшегося пользователя
                    loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed = loginPref.edit();
                    ed.putString("UsersLogin",email.getText().toString());
                    ed.commit();


                }
                else {
                    Snackbar.make(root2, "Вы не авторизовались!",com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT).show();
                }



            }
        };
        authButton.setOnClickListener(listenerButton);

    }
}
