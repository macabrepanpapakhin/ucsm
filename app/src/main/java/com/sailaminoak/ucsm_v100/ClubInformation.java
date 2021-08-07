package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ClubInformation extends AppCompatActivity {
    LinearLayout linearLayout;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_information);
        linearLayout=findViewById(R.id.layout);
        loadingDialog=new LoadingDialog(this,true);
        loadingDialog.startAnimationDialog();
        FirebaseDatabase.getInstance().getReference("Clubs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sp:snapshot.getChildren()){
                    Club club=sp.getValue(Club.class);
                    if(club!=null){
                        TextView button=new TextView(ClubInformation.this);
                        button.setBackgroundResource(R.drawable.beautify_button);
                        button.setCompoundDrawablesWithIntrinsicBounds(
                              R.drawable.events, 0, 0, 0);
                        button.setGravity(Gravity.CENTER);
                        button.setPadding(2,2,2,2);
                        button.setText(club.name);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(20, 25, 20, 20);
                        button.setLayoutParams(layoutParams);
                        button.setOnClickListener((v -> {
                            Intent intent=new Intent(ClubInformation.this,ViewingClub.class);
                            intent.putExtra("t",club.getName());
                            startActivity(intent);
                        }));
                        linearLayout.addView(button);
                        loadingDialog.closingAlertDialog();

                    }else{
                        DisplayToast("Error undetectable");
                        loadingDialog.closingAlertDialog();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.closingAlertDialog();

            }
        });

    }
    void DisplayToast(String msg){
        Toast.makeText(ClubInformation.this,msg,Toast.LENGTH_SHORT).show();
    }
}