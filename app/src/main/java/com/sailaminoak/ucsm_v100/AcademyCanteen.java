package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AcademyCanteen extends AppCompatActivity {
    RatingBar ratingBar;
    Button rate;
    float total=0;
    int count=0;
    ImageView imageView;
    EditText availableItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_academy_canteen);
        ratingBar=findViewById(R.id.ratingBar);
        imageView=findViewById(R.id.canteenImage);
        availableItems=findViewById(R.id.availabeItems);
        FirebaseDatabase database=FirebaseDatabase.getInstance();
      try{  database.getReference("Canteens").child("Academy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Canteen canteen=snapshot.getValue(Canteen.class);
                    if(canteen!=null){
                        String image123=canteen.image;

                        if(image123.length()<=1000){
                           imageView.setImageResource(R.drawable.unknown);
                        }else{
                            byte[] b = Base64.decode(image123, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                            imageView.setImageBitmap(bitmap);
                        }
                        availableItems.setText(canteen.availableItems);


                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}catch (Exception e){
          imageView.setImageResource(R.drawable.unknown);
          availableItems.setText("Loading");
      }




      FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    try{
        firebaseDatabase.getReference("CanteenReview").child("Academy").addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              for(DataSnapshot ds:snapshot.getChildren()){
                  review review=ds.getValue(review.class);
                  if(review!=null){
                      count++;
                      total+=(Float.parseFloat(review.rateit));

                      ratingBar.setRating(total/count);

                  }
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });}catch (Exception e){
        ratingBar.setRating(5);
    }

        ratingBar.setEnabled(false);
        rate=findViewById(R.id.rate);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AcademyCanteen.this,GiveRatingToCanteen.class);
                intent.putExtra("t","Academy");
                startActivity(intent);
            }
        });

    }
    void DisplayToast(String msg){
        Toast.makeText(AcademyCanteen.this,msg,Toast.LENGTH_SHORT).show();
    }
}