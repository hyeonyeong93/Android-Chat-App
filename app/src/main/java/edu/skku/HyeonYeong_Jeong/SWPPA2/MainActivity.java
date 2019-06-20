package edu.skku.HyeonYeong_Jeong.SWPPA2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            intent = new Intent(MainActivity.this, ChatListActivity.class);
            intent.putExtra("STD_NUM", SaveSharedPreference.getUserName(this));
            startActivity(intent);
            this.finish();
        }




    }
}
