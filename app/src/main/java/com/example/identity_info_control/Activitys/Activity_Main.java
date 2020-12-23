package com.example.identity_info_control.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identity_info_control.Adapter.ItemAdapter;
import com.example.identity_info_control.Adapter.item;
import com.example.identity_info_control.DBHelper.MyDatabaseHelper;
import com.example.identity_info_control.OnCLickLinstener.MenuOnClickListener;
import com.example.identity_info_control.R;

import java.util.ArrayList;
import java.util.List;

public class Activity_Main extends AppCompatActivity {

    //调用自定义的itemList，每一项item都是名字name和图片的url
    private List<item> itemList = new ArrayList<>();

    //调用MyDatabaseHelper,用来打开并读取数据库
    private MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"identity_info.db", null, 1);

    //打开数据库，若没有则执行MyDatabaseHelper的onCreate方法创建
    private SQLiteDatabase db;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTitleUI();

        //检查存储权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
        }

        //搜索按钮的触发事件
        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_();
            }

        });

    }

    private void initTitleUI(){
        //隐藏原菜单
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        //标题栏按钮监听
        LinearLayout titleLayout = findViewById(R.id.title_linearlayout);
        db= dbHelper.getWritableDatabase();
        MenuOnClickListener mcl = new MenuOnClickListener(this, titleLayout);
        mcl.listenMenu();
    }

    private void search_(){
        EditText editText_search = findViewById(R.id.search);
        String str = editText_search.getText().toString();

//        //新建一个itemList2,用来存储搜索到的数据
//        List<item> itemList2 = new ArrayList<>();
        Cursor cursor;

        Log.d("main str", "onClick: "+str);

        //空输入时点击查询按钮显示所有联系人
        if (str.length()==0){
            //提交SQL查询语句,查询表的所有列，并按姓名排序
            cursor = db.rawQuery("select idnumber,name,imageurl from identity order by name", null);
        }else{
            //提交SQL查询语句,查询表的所有列，并按姓名排序
            cursor = db.rawQuery("select idnumber,name,imageurl from identity where name like '"
                    +str+"' or name regexp '.*"+str+".*' or idnumber like '"
                    +str+"' or idnumber regexp '.*"+str+".*'", null);
        }

        //读取返回的cursor并写入itemList
        itemList = getItemListFromCursor(cursor);
        ItemAdapter adapter = new ItemAdapter(Activity_Main.this,
                R.layout.item, itemList);
        listView.setAdapter(adapter);//写入ListView并显示
    }

    //请求权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED){

                }else{
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    //重写onResume方法，为了每次回到主界面重新读取数据
    @Override
    protected void onResume() {
        //清空itemList
        itemList.clear();

        //初始化数据，这是下面自定义的方法
        init_contacts();

        TextView tip = findViewById(R.id.textView8);
        if(itemList.size() == 0){
            tip.setVisibility(View.VISIBLE);
        }else{
            tip.setVisibility(View.GONE);
        }

        //下面是关于ListView的设置
        ItemAdapter adapter = new ItemAdapter(Activity_Main.this,
                R.layout.item, itemList);
        listView = findViewById(R.id.listview);//获取ListView组件
        listView.setAdapter(adapter);//写入ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Log.d("main", "onItemClick: "+position);
                //启动详细信息界面
                Intent intent = new Intent(Activity_Main.this, Activity_Detail.class);

                //传递点击的item的idNumber
                intent.putExtra("idNumber",itemList.get(position).getId());
                startActivity(intent);
            }
        });
        super.onResume();
    }

    //初始化联系人数据
    private void init_contacts() {

        //提交SQL查询语句,查询contacts表的所有列，并按姓名排序
        Cursor cursor = db.rawQuery("select idnumber,name,imageurl from identity order by name", null);

        //读取返回的cursor并写入itemList
        itemList = getItemListFromCursor(cursor);
    }

    private List<item> getItemListFromCursor(Cursor cursor){
        List<item> itemList_ = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("idnumber"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("imageurl"));

                item man = new item(id,name,imageUrl);
                itemList_.add(man);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemList_;
    }

    //启动次数计数器
    private int startTimeCounter(){
        SharedPreferences userInfo = getSharedPreferences("start", MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        int x;//获取记录启动次数的值，若获取不到就默认为1
        x=userInfo.getInt("start",1);
        //判断第几次启动
        if(x==1)
        {
            Intent intent_welcome = new Intent(this, Activity_Information.class);
            intent_welcome.putExtra("info", "help");
        }
        editor.putInt("start",++x);//为启动数加一
        editor.apply();
        return --x;
    }
    //重写目录菜单
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main,menu);//加载目录布局文件res/menu/main.xml
//        return true;
//    }

    //设置菜单点击事件
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.add:
//
//                //跳转到添加联系人页面，并结束本页面
//                Intent intent = new Intent(Activity_Main.this, Activity_Add.class);
//                startActivity(intent);
//                finish();
//                Toast.makeText(this, "You clicked add", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.help:
//
//                //跳转到添加联系人页面，并结束本页面
//                Intent intent2 = new Intent(Activity_Main.this, Activity_Information.class);
//                intent2.putExtra("info","help");
//                startActivity(intent2);
//                finish();
//                Toast.makeText(this, "You clicked help", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.about:
//
//                //跳转到添加联系人页面，并结束本页面
//                Intent intent3 = new Intent(Activity_Main.this, Activity_Information.class);
//                intent3.putExtra("info","about");
//                startActivity(intent3);
//                finish();
//                Toast.makeText(this, "You clicked about", Toast.LENGTH_SHORT).show();
//                break;
//
//            default:
//                break;
//        }
//        return true;
//    }
}
