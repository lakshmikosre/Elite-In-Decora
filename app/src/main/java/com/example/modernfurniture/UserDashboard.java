package com.example.modernfurniture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserDashboard  extends Activity {
ImageView chair,lamp,table,tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userdashboard);

        chair=(ImageView)findViewById(R.id.chair);
        chair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(UserDashboard.this,DetailsPage.class);
                i1.putExtra("name","chair");
                i1.putExtra("cost","1000/per piece");
                i1.putExtra("type","Wooden");
                i1.putExtra("image","@drawable/chair");
                startActivity(i1);
            }
        });

        lamp=(ImageView)findViewById(R.id.lamp);
        lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(UserDashboard.this,DetailsPage.class);
                i1.putExtra("name","Lamp");
                i1.putExtra("cost","900/per piece");
                i1.putExtra("type","Electronics");
                i1.putExtra("image","@drawable/lamp");
                startActivity(i1);
            }
        });

        table=(ImageView)findViewById(R.id.table);
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(UserDashboard.this,DetailsPage.class);
                i1.putExtra("name","Table");
                i1.putExtra("cost","1200/per piece");
                i1.putExtra("type","Wooden");
                i1.putExtra("image","@drawable/table");
                startActivity(i1);
            }
        });

        tv=(ImageView)findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1= new Intent(UserDashboard.this,DetailsPage.class);
                i1.putExtra("name","Table TV");
                i1.putExtra("cost","19000/per piece");
                i1.putExtra("type","Electronice");
                i1.putExtra("image","@drawable/odltv");
                startActivity(i1);
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.gallery);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.wishlist:
                        startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.camera:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.cart:
                        startActivity(new Intent(getApplicationContext(),cart.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.gallery:
                        return true;
                }
                return false;
            }
        });
    }
}
