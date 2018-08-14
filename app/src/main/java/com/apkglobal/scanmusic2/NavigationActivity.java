package com.apkglobal.scanmusic2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NavigationActivity extends AppCompatActivity {

    Button btn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        btn= findViewById(R.id.button);
        drawerLayout= findViewById(R.id.drawerlayout);
        navigationView= findViewById(R.id.navigaionView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.import2:
                        item.setChecked(true);
                        Toast.makeText(NavigationActivity.this, "Import Selected" , Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return  true;

                }
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              gotonext();
            }
        });
    }

    private void gotonext()
    {
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M&&checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        else
        {
            Intent intent=new Intent(NavigationActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
