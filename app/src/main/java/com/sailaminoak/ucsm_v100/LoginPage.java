package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginPage extends AppCompatActivity {
private FirebaseAuth mAuth;
private EditText editTextEmail,editTextPassword;
SharedPreferences sharedPreferences;
private CircularProgressButton circularProgressButton;
LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login_page);
        sharedPreferences =this.getSharedPreferences("UCSM", Context.MODE_PRIVATE);

        loadingDialog=new LoadingDialog(LoginPage.this);
        mAuth=FirebaseAuth.getInstance();
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        circularProgressButton=findViewById(R.id.cirLoginButton);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startAnimationDialog();
                userLogin();
            }
        });


    }
    public void userLogin(){
        String email=editTextEmail.getText().toString().trim();
        if(email.length()==0){
            editTextEmail.setError("Invalid Email Password!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Invalid Email Password!");
            editTextEmail.requestFocus();
            return;
        }

        String password=editTextPassword.getText().toString().trim();
        if(password.length()==0){

            editTextPassword.setError("Invalid Password!");
            editTextPassword.requestFocus();
            return;
        }
       mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                       loadingDialog.closingAlertDialog();
                       Intent intent=new Intent(LoginPage.this,MainActivity.class);
                       startActivity(intent);
                       SharedPreferences.Editor editor=sharedPreferences.edit();
                       editor.putInt("AlreadyLogIn",11);
                       editor.apply();
                       overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                       finish();
               }else{
                   loadingDialog.closingAlertDialog();
                   Toast.makeText(LoginPage.this,"Incorrect Email Address or Password",Toast.LENGTH_LONG).show();
               }
           }
       });


    }
    public void onLoginClick(View view){
        Intent intent=new Intent(LoginPage.this,RegisterPage.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }


}