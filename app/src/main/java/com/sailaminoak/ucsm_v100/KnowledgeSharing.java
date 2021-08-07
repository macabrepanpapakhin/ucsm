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

public class KnowledgeSharing extends AppCompatActivity {
    LinearLayout linearLayout;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_sharing);
        linearLayout=findViewById(R.id.linearLayout);
        loadingDialog=new LoadingDialog(KnowledgeSharing.this);
        loadingDialog.startAnimationDialog();
        FirebaseDatabase.getInstance().getReference("CoursesName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot ds:snapshot.getChildren()){
                   CoursesName names=ds.getValue(CoursesName.class);
                   Button button=new Button(KnowledgeSharing.this);
                   button.setPadding(10,20,10,0);
                   LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                           LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                   layoutParams.setMargins(30, 25, 30, 20);
                   button.setLayoutParams(layoutParams);
                   button.setText(names.name);
                   button.setGravity(Gravity.LEFT);
                   button.setTextColor(getResources().getColor(R.color.darkTextColor));
                   button.setBackground(getDrawable(R.color.white));
                   button.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent intent=new Intent(KnowledgeSharing.this,subTitlePage.class);
                           intent.putExtra("t",names.name);
                           startActivity(intent);
                       }
                   });
                   linearLayout.addView(button);
                   loadingDialog.closingAlertDialog();

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    void DisplayToast(String msg){
        Toast.makeText(KnowledgeSharing.this,msg,Toast.LENGTH_SHORT).show();
    }
}