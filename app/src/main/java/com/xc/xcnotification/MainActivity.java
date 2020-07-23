package com.xc.xcnotification;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xc.xcnotification.XCNotification.XCNotificationCenter;
import com.xc.xcnotification.XCNotification.XCNotificationConstant;
import com.xc.xcnotification.XCNotification.XCNotificationName;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /// 添加对通知监听
    private void addXCNotificationObservers() {
        XCNotificationCenter.defaultCenter().add(
                this,
                XCNotificationName.name(XCNotificationConstant.kEvent1),
                this,
                (notification) -> {
                    System.out.println("received notification");
                });
    }

    /// 发送通知
    private void postXCNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                XCNotificationCenter.defaultCenter().post(
                        XCNotificationName.name(XCNotificationConstant.kEvent1),
                        "aUserInfo",
                        MainActivity.this);
            }
        }, 8000);
    }
}
