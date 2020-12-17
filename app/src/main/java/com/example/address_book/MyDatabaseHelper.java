package com.example.address_book;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //定义创建表的SQL语句
    public static final String CREAT_TABLE = "create table identity ("
            + "id integer primary key autoincrement, "//主键，序号
            + "imageurl text, "//图片地址
            + "note text, "//备注
            + "idnumber text, "//身份证号
            + "email text, "//邮箱
            + "company text, "//公司
            + "phone text, "//电话
            + "name text)";//姓名


    private Context mContext;

    public MyDatabaseHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    //
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREAT_TABLE);//创建表
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists identity");//更新，如果存在表则删除
        onCreate(db);
    }
}
