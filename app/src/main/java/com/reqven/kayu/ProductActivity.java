package com.reqven.kayu;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends AppCompatActivity{
    private ActionBar actionBar;
    private Toolbar toolBar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

        toolBar   = findViewById(R.id.main_toolbar);
        tabLayout = findViewById(R.id.main_tabs);
        toolBar.setTitle("Product title");
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_subject_24px));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_baseline_local_dining_24px));
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
