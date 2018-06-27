package com.reqven.kayu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements HomeFragment.FragmentHomeListener, HistoryFragment.FragmentHistoryListener, AccountFragment.FragmentAccountListener, NavigationView.OnNavigationItemSelectedListener {

    HomeFragment fragmentHome;
    HistoryFragment fragmentHistory;
    AccountFragment fragmentAccount;

    /*Bluetooth related declarations*/
    private static final String TAG = "SEX";
    private ArrayList<BluetoothDevice> devices;
    private ArrayList scans_list = new ArrayList();
    private ListView listView;
    private ListView scans;
    private Switch toggle;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private StringBuilder recDataString = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        downloadFiles();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Map<String, ?> settings = prefs.getAll();
        settings.isEmpty();

        PreferencesActivity.retrieveUserPreferences(getApplicationContext());

        fragmentHome    = new HomeFragment();
        fragmentHistory = new HistoryFragment();
        fragmentAccount = new AccountFragment();

        Toolbar toolBar = findViewById(R.id.toolbar);
        toolBar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolBar);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragmentHome)
                                .commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragmentHistory)
                                .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInputHistorySent("test");
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentHome)
                .commit();

        /* Bluetooth related code */

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                devices = new ArrayList<>();
                devices.addAll(pairedDevices);
                fragmentHome.setPairedDevices(devices);
            }
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onInputHomeSent(CharSequence input) {
        Log.d(TAG, myUUID.toString());
    }
    public void connectDevice(BluetoothDevice device) {
        ConnectThread mConnectThread = new ConnectThread(device);
        mConnectThread.start();
    }

    @Override
    public void setPairedDevices(ArrayList devices) {
        //fragmentHome.setPairedDevices(devices);
        Log.d(TAG, "setPairedDevices from MainActivity");
    }


    @Override
    public void onInputAccountSent(CharSequence input) {

    }
    @Override
    public void onInputHistorySent(CharSequence input) {
        fragmentHistory.addItem(input);
    }


    private void downloadFiles() {
        final String url = "https://api.myjson.com/bins/w2i9q";
        final String filename = "additives.json";

        try {
            InputStream inputStream = getApplicationContext().openFileInput(filename);
            inputStream.close();
            Toast.makeText(getApplicationContext(), "file " + filename + " exist", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "file " + filename + " doesnt exist", Toast.LENGTH_SHORT).show();

            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
                        outputStreamWriter.write(response.toString());
                        outputStreamWriter.close();
                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }


    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {

            }
            mmSocket = tmp;
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();
            try
            {
                mmSocket.connect();
            }
            catch (IOException connectException) {
                try
                {
                    mmSocket.close();
                } catch (IOException closeException) {

                }
                return;
            }
            final ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
            mConnectedThread.start();

            mConnectedThread.write("1".getBytes());
            mConnectedThread.write("2".getBytes());

            /*
            toggle.setEnabled(true);
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mConnectedThread.write("1".getBytes());
                    } else {
                        mConnectedThread.write("2".getBytes());
                    }
                }
            });
            */
            //mConnectedThread.read();

        }

        public void cancel() {
            try
            {
                mmSocket.close();
            } catch (IOException e) {

            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {

            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            mmBuffer = new byte[1024];
            int numBytes;

            while(true) {
                try {
                    int bytesAvailable = mmInStream.available();
                    if (bytesAvailable > 0) {
                        Log.d(TAG, "while ?");
                        // Read from the InputStream.
                        numBytes = mmInStream.read(mmBuffer);
                        String readMessage = new String(mmBuffer, 0, numBytes);
                        mHandler.obtainMessage(3, numBytes, -1, readMessage).sendToTarget();

                        // Send the obtained bytes to the UI activity.
                        //Message readMsg = mHandler.obtainMessage(3, numBytes, -1, mmBuffer);
                        //readMsg.sendToTarget();
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {

            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {

            }
        }
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //byte[] writeBuf = (byte[]) msg.obj;
            int begin = msg.arg1;
            int end = msg.arg2;
            switch(msg.what) {
                case 1:
                    //String writeMessage = new String(writeBuf);
                    //writeMessage = writeMessage.substring(begin, end);
                    //Log.d(TAG, "begin:" + begin + " | end:" + end + " | Message: " + writeMessage);
                    break;
                case 3:
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);

                    String last = recDataString.toString().substring(recDataString.toString().length() - 1);
                    //Log.d(TAG, "msg: " + readMessage + " | last: " + last);

                    if (last.equals("}")) {
                        String barcode = recDataString.toString();
                        barcode = barcode.replaceAll("[{}]", " ");
                        Toast.makeText(getBaseContext(), barcode, Toast.LENGTH_LONG).show();

                        fragmentHistory.addItem(barcode);
                        //scans_list.add(barcode);
                        //ArrayAdapter adapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, scans_list);
                        //scans.setAdapter(adapter);

                        recDataString.setLength(0);
                        recDataString = new StringBuilder();
                    }
            }
            return false;
        }
    });


}
