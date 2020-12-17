package com.example.address_book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Activity_Main extends AppCompatActivity {

    //调用自定义的itemList，每一项item都是名字name和图片的id imageId
    private List<item> itemList = new ArrayList<>();

    //调用MyDatabaseHelper,用来打开并读取数据库
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"identity_info.db", null, 1);

    //这三行是创建活动，Android Studio自动生成
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //打开数据库，若没有则执行MyDatabaseHelper的onCreate方法创建
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        //初始化数据，这是下面自定义的方法
        init_contacts();

        //下面是关于ListView的设置
        ItemAdapter adapter = new ItemAdapter(Activity_Main.this,
                R.layout.item, itemList);
        ListView listView = findViewById(R.id.listview);//获取ListView组件
        listView.setAdapter(adapter);//写入ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Log.d("main", "onItemClick: "+position);
                //启动详细信息界面
                Intent intent = new Intent(Activity_Main.this, Activity_Detail.class);

                //传递点击的item的姓名
                intent.putExtra("name",itemList.get(position).getName());
                startActivity(intent);
            }
        });

        //搜索按钮的触发事件
        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText_search = findViewById(R.id.search);
                String str = editText_search.getText().toString();

                //新建一个itemList2,用来存储搜索到的数据
                List<item> itemList2 = new ArrayList<>();
                Cursor cursor;

                Log.d("main str", "onClick: "+str);

                //空输入时点击查询按钮显示所有联系人
                if (str.length()==0){
                    //提交SQL查询语句,查询表的所有列，并按姓名排序
                    cursor = db.rawQuery("select name from identity order by name", null);
                }else{
                    //提交SQL查询语句,查询表的所有列，并按姓名排序
                    cursor = db.rawQuery("select name,imageurl from identity where name like '"
                            +str+"' or name regexp '.*"+str+".*'", null);
                }

                //读取返回的cursor并写入itemList
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String imageUrl = cursor.getString(cursor.getColumnIndex("imageurl"));
                        item man = new item(name,imageUrl);
                        itemList2.add(man);//将查询结果写入itemList2
                    } while (cursor.moveToNext());
                }
                cursor.close();
                ItemAdapter adapter = new ItemAdapter(Activity_Main.this,
                        R.layout.item, itemList2);
                ListView listView = findViewById(R.id.listview);//获取ListView组件
                listView.setAdapter(adapter);//写入ListView并显示
            }

        });

    }

    //初始化联系人数据
    private void init_contacts() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();//若数据库已存在则打开，否则创建一个数据库

        //导入的四张头像图片的第一张id
        int img_id = 2131099746;

        //提交SQL查询语句,查询contacts表的所有列，并按姓名排序
        Cursor cursor = db.rawQuery("select name,imageurl from identity order by name", null);

        //读取返回的cursor并写入itemList
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("imageurl"));
//                String phone = cursor.getString(cursor.getColumnIndex("phone"));
//                String company = cursor.getString(cursor.getColumnIndex("company"));
//                String email = cursor.getString(cursor.getColumnIndex("email"));
//                String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
//                String note = cursor.getString(cursor.getColumnIndex("note"));
//                Log.d("mainActivity", "init_contacts: "+R.drawable.img2);
                item man = new item(name,imageUrl);
                itemList.add(man);
                //设置循环使用四张头像图片
                if (img_id>2131099749){
                    img_id=2131099746;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    //重写目录菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);//加载目录布局文件res/menu/main.xml
        return true;
    }

    //设置菜单点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:

                //跳转到添加联系人页面，并结束本页面
                Intent intent = new Intent(Activity_Main.this, Activity_Add.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "You clicked add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:

                //跳转到添加联系人页面，并结束本页面
                Intent intent2 = new Intent(Activity_Main.this, InformationActivity.class);
                startActivity(intent2);
                finish();
                Toast.makeText(this, "You clicked help", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:

                //跳转到添加联系人页面，并结束本页面
                Intent intent3 = new Intent(Activity_Main.this, InformationActivity.class);
                startActivity(intent3);
                finish();
                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }
    
    
}
