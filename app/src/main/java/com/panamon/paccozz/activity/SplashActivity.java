package com.panamon.paccozz.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.panamon.paccozz.R;
import com.panamon.paccozz.common.SharedPref;
import com.panamon.paccozz.common.Singleton;

public class SplashActivity extends AppCompatActivity {

    private Thread startThread;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPref = new SharedPref(this);

        Runnable threadJob = new MyRunnable();
        startThread = new Thread(threadJob);
        startThread.start();
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            try {
                startThread.sleep(2000);
                gotoScreen();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void gotoScreen() {
        if (sharedPref.getIsLogged()) {
            Singleton.getInstance().UserId = sharedPref.getUserId();
            Singleton.getInstance().UserName = sharedPref.getUserName();
            Singleton.getInstance().UserEmail = sharedPref.getUserEmail();
            Singleton.getInstance().UserMobile = sharedPref.getUserMobile();
            Singleton.getInstance().ParkId = sharedPref.getParkId();
            Singleton.getInstance().ParkName = sharedPref.getParkName();
            Singleton.getInstance().WalletAmount = sharedPref.getWalletAmount();
            Singleton.getInstance().ProfileImage = sharedPref.getProfileImage();
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            finish();
        } else {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        }
    }
}
