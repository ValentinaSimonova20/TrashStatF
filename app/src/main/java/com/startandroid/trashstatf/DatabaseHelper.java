package com.startandroid.trashstatf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;



public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME ="PackInformationDB";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_USER = "users";
    public static final String TABLE_DICT = "dictionary";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_LstOfProducts = "list_of_products";
    private static final String KEY_user_login = "user_login";
    private static final String KEY_user_password = "user_password";
    private static final String KEY_user_lstid = "user_lst_id";
    private static final String KEY_user_name = "user_name";
    private static final String KEY_user_mobile = "user_mobile";
    private static final String KEY_user_amountP = "user_lst_amount";
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
                                                    KEY_user_lstid+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                                    KEY_user_name+" TEXT,"+
                                                    KEY_user_mobile+" TEXT );";

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
            KEY_product_id+ " INTEGER,"+KEY_user_amountP+" INTEGER );";



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

    public boolean isUserExists(String login){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+KEY_user_login+"='"+login+"'",null);
        if(cursor.moveToNext()){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean isPassCorrect(String login, String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursore = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+KEY_user_login+"='"+login+"'"+" AND "+KEY_user_password+"='"+pass+"'",null);
        if(cursore.moveToNext()){
            cursore.close();
            return true;
        }
        cursore.close();
        return false;
    }

    public void addNewUser(String password, String email, String number, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_user_login,email);
        values.put(KEY_user_password,password);
        values.put(KEY_user_mobile,number);
        values.put(KEY_user_name,name);
        db.insert(TABLE_USER , null, values);
    }

    public boolean authUser(String email, String pass){
        if(isUserExists(email)){
            return isPassCorrect(email, pass);
        }
        return false;
    }

    public String getUserLst_id(String login){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursore = db.rawQuery("SELECT "+KEY_user_lstid+" FROM "+TABLE_USER+" WHERE "+KEY_user_login+"='"+login+"'",null);
        cursore.moveToNext();
        String Lst_id;
        Lst_id = cursore.getString(cursore.getColumnIndex(KEY_user_lstid));
        return Lst_id;
    }





    public void addProduct(String productName,String packType,String recycleCode,int amount, String userLst_id, String userLogin){
        //Проверяем есть ли данный продукт в таблице продуктов
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT *" +" FROM " +TABLE_PRODUCTS+
                " WHERE " +KEY_product_name+"='"+productName+"'"+" AND "+KEY_dict_id+
                "=(SELECT "+KEY_dict_id+" FROM "+TABLE_DICT+" WHERE "+KEY_dict_type+"='"+packType+"'"+" AND "+KEY_dict_recycleNumber+"='"+recycleCode+"');" ,null);

        //если нет, то добавляем его
        if(!cursor.moveToNext()){

            ContentValues values = new ContentValues();
            //Выбираем ID из таблицы dict(таблица dict id-type-recyclecode)
            Cursor cursor2 = db.rawQuery("SELECT "+KEY_dict_id+" FROM "+TABLE_DICT+" WHERE "+KEY_dict_type+"='"+packType+"' AND "+KEY_dict_recycleNumber+"='"+recycleCode+"'",null);
            cursor2.moveToNext();
            String dict_id = cursor2.getString(cursor2.getColumnIndex(KEY_dict_id));
            values.put(KEY_product_name,productName);
            values.put(KEY_dict_id,dict_id);
            //добавление продукта
            db.insert(TABLE_PRODUCTS,null,values);
            cursor2.close();
        }
        cursor.close();
        //заполняем таблицу listOfProduct(id list-id product - amount)

        Cursor cursor3 = db.rawQuery(
                "SELECT "+ KEY_product_id+" FROM " +TABLE_PRODUCTS+
                        " WHERE " +KEY_product_name+"='"+productName+"' AND "+KEY_dict_id+
                        "=(SELECT "+KEY_dict_id+" FROM "+TABLE_DICT+" WHERE "+KEY_dict_type+"='"+packType+"' AND "+KEY_dict_recycleNumber+"='"+recycleCode+"');" ,null);
        cursor3.moveToNext();
        String product_id = cursor3.getString(cursor3.getColumnIndex(KEY_product_id));
        cursor3.close();

        Cursor cursor5 = db.rawQuery("SELECT "+KEY_user_amountP+" FROM "+TABLE_LstOfProducts+" WHERE "+KEY_product_id+"='"+product_id+"' AND "+KEY_user_lstid+"="+userLst_id,null);

        Cursor cursor4 = db.rawQuery("SELECT "+KEY_user_lstid+" FROM "+TABLE_USER+
                " WHERE "+KEY_user_login+"='"+userLogin+"'",null);
        cursor4.moveToNext();
        String lst_id = cursor4.getString(cursor4.getColumnIndex(KEY_user_lstid));
        cursor4.close();


        ContentValues values2 = new ContentValues();
        values2.put(KEY_user_lstid,lst_id);
        values2.put(KEY_product_id,product_id);

        //обновление lstid, если продукт в данном списке id уже есть
        if (cursor5.moveToNext()){
            int amountP = cursor5.getInt(cursor5.getColumnIndex(KEY_user_amountP));
            amount+=amountP;
            values2.put(KEY_user_amountP,amount);
            String where = KEY_product_id + "='" + product_id+"' AND "+KEY_user_lstid+"="+userLst_id;
            db.update(TABLE_LstOfProducts,values2,where,null);
        }
        else {

            values2.put(KEY_user_amountP,amount);
            db.insert(TABLE_LstOfProducts, null, values2);
        }
        cursor5.close();
    }


    //тестовый вывод результата таблиц product и listOfProducts
    public StringBuilder getResult(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_PRODUCTS ,null);
        String res1,res2,res0;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()){

            res0 = cursor.getString(cursor.getColumnIndex( KEY_product_id));
            res1 = cursor.getString(cursor.getColumnIndex( KEY_product_name));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_dict_id));
            txtData.append(res0+" "+res1 + " "+res2+"\n");

        }
        cursor.close();
        txtData.append("\n\n\n");

        Cursor cursor2 = db.rawQuery("SELECT * FROM " +TABLE_LstOfProducts ,null);
        String res11,res12,res10;
        while(cursor2.moveToNext()){

            res10 = cursor2.getString(cursor2.getColumnIndex( KEY_user_lstid));
            res11 = cursor2.getString(cursor2.getColumnIndex( KEY_product_id));
            res12 = cursor2.getString(cursor2.getColumnIndex(KEY_user_amountP));
            txtData.append(res10+" "+res11 + " "+res12+"\n");

        }
        cursor2.close();
        return txtData;
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
        String[][] recycleCodes = { recycleCodesPl, recycleCodesG, recycleCodesM, recycleCodesComp, recycleCodesPap,recycleCodesOrg };;


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
        String res1,res2,res0,res3,res4;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()) {
            res0 = cursor.getString(cursor.getColumnIndex( KEY_user_login));
            res1 = cursor.getString(cursor.getColumnIndex( KEY_user_password));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_user_lstid));
            res3 = cursor.getString(cursor.getColumnIndex(KEY_user_name));
            res4 = cursor.getString(cursor.getColumnIndex(KEY_user_mobile));
            txtData.append(res0+" "+res1 + " "+res2+" "+res3+" "+res4+"\n");
        }
        cursor.close();
        return txtData;
        }



    //временная функция. проверить всё ли правильно записалось в в Dict с типами упаковок и кодами переработки
    public StringBuilder viewDict(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_DICT+" " ,null);
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

    public StringBuilder viewStat(String type,String userLst_id){
        String query = "SELECT sum("+KEY_user_amountP+") as Amount, "+KEY_dict_recycleNumber+" FROM "+TABLE_PRODUCTS+" INNER JOIN "+TABLE_LstOfProducts+" on "+TABLE_LstOfProducts+"."+
                KEY_product_id+"="+TABLE_PRODUCTS+"."+KEY_product_id+" INNER JOIN "+TABLE_DICT+" on "+TABLE_PRODUCTS+"."+KEY_dict_id+"="+TABLE_DICT+"."+KEY_dict_id+
                " WHERE "+KEY_dict_type+"='"+type+"'"+" AND "+KEY_user_lstid+"="+userLst_id+" GROUP BY "+KEY_dict_recycleNumber;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String res1,res2;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()){
            res1 = cursor.getString(cursor.getColumnIndex("Amount"));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_dict_recycleNumber));
            txtData.append(res2+"                            "+res1+"\n");
        }
        cursor.close();
        return txtData;
    }







}
