package com.example.identity_info_control.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.identity_info_control.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<item> {
    private int resourceId;

    public ItemAdapter(Context context, int textViewResourceId,
                       List<item> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    //重写View getView方法，用于加载新进入屏幕的列表项的显示
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        item item = getItem(position);//获取当前item实例

        View view;
        ViewHolder viewHolder;
        //这是优化性能的一步，如果有该子项的缓存，则直接调用缓存，若没有则需要加载
        if (convertView == null){
            //加载传入的布局
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image = view.findViewById(R.id.image);
            viewHolder.name = view.findViewById(R.id.name);
            view.setTag(viewHolder);//将ViewHolder存储在View中
        }else{
            //调用缓存
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
        }

        //获取图片显示窗口和文本窗口并设置显示的内容

        if (item.getImageId().equals("null")){
            viewHolder.image.setImageResource(R.drawable.img3);
        }else{
            viewHolder.image.setImageURI(Uri.parse(item.getImageId()));
        }
        viewHolder.name.setText(item.getName());

        return view;
    }
}

//建这个类为了优化ItemAdapter的 View getView,避免直接调用缓存时多余的findViewById
class ViewHolder{
    ImageView image;
    TextView name;
}
