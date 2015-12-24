package com.cardinalblue.singsomething;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                startFriendsList();
            }
        }.start();
    }

    private void startFriendsList() {
        Intent intent = new Intent(getApplicationContext(), OldFriendsListActivity.class);
        startActivity(intent);
        finish();
    }
}
