package com.reqven.kayu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

        fragmentHome       = new HomeFragment();
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
                                .replace(R.id.fragment_container, fragmentAccount)
                                .commit();
                        break;
                    case 2:
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInputHistorySent("test");
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentHome)
                .commit();

        /* Bluetooth related code */

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            devices = new ArrayList<>();
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                //paired_devices_list.add(device.getName() + "\n" + device.getAddress());
                devices.add(device);
                //mDevice = device;
            }
            fragmentHome.setPairedDevices(devices);
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.

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


            /*
            byte[] buffer = new byte[1024];
            mmBuffer = new byte[1024];
            int begin = 0;
            int bytes = 0;
            int numBytes;
            while (true) {
                try {
                    numBytes = mmInStream.read(mmBuffer);
                    Log.d(TAG, String.valueOf(numBytes));
                    Log.d(TAG, )

                    Integer p = mmInStream.read();
                    bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);

                    Log.d(TAG, "{ = " + String.valueOf("{".getBytes()[0]));
                    Log.d(TAG, "} = " + String.valueOf("}".getBytes()[0]));

                    for(int i = begin; i < bytes; i++) {

                        if(buffer[i] == "}".getBytes()[0]) {
                            mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
                            begin = i + 1;
                            if(i == bytes - 1) {
                                bytes = 0;
                                begin = 0;
                            }
                        }
                    }
                } catch (IOException e) {
                    break;
                }
            }*/
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
            int begin = (int)msg.arg1;
            int end = (int)msg.arg2;
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
