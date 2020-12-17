package com.startandroid.trashstatf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddPackFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner spinner1;
    private Spinner spinner2;
    private ArrayAdapter<CharSequence> adapter2;
    private Button addPackButton;

    private DatabaseHelper dbHelper;
    private SharedPreferences loginPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_addpack,container,false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String packType = parent.getItemAtPosition(position).toString();
        switch(packType) {
            case "Стекло":
                adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.RecycleCodesGlass,android.R.layout.simple_spinner_item);
                createSecondSpinner();
                break;
            case "Металлы":
                adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.RecycleCodesMetals,android.R.layout.simple_spinner_item);
                createSecondSpinner();
                break;
            case "Композиционный материалы":
                adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.RecycleCodesComp,android.R.layout.simple_spinner_item);
                createSecondSpinner();
                break;
            case "Бумага":
                adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.RecycleCodesPaper,android.R.layout.simple_spinner_item);
                createSecondSpinner();
                break;
            case "Органические материалы":
                adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.RecycleCodesOrg,android.R.layout.simple_spinner_item);
                createSecondSpinner();
                break;
            default:
                adapter2 = ArrayAdapter.createFromResource(getActivity(),R.array.RecycleCodesPlastic,android.R.layout.simple_spinner_item);
                createSecondSpinner();
                break;
        }


    }

    // необходимый метод для интерфейса AdapterView.OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createSecondSpinner(){
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.notifyDataSetChanged();
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Добавить упаковку");
        setHasOptionsMenu(true);

        //выпадающий список с типом упаковки
        spinner1 = getView().findViewById(R.id.spinner2);
        //выпадающий список с кодом переработки
        spinner2 = getView().findViewById(R.id.spinner3);

        //заполняем первый выпадающий список массивом из strings
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(),R.array.typeOfPack,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);


        //кнопка "Добавить".
        addPackButton = getView().findViewById(R.id.button);
        addPackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dbHelper = new DatabaseHelper(getActivity());
                //считываем все введенные значения в поля формы
                EditText product = getView().findViewById(R.id.productName);
                String mes = product.getText().toString();
                String spinner1Value = String.valueOf(spinner1.getSelectedItem());
                String spinner2Value = String.valueOf(spinner2.getSelectedItem());
                EditText amountPack =  getView().findViewById(R.id.amountPack);
                String mes2 = amountPack.getText().toString();

                if(!isFieldsValidate(mes,mes2)){
                    Toast.makeText(getActivity(),"Вы не заполнили все поля",Toast.LENGTH_SHORT).show();
                    return;
                }


                loginPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
                String userLogin = loginPref.getString("UsersLogin","");
                String userLstId = dbHelper.getUserLst_id(userLogin);
                dbHelper.addProduct(mes,spinner1Value,spinner2Value,Integer.parseInt(mes2),userLstId,userLogin);
                Toast.makeText(getActivity(),"Продукт добавлен!",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isFieldsValidate(String productName, String productAmount){
        boolean flag = true;
        if(productName.equals("")) flag = false;
        if(productAmount.equals("")){
            flag = false;
        }
        return flag;
    }
}
