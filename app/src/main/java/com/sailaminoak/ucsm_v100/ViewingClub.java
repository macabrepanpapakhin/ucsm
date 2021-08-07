package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewingClub extends AppCompatActivity {
    ImageView imageView;
    Button apply;
    EditText about;
    String newString;
    ImageButton notificationButton;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_club);
        imageView=findViewById(R.id.imageView);
        apply=findViewById(R.id.apply);
        about=findViewById(R.id.about);
        notificationButton=findViewById(R.id.notificationButton);
        loadingDialog=new LoadingDialog(this,false);
        loadingDialog.startAnimationDialog();

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        FirebaseDatabase.getInstance().getReference("Clubs").orderByChild("name").equalTo(newString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              for(DataSnapshot ds:snapshot.getChildren()){
                  Club club=ds.getValue(Club.class);
                  if(club!=null){
                      byte[] b = Base64.decode(club.image, Base64.DEFAULT);
                      Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                      imageView.setImageBitmap(bitmap);
                      about.setText(club.about);
                      loadingDialog.closingAlertDialog();
                  }else{
                      DisplayToast("no club information available");
                      loadingDialog.closingAlertDialog();
                  }



              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.closingAlertDialog();

            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayToast("Under Development");
            }
        });
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationButton.setImageResource(R.drawable.ic_baseline_notifications_off_24);
                DisplayToast("Under Development");
            }
        });
    }
    void DisplayToast(String msg){
        Toast.makeText(ViewingClub.this,msg,Toast.LENGTH_SHORT).show();
    }
}