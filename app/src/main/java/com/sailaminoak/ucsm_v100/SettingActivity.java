package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    TextView verify,changePassword,changeEmail,resetPassword,logout;
    boolean isOfficeMail=false;
    LoadingDialog loadingDialog;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String userId;
    String Default_Email_Address="macabrepanpapakhin@gmail.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        verify=findViewById(R.id.verifyWithOfficeMail);
        changePassword=findViewById(R.id.changePassword);
        changeEmail=findViewById(R.id.changeEmail);
        resetPassword=findViewById(R.id.resetPassword);
        logout=findViewById(R.id.logOut);
        loadingDialog=new LoadingDialog(SettingActivity.this);
        loadingDialog.startAnimationDialog();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users");
        userId=firebaseUser.getUid();
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile=snapshot.getValue(User.class);
                if(userProfile!=null){
                    Default_Email_Address=userProfile.email;
                    loadingDialog.closingAlertDialog();
                }else{
                    DisplayToast("your data not exit.");
                    loadingDialog.closingAlertDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOfficeMail=true;
                if(isOfficeMail){
                    if(checkIfEmailVerified()){
                        DisplayToast("verified with mail");
                    }
                }

            }
        });
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayToast("new email page is goging to redirect");
            }
        });
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailResetPassword();
            }
        });

    }
    void DisplayToast(String msg){
        Toast.makeText(SettingActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
    public void sendEmailResetPassword(){
        loadingDialog.startAnimationDialog();
        FirebaseAuth.getInstance().sendPasswordResetEmail(Default_Email_Address).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(Profile.this, "Send Reset Password Link to "+Default_Email_Address, Toast.LENGTH_SHORT).show();
                    DisplayToast("send reset password link to "+Default_Email_Address);
                    loadingDialog.closingAlertDialog();
                }else{

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
                            //Toast.makeText(Profile.this,"Sent Verification Email",Toast.LENGTH_LONG).show();
                            DisplayToast("sent verification email");
                            loadingDialog.closingAlertDialog();
                        }
                        else
                        {
                            loadingDialog.closingAlertDialog();
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
            //Toast.makeText(Profile.this, "Already Verified!!!", Toast.LENGTH_SHORT).show();
            DisplayToast("already verified");
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

                                            DisplayToast("already used email or unsuccessfully update contact to admin for information");
                                        }
                                    }
                                });

                    }
                });
    }
}