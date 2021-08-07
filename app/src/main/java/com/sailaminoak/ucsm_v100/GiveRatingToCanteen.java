package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class GiveRatingToCanteen extends AppCompatActivity {
    RatingBar ratingBar;
    Button button;
    String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_rating_to_canteen);
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

        button=findViewById(R.id.submit);
        ratingBar=findViewById(R.id.ratingBar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rate=String.valueOf(ratingBar.getRating());
                doSomeFirebase(rate);
                //Toast.makeText(GiveRatingToCanteen.this,rate,Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void doSomeFirebase(String value){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        review review=new review(value);

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.getReference("CanteenReview").child(newString).child(id).setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(GiveRatingToCanteen.this,"success",Toast.LENGTH_SHORT).show();
            }
        });

    }
}