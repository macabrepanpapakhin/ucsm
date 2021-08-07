package com.sailaminoak.ucsm_v100;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScheduleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String[] gridviewdata;
    public ScheduleAdapter(Context context,String[] gridviewdata){
        this.context=context;
        this.gridviewdata=gridviewdata;
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
            convertView=layoutInflater.inflate(R.layout.rowforshedule,null);
        }
        TextView textView=convertView.findViewById(R.id.text_view);
        textView.setText(gridviewdata[position]);
        return convertView;
    }
}
