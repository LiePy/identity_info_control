package com.example.identity_info_control.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identity_info_control.DBHelper.MyDatabaseHelper;
import com.example.identity_info_control.OnCLickLinstener.MenuOnClickListener;
import com.example.identity_info_control.R;

import java.io.InputStream;

public class Activity_Detail extends AppCompatActivity {
    private TextView title_text,name_;
    private ImageView image;
    private ImageView imageView;
    private EditText idNumber_,address_,email_,phone_,note_;
    private Button back_button,update_button,delete_button;


    private Dialog dialog;
    private String imageUrl2,id;
    private String name = null,phone = null,address = null,email = null,idNumber = null,note = null;

    //调用MyDatabaseHelper,用来打开并读取数据库
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"identity_info.db", null, 1);
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findAllView();

        initTitleUI();

        loadInfo();

        init();

        //小图的点击事件（弹出大图）
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        //设置返回按钮的触发事件
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//关闭本页面就是返回
            }
        });

        //设置删除按钮的触发事件
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo();
                finish();
            }
        });

        //设置修改按钮的触发事件
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeInfo();
                finish();//结束本页面
            }
        });
    }

    //找到所有组件实例
    private void findAllView(){
        title_text = findViewById(R.id.textView_title);
        imageView = findViewById(R.id.image_head);
        name_ = findViewById(R.id.name);
        idNumber_ = findViewById(R.id.idNumber);
        address_ = findViewById(R.id.address);
        email_ = findViewById(R.id.email);
        phone_ = findViewById(R.id.phone);
        note_ = findViewById(R.id.note);
        back_button = findViewById(R.id.back_button);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
    }

    private void initTitleUI(){
        //隐藏原标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        //标题栏按钮监听
        LinearLayout titleLayout = findViewById(R.id.title_linearlayout);
        MenuOnClickListener mcl = new MenuOnClickListener(this, titleLayout);
        mcl.listenMenu();

        title_text.setText("详细信息");
    }

    private void loadInfo(){
        Intent intent = getIntent();
        id = intent.getStringExtra("idNumber");

        //打开数据库，若没有则执行MyDatabaseHelper的onCreate方法创建
        db = dbHelper.getWritableDatabase();

        //提交SQL查询语句,查询contacts表的所有列，并按姓名排序
        Cursor cursor = db.rawQuery("select * from identity where idnumber='"+id+"'", null);

        //读取返回的cursor并写入itemList
        if (cursor.moveToFirst()) {
//            do {
            name = cursor.getString(cursor.getColumnIndex("name"));
            phone = cursor.getString(cursor.getColumnIndex("phone"));
            address = cursor.getString(cursor.getColumnIndex("address"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            idNumber = cursor.getString(cursor.getColumnIndex("idnumber"));
            note = cursor.getString(cursor.getColumnIndex("note"));
            imageUrl2 = cursor.getString(cursor.getColumnIndex("imageurl"));
//                Log.d("mainActivity", "init_contacts: "+R.drawable.img2);
//            } while (cursor.moveToNext());
        }
        cursor.close();

        if(imageUrl2.equals("null")){
            imageView.setImageResource(R.drawable.img3);
        }else{
            imageView.setImageURI(Uri.parse(imageUrl2));
        }
        name_.setText(name);
        idNumber_.setText(idNumber);
        address_.setText(address);
        email_.setText(email);
        phone_.setText(phone);
        note_.setText(note);
    }

    private void init() {
        //展示在dialog上面的大图
        dialog = new Dialog(this,R.style.FullActivity);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(attributes);

        image = getImageView();
        dialog.setContentView(image);

        //大图的点击事件（点击让他消失）
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void changeInfo(){
        //转到添加数据页面，并附带当前数据信息
        Intent intent = new Intent(Activity_Detail.this, Activity_Add.class);
        intent.putExtra("info","change");
        intent.putExtra("name", name);
        intent.putExtra("phone", phone);
        intent.putExtra("addr", address);
        intent.putExtra("email", email);
        intent.putExtra("idnumber", idNumber);
        intent.putExtra("imageUrl", imageUrl2);
        startActivity(intent);
    }

    //从数据库中删除
    private void deleteInfo(){
        db.execSQL("delete from identity where idnumber='"+id+"'");

        Toast.makeText(getBaseContext(),"删除成功！",Toast.LENGTH_SHORT).show();
    }

    private ImageView getImageView(){
        ImageView imageView = new ImageView(this);

        //宽高
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        //imageView设置图片
//        @SuppressLint("ResourceType") InputStream is = getResources().openRawResource(R.drawable.img3);
//
//        Drawable drawable = BitmapDrawable.createFromStream(is, null);
//        imageView.setImageDrawable(drawable);
        if(imageUrl2.equals("null")){
            imageView.setImageResource(R.drawable.img3);
        }else{
            imageView.setImageURI(Uri.parse(imageUrl2));
        }
        return imageView;
    }
}
