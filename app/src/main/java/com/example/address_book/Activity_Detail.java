package com.example.address_book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Detail extends AppCompatActivity {

    //调用MyDatabaseHelper,用来打开并读取数据库
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"identity_info.db", null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String name = intent.getStringExtra("name");

        //打开数据库，若没有则执行MyDatabaseHelper的onCreate方法创建
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        //提交SQL查询语句,查询contacts表的所有列，并按姓名排序
        Cursor cursor = db.rawQuery("select * from identity where name='"+name+"'", null);

        String phone = null,company = null,email = null,idNumber = null,note = null,imageUrl = null;
        //读取返回的cursor并写入itemList
        if (cursor.moveToFirst()) {
//            do {
//                String name = cursor.getString(cursor.getColumnIndex("name"));
            phone = cursor.getString(cursor.getColumnIndex("phone"));
            company = cursor.getString(cursor.getColumnIndex("company"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            idNumber = cursor.getString(cursor.getColumnIndex("idnumber"));
            note = cursor.getString(cursor.getColumnIndex("note"));
            imageUrl = cursor.getString(cursor.getColumnIndex("imageurl"));
//                Log.d("mainActivity", "init_contacts: "+R.drawable.img2);
//            } while (cursor.moveToNext());
        }
        cursor.close();

        //找到所有TextView组件实例
        ImageView imageView = findViewById(R.id.image_head);
        TextView name_ = findViewById(R.id.name);
        final EditText idNumber_ = findViewById(R.id.idNumber);
        final EditText company_ = findViewById(R.id.company);
        final EditText email_ = findViewById(R.id.email);
        final EditText phone_ = findViewById(R.id.phone);
        final EditText note_ = findViewById(R.id.note);

        imageView.setImageURI(Uri.parse(imageUrl));
        name_.setText(name);
        idNumber_.setText(idNumber);
        company_.setText(company);
        email_.setText(email);
        phone_.setText(phone);
        note_.setText(note);

        //设置返回按钮的触发事件
        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭本页面就是返回
            }
        });

        //设置删除按钮的触发事件
        Button delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从数据库中删除
                db.execSQL("delete from identity where name='"+name+"'");

                //文字提示删除成功
                Toast.makeText(getBaseContext(),"删除成功！",Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(Activity_Detail.this, Activity_Main.class);
                startActivity(intent1);//重新回到主页面，相当于刷新了主页面
            }
        });

        //设置修改按钮的触发事件
        Button update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_new,company_new,idNumber_new,note_new,email_new;
                phone_new = phone_.getText().toString();
                company_new = company_.getText().toString();
                idNumber_new = idNumber_.getText().toString();
                note_new = note_.getText().toString();
                email_new = email_.getText().toString();

                //先删除原先的数据
                db.execSQL("delete from identity where name='"+name+"'");

                //重新保存新的联系人信息到contacts表
                db.execSQL("insert into identity(name,phone,company,email,birthday,note) values(" +
                        "'"+name+"','"+phone_new+"','"+company_new+"','"+email_new+"','"+idNumber_new+"','"+note_new+"')");

                //提示“保存成功”
                Toast.makeText(getBaseContext(),"修改成功！",Toast.LENGTH_SHORT).show();

                //自动返回到主浏览页面
                Intent intent = new Intent(Activity_Detail.this, Activity_Main.class);
                startActivity(intent);
                finish();

            }
        });

    }
}
