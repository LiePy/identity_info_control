package com.example.identity_info_control.OnCLickLinstener;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.identity_info_control.Layout.TitleLayout;
import com.example.identity_info_control.R;

public class MenuOnClickListener {
    private static LinearLayout back_btn;
    private static LinearLayout menu_btn;
    private static Context context_;

    public MenuOnClickListener(Context context, LinearLayout titleLayout){
        context_ = context;
        back_btn = titleLayout.findViewById(R.id.back_layout);
        menu_btn = titleLayout.findViewById(R.id.menu_layout);
    }

    public static void listenMenu(){
        //标题栏按钮监听
        TitleLayout tl = new TitleLayout(context_);
        back_btn.setOnClickListener(tl);
        menu_btn.setOnClickListener(tl);
    }

}
