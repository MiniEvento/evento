package com.fireblaze.evento.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fireblaze.evento.SnackBarContainerInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public abstract class BaseActivity extends AppCompatActivity implements SnackBarContainerInterface {

    private ProgressDialog mProgressDialog;

    //Connection States
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private static Snackbar internetStatusSnack;
    private static boolean isConnected = false;
    private boolean exit = false;

    public void showProgressDialog(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog(){
        if (mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }

    }

    public FirebaseUser getUser(){
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void registerInternetCheckReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(broadcastReceiver,intentFilter);


    }
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = getConnectivityStatus(getBaseContext());
            setSnackBarMessage(status, getContainer());
        }
    };

    public static String getConnectivityStatusString(Context context){
        int conn = getConnectivityStatus(context);
        String status;
        if(conn == TYPE_WIFI)
            status = "Wifi Enabled";
        else if(conn == TYPE_MOBILE)
            status = "Mobile Data Enabled";
        else
            status= "Not connected to internet";

        return status;
    }

    public static int getConnectivityStatus(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;

        }
        return TYPE_NOT_CONNECTED;
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        //Log.d(TAG, "onOptionsItemSelected: logout success");
    }
    private void setSnackBarMessage(int status,View view){
        String internetStatus;
        int length;
        if(status == TYPE_WIFI || status == TYPE_MOBILE) {
            if(isConnected)
                return;
            length = Snackbar.LENGTH_SHORT;
            internetStatus = "Internet Connected";
            isConnected = true;

        } else {
            length = Snackbar.LENGTH_INDEFINITE;
            internetStatus = "Lost Internet Connection";
            isConnected = false;
        }

        internetStatusSnack = Snackbar.make(view,internetStatus,length)
            .setAction("X", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    internetStatusSnack.dismiss();
                }
            });
        internetStatusSnack.setActionTextColor(Color.WHITE);
        internetStatusSnack.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    public abstract View getContainer();

    public void exitApp(){
        if(exit){
            finish();
        }else {
            exit = true;
            Toast.makeText(this,"Press back again to exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            },3*1000);
        }
    }

}
