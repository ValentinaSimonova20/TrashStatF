package com.startandroid.trashstatf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;



public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME ="packinfo_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "users";
    private static final String TABLE_DICT = "dictionary";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_LstOfProducts = "list_of_products";
    private static final String KEY_user_login = "user_login";
    private static final String KEY_user_password = "user_password";
    private static final String KEY_user_lstid = "user_lst_id";
    private static final String KEY_product_id = "product_id";
    private static final String KEY_product_name = "product_name";
    private static final String KEY_dict_id = "dict_id";
    private static final String KEY_dict_type = "dict_type";
    private static final String KEY_dict_recycleNumber = "dict_recycleNumber";


    //Create tables

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "+ TABLE_USER +
                                                    "(" + KEY_user_login + " TEXT,"+
                                                    KEY_user_password+" TEXT," +
                                                    KEY_user_lstid+" INTEGER );";

    private static final String CREATE_TABLE_DICT = "CREATE TABLE "+ TABLE_DICT +
            "(" + KEY_dict_id + "  INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_dict_type+" TEXT," +
            KEY_dict_recycleNumber+" INTEGER );";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "+ TABLE_PRODUCTS +
            "(" + KEY_product_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_product_name+" TEXT," +
            KEY_dict_id+" INTEGER );";

    private static final String CREATE_TABLE_LstOfProducts = "CREATE TABLE "+ TABLE_LstOfProducts +
            "(" + KEY_user_lstid  + " INTEGER,"+
            KEY_product_id+" INTEGER );";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DICT);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_LstOfProducts);
        createDict();
    }

    public void createDict(){
        //здесь будет заполнение таблицы dict - соответствие типа упаковки и кода переработки
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
