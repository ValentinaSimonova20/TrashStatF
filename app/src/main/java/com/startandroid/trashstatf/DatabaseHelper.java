package com.startandroid.trashstatf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;



public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME ="PackInformationDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "users";
    private static final String TABLE_DICT = "dictionary";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_PRODUCTS_LST = "list_of_products";
    private static final String KEY_USER_LOGIN = "user_login";
    private static final String KEY_USER_PASSWORD = "user_password";
    private static final String KEY_USER_LST_ID = "user_lst_id";
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_USER_PRODUCT_AMOUNT = "user_lst_amount";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_DICT_ID = "dict_id";
    private static final String KEY_DICT_TYPE = "dict_type";
    private static final String KEY_DICT_RECYCLE_NUMBER = "dict_recycleNumber";

    private Context context;

    //Create tables

    private static final String CREATE_TABLE_USERS = "CREATE TABLE "+ TABLE_USER +
                                                    "(" + KEY_USER_LOGIN + " TEXT,"+
                                                    KEY_USER_PASSWORD+" TEXT," +
                                                    KEY_USER_LST_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                                    KEY_USERNAME+" TEXT,"+
                                                    KEY_USER_MOBILE+" TEXT );";

    private static final String CREATE_TABLE_DICT = "CREATE TABLE "+ TABLE_DICT +
            "(" + KEY_DICT_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_DICT_TYPE+" TEXT," +
            KEY_DICT_RECYCLE_NUMBER+" TEXT );";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "+ TABLE_PRODUCTS +
            "(" + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            KEY_PRODUCT_NAME+" TEXT," +
            KEY_DICT_ID+" INTEGER );";

    private static final String CREATE_TABLE_LST_OF_PRODUCTS = "CREATE TABLE "+ TABLE_PRODUCTS_LST +
            "(" + KEY_USER_LST_ID  + " INTEGER,"+
            KEY_PRODUCT_ID+ " INTEGER,"+KEY_USER_PRODUCT_AMOUNT+" INTEGER );";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_DICT);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_LST_OF_PRODUCTS);
        createDict(db);
        addOneUser(db);
    }

    boolean isUserExists(String login){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+KEY_USER_LOGIN+"='"+login+"'",null);
        if(cursor.moveToNext()){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    private boolean isPassCorrect(String login, String pass){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursore = db.rawQuery("SELECT * FROM "+TABLE_USER+" WHERE "+KEY_USER_LOGIN+"='"+login+"'"+" AND "+KEY_USER_PASSWORD+"='"+pass+"'",null);
        if(cursore.moveToNext()){
            cursore.close();
            return true;
        }
        cursore.close();
        return false;
    }

    void addNewUser(String password, String email, String number, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_LOGIN,email);
        values.put(KEY_USER_PASSWORD,password);
        values.put(KEY_USER_MOBILE,number);
        values.put(KEY_USERNAME,name);
        db.insert(TABLE_USER , null, values);
    }

    boolean authUser(String email, String pass){
        if(isUserExists(email)){
            return isPassCorrect(email, pass);
        }
        return false;
    }

    String getUserLstId(String login){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursore = db.rawQuery("SELECT "+KEY_USER_LST_ID+" FROM "+TABLE_USER+" WHERE "+KEY_USER_LOGIN+"='"+login+"'",null);
        cursore.moveToNext();
        String LstId;
        LstId = cursore.getString(cursore.getColumnIndex(KEY_USER_LST_ID));
        return LstId;
    }





    void addProduct(String productName, String packType, String recycleCode, int amount, String userLstId, String userLogin){
        //Проверяем есть ли данный продукт в таблице продуктов
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT *" +" FROM " +TABLE_PRODUCTS+
                " WHERE " +KEY_PRODUCT_NAME+"='"+productName+"'"+" AND "+KEY_DICT_ID+
                "=(SELECT "+KEY_DICT_ID+" FROM "+TABLE_DICT+" WHERE "+KEY_DICT_TYPE+"='"+packType+"'"+" AND "+KEY_DICT_RECYCLE_NUMBER+"='"+recycleCode+"');" ,null);

        //если нет, то добавляем его
        if(!cursor.moveToNext()){

            ContentValues values = new ContentValues();
            //Выбираем ID из таблицы dict(таблица dict id-type-recyclecode)
            Cursor cursor2 = db.rawQuery("SELECT "+KEY_DICT_ID+" FROM "+TABLE_DICT+" WHERE "+KEY_DICT_TYPE+"='"+packType+"' AND "+KEY_DICT_RECYCLE_NUMBER+"='"+recycleCode+"'",null);
            cursor2.moveToNext();
            String dictId = cursor2.getString(cursor2.getColumnIndex(KEY_DICT_ID));
            values.put(KEY_PRODUCT_NAME,productName);
            values.put(KEY_DICT_ID,dictId);
            //добавление продукта
            db.insert(TABLE_PRODUCTS,null,values);
            cursor2.close();
        }
        cursor.close();
        //заполняем таблицу listOfProduct(id list-id product - amount)

        Cursor cursor3 = db.rawQuery(
                "SELECT "+ KEY_PRODUCT_ID+" FROM " +TABLE_PRODUCTS+
                        " WHERE " +KEY_PRODUCT_NAME+"='"+productName+"' AND "+KEY_DICT_ID+
                        "=(SELECT "+KEY_DICT_ID+" FROM "+TABLE_DICT+" WHERE "+KEY_DICT_TYPE+"='"+packType+"' AND "+KEY_DICT_RECYCLE_NUMBER+"='"+recycleCode+"');" ,null);
        cursor3.moveToNext();
        String productId = cursor3.getString(cursor3.getColumnIndex(KEY_PRODUCT_ID));
        cursor3.close();

        Cursor cursor5 = db.rawQuery("SELECT "+KEY_USER_PRODUCT_AMOUNT+" FROM "+TABLE_PRODUCTS_LST+" WHERE "+KEY_PRODUCT_ID+"='"+productId+"' AND "+KEY_USER_LST_ID+"="+userLstId,null);

        Cursor cursor4 = db.rawQuery("SELECT "+KEY_USER_LST_ID+" FROM "+TABLE_USER+
                " WHERE "+KEY_USER_LOGIN+"='"+userLogin+"'",null);
        cursor4.moveToNext();
        String lstId = cursor4.getString(cursor4.getColumnIndex(KEY_USER_LST_ID));
        cursor4.close();


        ContentValues values2 = new ContentValues();
        values2.put(KEY_USER_LST_ID,lstId);
        values2.put(KEY_PRODUCT_ID,productId);

        //обновление lstid, если продукт в данном списке id уже есть
        if (cursor5.moveToNext()){
            int amountP = cursor5.getInt(cursor5.getColumnIndex(KEY_USER_PRODUCT_AMOUNT));
            amount+=amountP;
            values2.put(KEY_USER_PRODUCT_AMOUNT,amount);
            String where = KEY_PRODUCT_ID + "='" + productId+"' AND "+KEY_USER_LST_ID+"="+userLstId;
            db.update(TABLE_PRODUCTS_LST,values2,where,null);
        }
        else {

            values2.put(KEY_USER_PRODUCT_AMOUNT,amount);
            db.insert(TABLE_PRODUCTS_LST, null, values2);
        }
        cursor5.close();
    }




    //Добавляем одного тестового пользователя. Будет использоваться до реализации регистрации

    private void addOneUser(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(KEY_USER_LOGIN,"valentina");
        values.put(KEY_USER_PASSWORD,"1234");
        db.insert(TABLE_USER , null, values);
    }




    //создаем словарь с типами упаковок и их кодами переработок
    private void createDict(SQLiteDatabase db){

        //значения берем из ресурсов
        String[] typeOfPack =context.getResources().getStringArray(R.array.typeOfPack);
        String[] recycleCodesPl =context.getResources().getStringArray(R.array.RecycleCodesPlastic);
        String[] recycleCodesG =context.getResources().getStringArray(R.array.RecycleCodesGlass);
        String[] recycleCodesPap =context.getResources().getStringArray(R.array.RecycleCodesPaper);
        String[] recycleCodesM =context.getResources().getStringArray(R.array.RecycleCodesMetals);
        String[] recycleCodesOrg =context.getResources().getStringArray(R.array.RecycleCodesOrg);
        String[] recycleCodesComp =context.getResources().getStringArray(R.array.RecycleCodesComp);
        String[][] recycleCodes = { recycleCodesPl, recycleCodesG, recycleCodesM, recycleCodesComp, recycleCodesPap,recycleCodesOrg };


        for (int i=0;i<typeOfPack.length;i++)
        {
            for(String recycleArrayItem: recycleCodes[i]){
                ContentValues values = new ContentValues();
                values.put(KEY_DICT_TYPE,typeOfPack[i]);
                values.put(KEY_DICT_RECYCLE_NUMBER,recycleArrayItem);
                db.insert(TABLE_DICT , null, values);
            }
        }



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // метод необходим для использования бибилотеки sqlite
    }

    public StringBuilder viewUsers(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +TABLE_USER+" " ,null);
        String res1;
        String res2;
        String res0;
        String res3;
        String res4;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()) {
            res0 = cursor.getString(cursor.getColumnIndex(KEY_USER_LOGIN));
            res1 = cursor.getString(cursor.getColumnIndex(KEY_USER_PASSWORD));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_USER_LST_ID));
            res3 = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
            res4 = cursor.getString(cursor.getColumnIndex(KEY_USER_MOBILE));
            txtData.append(res0+" "+res1 + " "+res2+" "+res3+" "+res4+"\n");
        }
        cursor.close();
        return txtData;
        }


    StringBuilder viewStat(String type, String userLstId){
        String query = "SELECT sum("+KEY_USER_PRODUCT_AMOUNT+") as Amount, "+KEY_DICT_RECYCLE_NUMBER+" FROM "+TABLE_PRODUCTS+" INNER JOIN "+TABLE_PRODUCTS_LST+" on "+TABLE_PRODUCTS_LST+"."+
                KEY_PRODUCT_ID+"="+TABLE_PRODUCTS+"."+KEY_PRODUCT_ID+" INNER JOIN "+TABLE_DICT+" on "+TABLE_PRODUCTS+"."+KEY_DICT_ID+"="+TABLE_DICT+"."+KEY_DICT_ID+
                " WHERE "+KEY_DICT_TYPE+"='"+type+"'"+" AND "+KEY_USER_LST_ID+"="+userLstId+" GROUP BY "+KEY_DICT_RECYCLE_NUMBER;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        String res1;
        String res2;
        StringBuilder txtData = new StringBuilder();
        while(cursor.moveToNext()){
            res1 = cursor.getString(cursor.getColumnIndex("Amount"));
            res2 = cursor.getString(cursor.getColumnIndex(KEY_DICT_RECYCLE_NUMBER));
            txtData.append(res1+"                            "+res2+"\n");
        }
        cursor.close();
        return txtData;
    }







}
