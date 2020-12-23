package com.example.identity_info_control.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.identity_info_control.OnCLickLinstener.MenuOnClickListener;
import com.example.identity_info_control.R;

public class Activity_Information extends AppCompatActivity {
    final String helpText="1.添加数据\n" +
            "\r\r点击右上角“菜单”，弹出菜单选项，点击“添加数据”转到数据添加页面。\n" +
            "  1.1自定义照片：\n" +
            "\r\r在“添加数据”界面点击上方图片区域可选择本地图片。\n" +
            "  1.2完善信息要求：\n" +
            "\r\r根据提示填写信息，其中“姓名”和“身份证号”必须填写，其他为可选。\n" +
            "  1.3保存数据：\n" +
            "\r\r填写完毕后点击下方“保存”按钮即可保存数据。\n\n" +
            "2.查看详细数据\n" +
            "\r\r在主界面找到要查看的数据，点击即可进入“详细信息”页面，在此页面可以删除和转到修改信息页面。\n" +
            "\r\r在此页面点击上方照片区域可放大查看照片。\n\n" +
            "3.修改数据\n" +
            "  3.1打开方式：\n" +
            "\r\r在“详细信息”页面点击“修改”即可转到“修改信息”页面。\n" +
            "  3.2修改信息限制：" +
            "\r\r“身份证号”作为数据的主键，不可修改，其他均可修改。\n" +
            "  3.3放弃或保存修改：" +
            "\r\r若放弃修改，点击左上角标题栏的“返回”即可；若确定保存修改，点击下方“保存”按钮即可。\n\n" +
            "4.删除数据\n" +
            "\r\r在“详细信息”页面点击“删除”按钮即可删除本条数据。\n\n" +
            "5.查找数据\n" +
            "  5.1打开方式：" +
            "\r\r在主界面上方的搜索框内输入想要查找的信息，再点击右边的搜索按钮即可。\n" +
            "  5.2搜索条件限制：\n" +
            "\r\r支持按“姓名”和“身份证号”搜索。支持模糊搜索和部分搜索\n";

    final String aboutText="版本号：v1.2.0\n" +
            "作者：王磊\n" +
            "发布日期：2020.12.18";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        //隐藏原标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        //标题栏按钮监听
        LinearLayout titleLayout = findViewById(R.id.title_linearlayout);
        MenuOnClickListener mcl = new MenuOnClickListener(this, titleLayout);
        mcl.listenMenu();

        final TextView title_text = findViewById(R.id.textView_title);
        final TextView textMain = findViewById(R.id.text_main);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if (info .equals("help")){
            title_text.setText("帮助信息");
            textMain.setText(helpText);
            textMain.setMovementMethod(ScrollingMovementMethod.getInstance());
        }else if(info.equals("about")){
            title_text.setText("关于信息");
            textMain.setText(aboutText);
            textMain.setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected void onStop() {
        finish();   //这一步使在该页面跳转到其他页面后自动销毁
        super.onStop();
    }
}