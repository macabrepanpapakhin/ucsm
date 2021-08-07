package com.sailaminoak.ucsm_v100;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegisterPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText editTextMkpt;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
    EditText editTextConfirmedPassword;
    LoadingDialog loadingDialog;
    boolean testRegister=false;
    CircularProgressButton circularProgressButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        loadingDialog=new LoadingDialog(RegisterPage.this);
        changeStautsBarColor();
        mAuth = FirebaseAuth.getInstance();
        editTextMkpt=findViewById(R.id.editTextMkpt);
        editTextName=findViewById(R.id.editTextName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        editTextConfirmedPassword=findViewById(R.id.editTextConfirmedPassword);
        circularProgressButton=findViewById(R.id.cirRegisterButton);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                        loadingDialog.startAnimationDialog();
                        String result="";
                        result=registerValidation(editTextMkpt.getText().toString().trim(),
                        editTextName.getText().toString().trim(),
                        editTextEmail.getText().toString().trim(),
                        editTextPassword.getText().toString().trim(),
                        editTextConfirmedPassword.getText().toString().trim());
                        String mkpt=editTextMkpt.getText().toString().trim();
                        String name=editTextName.getText().toString().trim();
                        String email=editTextEmail.getText().toString().trim();
                        String password=editTextPassword.getText().toString().trim();
                        RegisterValidation();
                        if(testRegister){
                            //Toast.makeText(RegisterPage.this,"Operating..Please wait",Toast.LENGTH_SHORT).show();
                            mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString().trim(),editTextPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        String username="username";
                                        String major="se";
                                        String batch="2016";
                                        String participatedClub="Music Club,Tech Club";
                                        String verifyOrNot="true";
                                        User user=new User(editTextMkpt.getText().toString(),username,editTextName.getText().toString(),editTextEmail.getText().toString(),major,batch,editTextPassword.getText().toString(),"Under Review hehe","no bio yet",participatedClub,"Image will be placed",verifyOrNot);
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        database.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(RegisterPage.this,"Successfully Register to UCSM",Toast.LENGTH_SHORT).show();
                                                    loadingDialog.closingAlertDialog();
                                                }else{
                                                    Toast.makeText(RegisterPage.this,"Error Registration: Already Register with that Email!!!",Toast.LENGTH_SHORT).show();
                                                    loadingDialog.closingAlertDialog();
                                                }
                                            }
                                        });

                                    }else{
                                        Toast.makeText(RegisterPage.this,"Cannot Register That Time ",Toast.LENGTH_SHORT).show();
                                        loadingDialog.closingAlertDialog();
                                    }
                                }
                            });



                        }else{
                            Toast.makeText(RegisterPage.this,result,Toast.LENGTH_SHORT).show();
                            loadingDialog.closingAlertDialog();
                        }


            }


        });

    }
    public void onLoginClick(View view){
        Intent intent=new Intent(RegisterPage.this,LoginPage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
       finish();
    }
    public void changeStautsBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public void RegisterValidation(){
        if(editTextMkpt.getText().toString().trim().length()==0)
        {
            editTextMkpt.setError("MKPT is  require!");
            editTextMkpt.requestFocus();
            return;
        }
        if(editTextName.getText().toString().trim().length()==0)
        {
            editTextName.setError("Name is  require!");
            editTextName.requestFocus();
            return;
        }
        if(editTextEmail.getText().toString().trim().length()==0)
        {
            editTextMkpt.setError("Email is  require!");
            editTextMkpt.requestFocus();
            return;

        }
        if(editTextPassword.getText().toString().trim().length()==0)
        {
            editTextMkpt.setError("Password is  require!");
            editTextMkpt.requestFocus();
            return;
        }
        if(editTextConfirmedPassword.getText().toString().trim().length()==0)
        {
            editTextMkpt.setError("Confirmed Password is  require!");
            editTextMkpt.requestFocus();
            return;
        }
        if(!editTextPassword.getText().toString().trim().contentEquals(editTextConfirmedPassword.getText().toString().trim())){
            editTextConfirmedPassword.setError("Password and Confirmed Password are not the same");
            editTextConfirmedPassword.requestFocus();
            return;
        }




    }
    public String  registerValidation(String mkpt,String name,String email,String password,String confirmedPassword){
        if(mkpt.length()==0 || name.length()==0 || email.length()==0 || password.length()==0 || confirmedPassword.length()==0){
            return "Make Sure Every Field Fills";
        }
        if(!password.contentEquals(confirmedPassword)){
            return "Password and Confirmed Password Not Equal";
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
           return "Invalid Email Address";

        }
        if(password.length()<8){
            return "Password Have To Be More Than 8 Letters";
        }
        testRegister=true;
        return "Success";
    }
    private void DisplayToast(String string){
        Toast.makeText(RegisterPage.this,string,Toast.LENGTH_SHORT).show();
    }
}
