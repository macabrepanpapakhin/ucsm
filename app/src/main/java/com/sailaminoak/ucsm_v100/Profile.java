package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Profile extends AppCompatActivity{
    private EditText mkpt,name,emailaddress,mark,about,username,major,batch,participatedClub;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userId;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    LoadingDialog loadingDialog;
    de.hdodenhof.circleimageview.CircleImageView proflePicture;
    String Default_Email_Address="macabrepanpapakhin@gmail.com";
    private static final int select_photo=100;
    private StorageReference storageReference;
    public Uri uriImage;
    public String messyMkpt="";
    public String messyName="";
    public String messyEmail="";
    public String messyMark="";
    public String messyAbout="";
    public String messyImage="";
    public String messyPassword="";
    public ImageView imageViewForProfilePicture;
    public ImageView profileEdit;
    public ImageView signout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sharedPreferences =this.getSharedPreferences("UCSM", Context.MODE_PRIVATE);
        proflePicture=findViewById(R.id.profilePicture);
        proflePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });
        profileEdit=findViewById(R.id.profileEdit);
        signout=findViewById(R.id.signOut);
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent=new Intent(Profile.this,EditProfilePicture.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.stay);

            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FirebaseAuth.getInstance().signOut();

                 SharedPreferences.Editor editor=sharedPreferences.edit();
                 editor.putInt("AlreadyLogIn",0);
                 editor.apply();
                 finish();
                 Intent intent=new Intent(Profile.this,LoginPage.class);
                 startActivity(intent);

             }
        });
        //String previouslyEncodedImage = sharedPreferences.getString("image_data", "");
        loadingDialog=new LoadingDialog(Profile.this);
        loadingDialog.startAnimationDialog();
        username=findViewById(R.id.editTextUserName);
        major=findViewById(R.id.editTextMajor);
        batch=findViewById(R.id.editTextBatch);
        participatedClub=findViewById(R.id.editTextParticipatedClub);
        mkpt=findViewById(R.id.editTextMkpt);
        mkpt.setText("Loading");
        mkpt.setEnabled(false);
        name=findViewById(R.id.editTextName);
        name.setText("Loading");
        name.setEnabled(false);
        emailaddress=findViewById(R.id.editTextEmail);
        emailaddress.setEnabled(false);
        emailaddress.setText("Loading");
        mark=findViewById(R.id.editTextMark);
        mark.setEnabled(false);
        mark.setText("Loading");
        about=findViewById(R.id.editTextAbout);
        about.setEnabled(false);
        about.setText("Loading");
        drawerLayout=findViewById(R.id.drawer_layout);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toogle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_navigation,R.string.close_navigation);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        storageReference= FirebaseStorage.getInstance().getReference("ProfileImages");
        navigationView=findViewById(R.id.nav_profilemenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_verifyemail:
                        if(checkIfEmailVerified()){

                        }else{
                            verifyEmail();
                        }
                        break;
                    case R.id.nav_changepassword:
                        sendEmailResetPassword();
                        break;
                    case R.id.nav_changeemail:
                        startGallery();
                        Toast.makeText(Profile.this,"Choosing Image",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_messages:
                        Toast.makeText(Profile.this,"Directing to Messages",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_homeworktodo:
                        Toast.makeText(Profile.this,"Directing to HomeWork",Toast.LENGTH_SHORT).show();
                        break;


                }

                return false;
            }
        });

       try{
           firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=firebaseUser.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    loadingDialog.closingAlertDialog();
                    DisplayToast("Debugging Helper");
                    String MKPT=userProfile.mkpt;
                    String NAME=userProfile.name;
                    String EMAIL=userProfile.email;
                    String  MARK=userProfile.achievement;
                    String About=userProfile.bio;
                    String image123=userProfile.image;
                    messyMkpt=MKPT;
                    messyAbout=About;
                    messyEmail=EMAIL;
                    messyMark=MARK;
                    messyImage=image123;
                    messyName=NAME;
                    messyPassword=userProfile.password;
                    Default_Email_Address=EMAIL;
                    mkpt.setText("MKPT-   "+userProfile.getMkpt());
                    mkpt.setHint("");
                    mkpt.setTextColor(getResources().getColor(R.color.black));
                    mkpt.setTextSize(18);
                    mkpt.setEnabled(false);
                    name.setText("Name-   "+userProfile.getName());
                    name.setEnabled(false);
                    name.setHint("");
                    name.setTextColor(getResources().getColor(R.color.black));
                    name.setTextSize(18);
                    emailaddress.setText("Email-   "+userProfile.getEmail());
                    emailaddress.setHint("");
                    emailaddress.setEnabled(false);
                    emailaddress.setTextColor(getResources().getColor(R.color.black));
                    emailaddress.setTextSize(16);
                    mark.setText("Mark-   "+userProfile.getAchievement());
                    mark.setHint("");
                    mark.setEnabled(false);
                    mark.setTextColor(getResources().getColor(R.color.black));
                    mark.setTextSize(18);
                    about.setText("About-   "+userProfile.getBio());
                    about.setHint("");
                    about.setEnabled(false);
                    about.setTextColor(getResources().getColor(R.color.black));
                    about.setTextSize(18);
                    String previouslyEncodedImage1=image123;
                    if(image123.length()<=1000){
                        proflePicture.setImageResource(R.drawable.unknown);
                    }else{
                        byte[] b = Base64.decode(previouslyEncodedImage1, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        proflePicture.setImageBitmap(bitmap);
                    }
                    username.setText("username   "+userProfile.username);
                    username.setEnabled(false);
                    username.setTextSize(18);
                    username.setTextColor(getResources().getColor(R.color.black));
                    major.setText("major   "+userProfile.major);
                    major.setEnabled(false);
                    major.setTextSize(18);
                    major.setTextColor(getResources().getColor(R.color.black));
                    batch.setText("major   "+userProfile.batch);
                    batch.setEnabled(false);
                    batch.setTextSize(18);
                    batch.setTextColor(getResources().getColor(R.color.black));
                    participatedClub.setText("Participated Club   "+userProfile.participatedClub);
                    participatedClub.setEnabled(false);
                    participatedClub.setTextColor(getResources().getColor(R.color.black));
                    participatedClub.setTextSize(18);
                    loadingDialog.closingAlertDialog();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingDialog.closingAlertDialog();
                Toast.makeText(Profile.this,"Your Data Not Exist!!! Contact to Admin Team",Toast.LENGTH_LONG).show();
            }
        });

    }catch (Exception e){
           DisplayToast("fucking error thanks");
       }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
public void sendEmailResetPassword(){
        loadingDialog.startAnimationDialog();
        FirebaseAuth.getInstance().sendPasswordResetEmail(Default_Email_Address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Profile.this, "Send Reset Password Link to "+Default_Email_Address, Toast.LENGTH_SHORT).show();
                    loadingDialog.closingAlertDialog();
                }else{
                    Toast.makeText(Profile.this,"Undetectable Error",Toast.LENGTH_LONG).show();
                    loadingDialog.closingAlertDialog();
                }
            }

        });

}
public void verifyEmail(){
        loadingDialog.startAnimationDialog();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Profile.this,"Sent Verification Email",Toast.LENGTH_LONG).show();
                       loadingDialog.closingAlertDialog();
                    }
                    else
                    {
                        loadingDialog.closingAlertDialog();
                        Toast.makeText(Profile.this,"Undetectable Error, Contact to Admin Team!!!",Toast.LENGTH_LONG).show();
                        overridePendingTransition(0, 0);
                        overridePendingTransition(0, 0);
                    }
                }
            });
}
    private boolean checkIfEmailVerified()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.isEmailVerified())
        {
            Toast.makeText(Profile.this, "Already Verified!!!", Toast.LENGTH_SHORT).show();
            return true;
        }
      return false;

    }






    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model;
    }

    private void startGallery() {
        Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, 1000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                uriImage=data.getData();
                Bitmap bitmapImage = null;
                try {
                    bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageViewForProfilePicture=new ImageView(Profile.this);
                imageViewForProfilePicture.setImageBitmap(bitmapImage);
                proflePicture.setImageBitmap(bitmapImage);
                TestHelperMethod();


            }
        }
    }
    public void TestHelperMethod(){
        byte[] b = imageToByte(imageViewForProfilePicture);
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        String username="username";
        String major="se";
        String batch="2016";
        String participatedClub="Music Club,Tech Club";
        String verifyOrNot="true";

        User user=new User(messyMkpt,username,messyName,messyEmail,major,batch,messyPassword,messyMark,messyAbout,participatedClub,encodedImage,verifyOrNot);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Profile.this,"Uploaded ,and thanks",Toast.LENGTH_SHORT).show();
                    loadingDialog.closingAlertDialog();
                }else{
                    Toast.makeText(Profile.this,"Error Upload: 323!!",Toast.LENGTH_SHORT).show();
                    loadingDialog.closingAlertDialog();
                }
            }
        });
    }
    public byte[] imageToByte(ImageView image){
        Bitmap bitmap= ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,20,stream);
        return stream.toByteArray();
    }
private String getFileExtension(Uri uri){
    ContentResolver contentResolver=getContentResolver();
    MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
}
public void UploadImageToFirebase(){
        if(uriImage!=null){
            loadingDialog.startAnimationDialog();
            StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uriImage));
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadingDialog.closingAlertDialog();
                    Toast.makeText(Profile.this,"Successfully Uploaded to Firebase Storage",Toast.LENGTH_LONG).show();
                    UploadImage uploadImage=new UploadImage("Macabre Pan Pa Pa Khin",taskSnapshot.getUploadSessionUri().toString());
                    String uploadID=reference.push().getKey();
                    reference.child(uploadID).setValue(uploadImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingDialog.closingAlertDialog();
                    Toast.makeText(Profile.this,"Firebase Storage Error",Toast.LENGTH_LONG).show();
                }
            });

        }else{
            Toast.makeText(this,"No Files Selected",Toast.LENGTH_LONG).show();
        }
}
private void DisplayToast(String msg){
        Toast.makeText(Profile.this,msg,Toast.LENGTH_SHORT).show();
}

}