package com.sailaminoak.ucsm_v100;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdaper extends BaseAdapter {
private Context context;
private LayoutInflater layoutInflater;
private String[] gridviewdata;
private int[] images;
public MainAdaper(Context context,String[] gridviewdata,int[] images){
    this.context=context;
    this.gridviewdata=gridviewdata;
    this.images=images;
}

    @Override
    public int getCount() {
        return gridviewdata.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(layoutInflater==null
        ){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.row,null);
        }
        ImageView imageView=convertView.findViewById(R.id.image_view);
        TextView textView=convertView.findViewById(R.id.text_view);
        imageView.setImageResource(images[position]);
        textView.setText(gridviewdata[position]);
        return convertView;

    }
}
