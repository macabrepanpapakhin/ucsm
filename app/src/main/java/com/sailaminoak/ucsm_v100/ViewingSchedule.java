package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.transition.Scene;
import android.view.Display;
import android.view.View;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ViewingSchedule extends AppCompatActivity {
    CalendarView calendarView;
    GridView gridView;
    TextView selfstudyReminder,chooseOtherClass,todaySchedule;
    SharedPreferences sharedPreferences;
    String className="1CST";
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_schedule);
        calendarView=findViewById(R.id.calenderView);
        selfstudyReminder=findViewById(R.id.selfStudyReminder);
        chooseOtherClass=findViewById(R.id.chooseOtherClass);
        gridView=findViewById(R.id.grid_view);
        todaySchedule=findViewById(R.id.todayScheduleTextView);
        sharedPreferences =this.getSharedPreferences("UCSM", Context.MODE_PRIVATE);
        chooseOtherClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ViewingSchedule.this,chooseOtherClass.class));
            }
        });
        className=sharedPreferences.getString("className","1CST");
        loadingDialog=new LoadingDialog(this,true);
        loadingDialog.startAnimationDialog();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        Calendar cc = Calendar.getInstance();
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH);
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        cc.set(year,month,mDay);
        int today=cc.get(Calendar.DAY_OF_WEEK);
        String bitch="";
        switch (today){
            case 1:bitch="Sunday";
            break;
            case 2:bitch="Monday";break;
            case 3:bitch="Tuesday";break;
            case 4:bitch="Wednesday";break;
            case 5:bitch="Thursday";break;
            case 6:bitch="Friday";break;
            case 7:bitch="Saturday";break;
            default:
                bitch="the fuck day";
                break;
        }
        todaySchedule.setText("Today's Schedule("+bitch +")");


        database.getReference("Schedules").child(className).child(bitch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Schedule schedule=snapshot.getValue(Schedule.class);
                if(schedule!=null){
                    String[] schedules=schedule.subjects.split(",");
                   int count=0;
                   for(String s:schedules){
                       if(s.contentEquals("self study")){
                           count++;
                       }
                   }
                   if(count!=0){
                       selfstudyReminder.setText("* There is "+count+" self study. Be Productive During Self Study");
                   }else{
                       selfstudyReminder.setText("Be Productive!!!");
                   }
                   if(schedules.length==6){
                       ScheduleAdapter scheduleAdapter=new ScheduleAdapter(getApplicationContext(),schedules);
                       gridView.setAdapter(scheduleAdapter);
                       loadingDialog.closingAlertDialog();

                   }else{
                       DisplayToast("Invalid Schedules Format. Report to Admin Team");
                       loadingDialog.closingAlertDialog();
                   }

                }else{
                    DisplayToast("No data exit for that day.");
                    loadingDialog.closingAlertDialog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    DisplayToast("No data exits. Admin Team will fix soon.");
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                loadingDialog.startAnimationDialog();
                String date=dayOfMonth+"/"+(month+1)+"/"+year;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                String bitch1="";
                switch (dayOfWeek){
                    case 1:bitch1="Sunday";
                        break;
                    case 2:bitch1="Monday";break;
                    case 3:bitch1="Tuesday";break;
                    case 4:bitch1="Wednesday";break;
                    case 5:bitch1="Thursday";break;
                    case 6:bitch1="Friday";break;
                    case 7:bitch1="Saturday";break;
                    default:
                        bitch1="the fuck day?";
                        break;
                }
                String finalBitch = bitch1;
                FirebaseDatabase.getInstance().getReference("Schedules").child(className).child(bitch1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Schedule schedule=snapshot.getValue(Schedule.class);
                        if(schedule!=null){
                            String[] schedules=schedule.subjects.split(",");
                            int count=0;
                            for(String s:schedules){
                                if(s.contentEquals("self study")){
                                    count++;
                                }
                            }

                            if(schedules.length==6){
                                ScheduleAdapter scheduleAdapter=new ScheduleAdapter(getApplicationContext(),schedules);
                                gridView.setAdapter(scheduleAdapter);
                                if(count!=0){
                                    selfstudyReminder.setText("* There is "+count+" self study. Be Productive During Self Study");
                                }else{
                                    selfstudyReminder.setText("Be Productive!!!oh my god");
                                }
                                todaySchedule.setText(date+" 's Schedule ("+ finalBitch +")");
                                loadingDialog.closingAlertDialog();

                            }else{
                                DisplayToast("Invalid Schedules Format. Report to Admin Team");
                                loadingDialog.closingAlertDialog();
                            }

                        }else{
                            DisplayToast("No data exit for that day.");
                            loadingDialog.closingAlertDialog();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        DisplayToast("no data available. sorry");

                    }
                });
            }
        });
    }
    void DisplayToast(String msg){
        Toast.makeText(ViewingSchedule.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        loadingDialog.closingAlertDialog();
        finish();
        startActivity(new Intent(ViewingSchedule.this,MainActivity.class));
    }
}