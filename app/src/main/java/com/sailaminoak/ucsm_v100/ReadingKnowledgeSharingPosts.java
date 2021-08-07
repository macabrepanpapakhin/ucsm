package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReadingKnowledgeSharingPosts extends AppCompatActivity {
    String subTitle="Java";
    String headerTitle="Programming Languages";
    TextView subTitleTextView,mainTextView;
    String newString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_knowledge_sharing_posts);
        subTitleTextView=findViewById(R.id.subTitle);
        mainTextView=findViewById(R.id.mainCharacter);

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
        DisplayToast(newString);
        String heritage[]=newString.split(",");
        headerTitle=heritage[0];
        subTitle=heritage[1];
        subTitleTextView.setText(subTitle);
        FirebaseDatabase.getInstance().getReference("KnowledgeSharing").child(headerTitle).child(subTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sharingPost sharingPost=snapshot.getValue(sharingPost.class);
                if(sharingPost!=null){
                    mainTextView.setText(sharingPost.sentences);
                }else{
                   mainTextView.setText("it is obvious Admin didn't added data.so shut your mind. I knwo this text never be show to user so i write shit");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
    DisplayToast("Admin not added data.Please check later");
            }
        });
    }
    void  DisplayToast(String msg){
        Toast.makeText(ReadingKnowledgeSharingPosts.this,msg,Toast.LENGTH_SHORT).show();
    }
}