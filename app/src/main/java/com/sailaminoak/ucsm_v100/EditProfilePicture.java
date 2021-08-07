package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfilePicture extends AppCompatActivity {
    de.hdodenhof.circleimageview.CircleImageView profilePictue;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userId;
    LoadingDialog loadingDialog;
    de.hdodenhof.circleimageview.CircleImageView proflePicture;
    String Default_Email_Address="macabrepanpapakhin@gmail.com";
    private static final int select_photo=100;
    private StorageReference storageReference;
    public Uri uriImage;
    Button buttonSave;
    public ImageView imageViewForProfilePicture;
    String newString="origina mkpt lol";
    ImageButton imageButton;
    private EditText editTextName,editTextEmail,editTextAbout;
    TextView sendEmailVerification;
    TextView sendResetPassword;
    TextView emailForVerification,emailForResetPassword;
    boolean jumpOrNot=true;
    String emailFromProfile="";
    String mkpt="",password="",mark="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_picture);
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
        loadingDialog=new LoadingDialog(EditProfilePicture.this);
        emailForResetPassword=findViewById(R.id.emailForResetPassword);
        emailForVerification=findViewById(R.id.emailForVerification);

        //String[] data_array=newString.split(",");
        //String mkpt=data_array[0];
        //String mark=data_array[1];
        //String password=data_array[2];
        //String emailFromProfile=data_array[3];
        imageButton=findViewById(R.id.backImageButton);
        sendEmailVerification=findViewById(R.id.sendEmailVerificationButton);
        sendResetPassword=findViewById(R.id.sendPasswordButton);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=firebaseUser.getUid();
        loadingDialog= new LoadingDialog(EditProfilePicture.this);
        loadingDialog.startAnimationDialog();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    mkpt=userProfile.mkpt;
                    mark=userProfile.achievement;
                    password=userProfile.password;
                    emailFromProfile=userProfile.email;
                    editTextAbout.setText(userProfile.bio);
                    editTextName.setText(userProfile.name);
                    editTextEmail.setText(userProfile.email);
                    emailForVerification.setText("("+userProfile.email+")");
                    emailForResetPassword.setText("("+userProfile.email+")");
                    Bitmap bitmap = null;
                    imageViewForProfilePicture=new ImageView(EditProfilePicture.this);
                    if(userProfile.image.length()<=1000){
                        imageViewForProfilePicture.setImageResource(R.drawable.unknown);

                    }else{
                        byte[] b = Base64.decode(userProfile.image, Base64.DEFAULT);
                         bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                        imageViewForProfilePicture.setImageBitmap(bitmap);

                    }

                    loadingDialog.closingAlertDialog();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Default_Email_Address=emailFromProfile;
                sendEmailResetPassword();

            }
        });
        sendEmailVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startAnimationDialog();
              if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()){
                  return;
              }
              verifyEmail();

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profilePictue=findViewById(R.id.profilePicture);

         editTextName=findViewById(R.id.editTextName);
         editTextEmail=findViewById(R.id.editTextEmail);
         editTextAbout=findViewById(R.id.editTextAbout);
        profilePictue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });
        buttonSave=findViewById(R.id.saveButton);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.startAnimationDialog();
                TestHelperMethod(mkpt,editTextName.getText().toString().trim(),editTextEmail.getText().toString().trim(),password,mark,editTextAbout.getText().toString());

              try{
                  if(Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()){
                      updateEmail(editTextEmail.getText().toString().trim(),emailFromProfile,password);
                      loadingDialog.closingAlertDialog();


                  }
              }catch (Exception e){
                  DisplayToast("error please");
              }
            }
        });



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
                if(bitmapImage==null){
                    profilePictue.setImageResource(R.drawable.unknown);
                }
                else{
                    proflePicture.setImageBitmap(bitmapImage);
                }
            }
        }
    }



    public void TestHelperMethod(String messyMkpt,String messyName,String messyEmail,String messyPassword,String messyMark,String messyAbout){
        if(!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString().trim()).matches()){
            editTextEmail.requestFocus();
            editTextEmail.setError("Invalid Email Address");
            return;
        }
        if(editTextName.getText().toString().trim().length()==0){
            editTextName.requestFocus();
            editTextName.setError("Invalid Name");
            return;
        }
        if(editTextAbout.getText().toString().trim().length()<=10){
            editTextAbout.requestFocus();
            editTextAbout.setError("About must have at least 10 characters");
            return;
        }
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
                    //Toast.makeText(EditProfilePicture.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();
                    //loadingDialog.closingAlertDialog();
                    //onBackPressed();
                }else{
                    //Toast.makeText(EditsProfilePicture.this,"Error Upload: 323!!",Toast.LENGTH_SHORT).show();
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
    private  void DisplayToast(String message){
        Toast.makeText(EditProfilePicture.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(EditProfilePicture.this,Profile.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }
    public void verifyEmail(){
        loadingDialog.startAnimationDialog();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(EditProfilePicture.this,"Sent Verification Email",Toast.LENGTH_LONG).show();
                            loadingDialog.closingAlertDialog();
                        }
                        else
                        {
                            loadingDialog.closingAlertDialog();
                            Toast.makeText(EditProfilePicture.this,"Undetectable Error, Contact to Admin Team!!!",Toast.LENGTH_LONG).show();
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
            Toast.makeText(EditProfilePicture.this, "Already Verified!!!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }
    public void updateEmail(String newEmail,String oldEmail,String oldPassword){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(oldEmail, oldPassword); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DisplayToast("Successfully updated");
                                            onBackPressed();
                                        }
                                        else{
                                            jumpOrNot=false;
                                            DisplayToast("already used email or unsuccessfully update contact to admin for information");
                                        }
                                    }
                                });

                    }
                });
    }
    public void sendEmailResetPassword(){
        loadingDialog.startAnimationDialog();
        FirebaseAuth.getInstance().sendPasswordResetEmail(Default_Email_Address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfilePicture.this, "Send Reset Password Link to "+Default_Email_Address, Toast.LENGTH_SHORT).show();
                    loadingDialog.closingAlertDialog();
                }else{
                    Toast.makeText(EditProfilePicture.this,"Undetectable Error",Toast.LENGTH_LONG).show();
                    loadingDialog.closingAlertDialog();
                }
            }

        });

    }

}