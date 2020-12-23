package com.example.identity_info_control.Layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identity_info_control.Activitys.Activity_Add;
import com.example.identity_info_control.Activitys.Activity_Information;
import com.example.identity_info_control.Activitys.Activity_Main;
import com.example.identity_info_control.R;


public class TitleLayout extends LinearLayout implements View.OnClickListener{
    private static Context context;
    private LinearLayout textview_back;
    private LinearLayout textview_menu;

    public TitleLayout(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.title, this);
        textview_back = findViewById(R.id.back_layout);
        textview_menu = findViewById(R.id.menu_layout);
        textview_back.setOnClickListener(this);
        textview_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_layout:
//                Toast.makeText(getContext().getApplicationContext(),"t7",Toast.LENGTH_SHORT).show();
                ((Activity)getContext()).onBackPressed();
                break;
            case R.id.menu_layout:
//                Toast.makeText(getContext().getApplicationContext(),"功能暂未开放",Toast.LENGTH_SHORT).show();
                showPopupMenu(v);
                break;
            default:

        }
    }

    //菜单栏子项目
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(context, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.add:

                        //跳转到添加联系人页面，并结束本页面
                        Intent intent = new Intent(context, Activity_Add.class);
                        context.startActivity(intent);
//                        finish();
//                        Toast.makeText(context, "You clicked add", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.help:

                        //跳转到帮助页面，并结束本页面
                        Intent intent2 = new Intent(context, Activity_Information.class);
                        intent2.putExtra("info", "help");
                        context.startActivity(intent2);
//                        finish();
//                        Toast.makeText(context, "You clicked help", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.about:

                        //跳转到关于页面
                        Intent intent3 = new Intent(context, Activity_Information.class);
                        intent3.putExtra("info", "about");
                        context.startActivity(intent3);
//                        finish();
//                        Toast.makeText(context, "You clicked about", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(context, "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }
}
