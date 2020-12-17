package com.example.address_book;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Activity_Add extends AppCompatActivity {

    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"identity_info.db", null, 1);
    private static ImageView imageView;
    private static String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final SQLiteDatabase db = dbHelper.getWritableDatabase();//若数据库已存在则打开，否则创建一个数据库

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, 0x1);
            }
        });

        Button add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取所有输入框实例
                EditText name = findViewById(R.id.editText);
                EditText idNumber = findViewById(R.id.editText2);
                EditText company = findViewById(R.id.editText3);
                EditText email = findViewById(R.id.editText4);
                EditText phone = findViewById(R.id.editText5);
                EditText note = findViewById(R.id.editText6);

                //获取所有输入框里的文字
                String name_ = name.getText().toString();
                String phone_ = phone.getText().toString();
                String company_ = company.getText().toString();
                String email_ = email.getText().toString();
                String idNumber_ = idNumber.getText().toString();
                String note_ = note.getText().toString();

                //提交SQL语句，保存联系人信息到contacts表
                db.execSQL("insert into identity(name,phone,company,email,idnumber,note,imageurl) values(" +
                        "'"+name_+"','"+phone_+"','"+company_+"','"+email_+"','"+idNumber_+"','"+note_+"','"+imageUrl+"')");

                //提示“保存成功”
                Toast.makeText(getBaseContext(),"保存成功！",Toast.LENGTH_SHORT).show();

                //自动返回到主浏览页面
                Intent intent = new Intent(Activity_Add.this, Activity_Main.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
                imageUrl = data.getData().toString();
                imageView.setImageURI(data.getData());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
