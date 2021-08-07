package com.sailaminoak.ucsm_v100;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class chooseOtherClass extends AppCompatActivity {
    GridView gridView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_other_class);
        gridView=findViewById(R.id.grid_view1);
        sharedPreferences =this.getSharedPreferences("UCSM", Context.MODE_PRIVATE);

        String[] classes={"1CST","2CS","2CT","3CS","3CT","4SE","4BIS","4HPC","4NT","4EB","5SE","5HPC","5BIS","5EB","5NT"};
        ScheduleAdapter scheduleAdapter=new ScheduleAdapter(getApplicationContext(),classes);
        gridView.setAdapter(scheduleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("className","1CST");
                    editor.apply();
                    DisplayToast("applied to 1CST and it be default");
                    finish();
                    startActivity(new Intent(chooseOtherClass.this,ViewingSchedule.class));
                }else{
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("className","2CS");
                    editor.apply();
                    DisplayToast("applied to 2CS and it be default");
                    finish();
                    startActivity(new Intent(chooseOtherClass.this,ViewingSchedule.class));
                }
            }
        });
    }
    void DisplayToast(String msg){
        Toast.makeText(chooseOtherClass.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(chooseOtherClass.this,MainActivity.class));
    }
}