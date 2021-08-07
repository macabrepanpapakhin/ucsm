package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewingStudentData extends AppCompatActivity {
    String newString;
    private EditText mkpt,name,emailaddress,mark,about,username,batch,major,participatedClub;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    de.hdodenhof.circleimageview.CircleImageView proflePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_student_data);
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
        loadingDialog=new LoadingDialog(ViewingStudentData.this);
        loadingDialog.startAnimationDialog();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        proflePicture=findViewById(R.id.profilePicture);
        mkpt = findViewById(R.id.editTextMkpt);
        mkpt.setText("Loading");
        mkpt.setEnabled(false);
        username=findViewById(R.id.editTextUserNamet);
        username.setText("Loading");
        username.setEnabled(false);
        batch=findViewById(R.id.editTextBatch);
        batch.setText("Loading");
        batch.setEnabled(false);
        major=findViewById(R.id.editTextMajor);
        major.setEnabled(false);
        major.setText("Loading");
        participatedClub=findViewById(R.id.editTextParticipatedClub);
        participatedClub.setText("Loading");
        participatedClub.setEnabled(false);
        name = findViewById(R.id.editTextName);
        name.setText("Loading");
        name.setEnabled(false);
        emailaddress = findViewById(R.id.editTextEmail);
        emailaddress.setEnabled(false);
        emailaddress.setText("Loading");
        mark = findViewById(R.id.editTextMark);
        mark.setEnabled(false);
        mark.setText("Loading");
        about = findViewById(R.id.editTextAbout);
        about.setEnabled(false);
        about.setText("Loading");
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("mkpt").equalTo(newString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user=snapshot.getValue(User.class);
                    mkpt.setText(user.mkpt);
                    name.setText(user.name);
                    emailaddress.setText(user.email);
                    mark.setText(user.achievement);
                    about.setText(user.bio);
                    username.setText(user.username);
                    major.setText(user.major);
                    batch.setText(user.batch);
                    participatedClub.setText(user.participatedClub);
                    if(user.image.length()<=1000){
                        proflePicture.setImageResource(R.drawable.unknown);
                    }else{

                    byte[] b = Base64.decode(user.image, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                    proflePicture.setImageBitmap(bitmap);
                    }
                    loadingDialog.closingAlertDialog();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.closingAlertDialog();
                    DisplayToast("No Data Exit Anymore");
            }


        });
    }
    public void DisplayToast(String somefrickingtext){
        Toast.makeText(ViewingStudentData.this,somefrickingtext,Toast.LENGTH_SHORT).show();
    }
}