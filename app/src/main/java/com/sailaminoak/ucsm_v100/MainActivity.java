package com.sailaminoak.ucsm_v100;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.firebase.messaging.FirebaseMessaging;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.luseen.spacenavigation.SpaceOnLongClickListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment = null;
    private FrameLayout frameLayout;
    private SpaceNavigationView spaceNavigationView;
    SharedPreferences sharedPreferences;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        getFirebaseMessagingToken();
        FirebaseMessagingService firebaseMessagingService=new FirebaseMessagingService();
        sharedPreferences =this.getSharedPreferences("UCSM", Context.MODE_PRIVATE);
        int num=sharedPreferences.getInt("AlreadyLogIn",0);
        if(num==0){
            finish();
            Intent intent=new Intent(this,LoginPage.class);
            startActivity(intent);
        }else{

        }
        spaceNavigationView = findViewById(R.id.space);
        frameLayout=findViewById(R.id.container);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Content", R.drawable.contact));
        spaceNavigationView.addSpaceItem(new SpaceItem("Tops", R.drawable.medal));
        fragment=new Content();
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment,"content").commit();
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                //Toast.makeText(MainActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                FragmentManager fragmentManager=getSupportFragmentManager();
                if(itemIndex==0){
                    try{
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("firstpage")).commit();
                    }catch (Exception e){

                    }
                    if(fragmentManager.findFragmentByTag("content")!=null){
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("content")).commit();
                    } else{
                        fragmentManager.beginTransaction().add(R.id.container, new Content(), "content").commit();
                    }


                }else if(itemIndex==1){
                    try{
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("content")).commit();
                    }catch (Exception e){

                    }
                    if(fragmentManager.findFragmentByTag("firstpage")!=null){
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("firstpage")).commit();
                    } else{
                        fragmentManager.beginTransaction().add(R.id.container, new FirstPage(), "firstpage").commit();
                    }



                    spaceNavigationView.showBadgeAtIndex(1,99, getResources().getColor(R.color.colorAccent));
                }else if(itemIndex==2){
                   // Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                    fragmentManager.beginTransaction().add(R.id.container, new Content(), "content").commit();
                    spaceNavigationView.showBadgeAtIndex(2,99, getResources().getColor(R.color.colorAccent));
                }else{
                    //Toast.makeText(MainActivity.this, "3", Toast.LENGTH_SHORT).show();
                    spaceNavigationView.showBadgeAtIndex(3,99, getResources().getColor(R.color.colorAccent));

                    fragmentManager.beginTransaction().add(R.id.container, new FirstPage(), "firstpage").commit();
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, String.valueOf(itemIndex), Toast.LENGTH_SHORT).show();
            }
        });
        spaceNavigationView.setSpaceOnLongClickListener(new SpaceOnLongClickListener() {
            @Override
            public void onCentreButtonLongClick() {
                //Toast.makeText(MainActivity.this,"onCentreButtonLongClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(int itemIndex, String itemName) {
               // Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press one more time to exit UCSM", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

    }
    public void getFirebaseMessagingToken ( ) {
        FirebaseMessaging.getInstance ().getToken ()
                .addOnCompleteListener ( task -> {
                    if (!task.isSuccessful ()) {
                        //Could not get FirebaseMessagingToken
                        return;
                    }
                    if (null != task.getResult ()) {
                        //Got FirebaseMessagingToken
                        String firebaseMessagingToken = Objects.requireNonNull ( task.getResult () );
                        token=firebaseMessagingToken;
                        Log.d("token",token);
                        //Use firebaseMessagingToken further
                    }
                } );
    }
}