package com.sailaminoak.ucsm_v100;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class CanteenInformation extends AppCompatActivity {
    LinearLayout linearLayout,academy,shwetaungtan,moemyittar,kyarnikan,tanmyitkyi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_information);
        linearLayout=findViewById(R.id.layout);
        academy=findViewById(R.id.academycanteen);
        shwetaungtan=findViewById(R.id.shwetaungtancanteen);
        moemyittar=findViewById(R.id.moemyittarcanteen);
        kyarnikan=findViewById(R.id.kyarnikancanteen);
        tanmyitkyi=findViewById(R.id.tanmyitkyicanteen);
        academy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(CanteenInformation.this,AcademyCanteen.class));

            }
        });
        shwetaungtan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(CanteenInformation.this,ShweTaungTan.class));

            }
        });
        moemyittar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayToast("Moe Myit Tar");
            }
        });
        kyarnikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayToast("Kyar Ni Kan");
            }
        });
        tanmyitkyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayToast("Tan Myit Kyi");
            }
        });



    }
    void DisplayToast(String msg){
        Toast.makeText(CanteenInformation.this,msg,Toast.LENGTH_SHORT).show();
    }
}