package com.e.softwaredesign;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class FingerPrintHandler extends FingerprintManager.AuthenticationCallback {
    private Context appContext;
    private AppCompatActivity baseActivity;

    private int i_AuthCount;
    private boolean b_Authfingerprint;
    private String str_Result;

    public void setAuthCount(int authCount) {
        i_AuthCount = authCount;
    }

    public FingerPrintHandler(Context context){
        this.appContext = context;
    }

    public FingerPrintHandler(Context context, AppCompatActivity activity,String result) {
        this.b_Authfingerprint=false;
        this.appContext = context;
        this.baseActivity = activity;
        this.str_Result = result;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject){
        CancellationSignal cancellationSignal = new CancellationSignal();
        manager.authenticate(cryptoObject,cancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(appContext, "Authentication error\n"+ errString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        Toast.makeText(appContext,"Authenticaion help\n"+helpString,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(appContext,"등록되지 않은 지문입니다.",Toast.LENGTH_LONG).show();
        i_AuthCount++;

        if(i_AuthCount>=4){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(appContext,"인식을 4번 틀리셨습니다.",Toast.LENGTH_SHORT).show();
                        baseActivity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },300);
        }
     }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(appContext,"Authentication success\n" + result, Toast.LENGTH_LONG).show();
        b_Authfingerprint=true;

        if(b_Authfingerprint){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent fingerAuthIntent = new Intent(baseActivity,ResultActivity.class);
                        fingerAuthIntent.putExtra("result",str_Result);
                        baseActivity.startActivity(fingerAuthIntent);
                        baseActivity.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 300);
        }
    }
}
