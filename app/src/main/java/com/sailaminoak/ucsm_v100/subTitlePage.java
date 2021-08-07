package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class subTitlePage extends AppCompatActivity {
    LinearLayout linearLayout;
    String newString="Programming Language";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_title_page);
        linearLayout=findViewById(R.id.layoutOfLinear);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("t");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("t");
        }


        FirebaseDatabase.getInstance().getReference("KnowledgeSharing").child(newString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    Button button=new Button(subTitlePage.this);
                button.setPadding(10,20,10,0);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 25, 30, 20);
                button.setLayoutParams(layoutParams);
                button.setText(ds.getKey().toString().trim());
                button.setGravity(Gravity.LEFT);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(subTitlePage.this,ReadingKnowledgeSharingPosts.class);
                        String combineString=newString+","+ds.getKey();
                        intent.putExtra("t",combineString);
                        startActivity(intent);
                    }
                });
                button.setTextColor(getResources().getColor(R.color.darkTextColor));
                button.setBackground(getDrawable(R.color.white));
                linearLayout.addView(button);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DisplayToast("Admins did not added any items.Please check later");
            }
        });
    }
    void DisplayToast(String msg){
        Toast.makeText(subTitlePage.this, msg, Toast.LENGTH_SHORT).show();
    }
}