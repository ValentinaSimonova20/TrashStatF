package com.startandroid.trashstatf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;



public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME ="packinfo_database1";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_USER = "users";
    public static final String TABLE_DICT = "dictionary";
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

    private Context context;

    //Create tables

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "+ TABLE_USER +
                                                    "(" + KEY_user_login + " TEXT,"+
                                                    KEY_user_password+" TEXT," +
                                                    KEY_user_lstid+" INTEGER PRIMARY KEY AUTOINCREMENT );";

    private static final String CREATE_TABLE_DICT = "CREATE TABLE "+ TABLE_DICT +
            "(" + KEY_dict_id + "  INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_dict_type+" TEXT," +
            KEY_dict_recycleNumber+" TEXT );";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "+ TABLE_PRODUCTS +
            "(" + KEY_product_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_product_name+" TEXT," +
            KEY_dict_id+" INTEGER );";

    private static final String CREATE_TABLE_LstOfProducts = "CREATE TABLE "+ TABLE_LstOfProducts +
            "(" + KEY_user_lstid  + " INTEGER,"+
            KEY_product_id+" INTEGER );";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DICT);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_LstOfProducts);
        createDict(db);
        addOneUser(db);
    }

    //Добавляем одного тестового пользователя. Будет использоваться до реализации регистрации

    public void addOneUser(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(KEY_user_login,"valentina");
        values.put(KEY_user_password,"1234");
        db.insert(TABLE_USER , null, values);
    }




    //создаем словарь с типами упаковок и их кодами переработок
    public void createDict(SQLiteDatabase db){

        //значения берем из ресурсов
        String[] typeOfPack =context.getResources().getStringArray(R.array.typeOfPack);   ;
        String[] recycleCodesPl =context.getResources().getStringArray(R.array.RecycleCodesPlastic);   ;
        String[] recycleCodesG =context.getResources().getStringArray(R.array.RecycleCodesGlass);   ;
        String[] recycleCodesPap =context.getResources().getStringArray(R.array.RecycleCodesPaper);   ;
        String[] recycleCodesM =context.getResources().getStringArray(R.array.RecycleCodesMetals);   ;
        String[] recycleCodesOrg =context.getResources().getStringArray(R.array.RecycleCodesOrg);   ;
        String[] recycleCodesComp =context.getResources().getStringArray(R.array.RecycleCodesComp);
        String[][] recycleCodes = { recycleCodesPl, recycleCodesG, recycleCodesPap, recycleCodesM, recycleCodesOrg,recycleCodesComp };;


        for (int i=0;i<typeOfPack.length;i++)
        {
            for(String recycleArrayItem: recycleCodes[i]){
                ContentValues values = new ContentValues();
                values.put(KEY_dict_type,typeOfPack[i]);
                values.put(KEY_dict_recycleNumber,recycleArrayItem);
                db.insert(TABLE_DICT , null, values);
            }
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public StringBuilder viewUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_USER+" " ,null);
        String res1,res2,res0;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()) {
            res0 = cursor.getString(cursor.getColumnIndex( KEY_user_login));
            res1 = cursor.getString(cursor.getColumnIndex( KEY_user_password));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_user_lstid));
            txtData.append(res0+" "+res1 + " "+res2+"\n");
        }
        cursor.close();
        return txtData;
        }



    //временная функция. проверить всё ли правильно записалось в в Dict с типами упаковок и кодами переработки
    public StringBuilder viewDict(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_USER+" " ,null);
        String res1,res2,res0;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()){

            res0 = cursor.getString(cursor.getColumnIndex( KEY_dict_id));
            res1 = cursor.getString(cursor.getColumnIndex( KEY_dict_type));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_dict_recycleNumber));
            txtData.append(res0+" "+res1 + " "+res2+"\n");

        }
        cursor.close();
        return txtData;
    }


}
