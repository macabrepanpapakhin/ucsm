package com.sailaminoak.ucsm_v100;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog alertDialog;
    boolean cancelable=true;
    public LoadingDialog(Activity activity){
        this.activity=activity;
    }
    public void startAnimationDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(cancelable);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void closingAlertDialog(){
        alertDialog.dismiss();
    }
    public LoadingDialog(Activity activity,boolean cancelable){
        this.activity=activity;
        this.cancelable=cancelable;

    }
}
