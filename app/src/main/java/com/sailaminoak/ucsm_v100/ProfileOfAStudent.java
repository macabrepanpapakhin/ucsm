package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class ProfileOfAStudent extends AppCompatActivity {
    TextView mkpt,name,emailaddress,mark,about;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userId;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_of_a_student);
        loadingDialog=new LoadingDialog(ProfileOfAStudent.this);
        loadingDialog.startAnimationDialog();
        mkpt=findViewById(R.id.mkpt);
        name=findViewById(R.id.name);
        emailaddress=findViewById(R.id.emailaddress);
        mark=findViewById(R.id.mark);
        about=findViewById(R.id.about);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=firebaseUser.getUid();
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation,R.string.close_navigation);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        reference.child(userId).addListenerForSingleValueEvent((new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    mkpt.setText("MKPT-   " +userProfile.mkpt);
                    name.setText("Name-   "+userProfile.name);
                    emailaddress.setText("Email-   "+userProfile.email);
                    mark.setText("Mark-   "+userProfile.achievement);
                    about.setText("About-   "+userProfile.bio);
                    loadingDialog.closingAlertDialog();
                }else{
                    loadingDialog.closingAlertDialog();
                    Toast.makeText(ProfileOfAStudent.this,"Data Not Exit! Contact to Admin Team!!!",Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.closingAlertDialog();
                Toast.makeText(ProfileOfAStudent.this,"Your Data Not Exist!!! Contact to Admin Team",Toast.LENGTH_LONG).show();
            }
        }));
    }
}