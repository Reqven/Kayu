package com.reqven.kayu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomeFragment.FragmentHomeListener, HistoryFragment.FragmentHistoryListener, AccountFragment.FragmentAccountListener {

    private TextView mTextMessage;
    private AppCompatButton product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        product = (AppCompatButton) findViewById(R.id.button);

        final HomeFragment fragmentHome       = new HomeFragment();
        final HistoryFragment fragmentHistory = new HistoryFragment();
        final AccountFragment fragmentAccount = new AccountFragment();

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ProductActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        mTextMessage.setText(R.string.title_home);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragmentHome)
                                .commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_history:
                        mTextMessage.setText(R.string.title_history);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragmentHistory)
                                .commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_account:
                        mTextMessage.setText(R.string.title_account);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragmentAccount)
                                .commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentHome)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public void onInputHomeSent(CharSequence input) {

    }
    @Override
    public void onInputAccountSent(CharSequence input) {

    }
    @Override
    public void onInputHistorySent(CharSequence input) {

    }
}
