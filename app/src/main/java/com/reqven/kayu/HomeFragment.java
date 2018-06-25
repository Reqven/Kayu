package com.reqven.kayu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class HomeFragment extends Fragment {
    private FragmentHomeListener listener;

    /* Bluetooth related declarations */
    private static final String TAG = "SEX";
    private ArrayList<BluetoothDevice> pairedDevices;
    private ArrayList paired_devices_list = new ArrayList();
    private StringBuilder recDataString = new StringBuilder();

    public interface FragmentHomeListener {
        void onInputHomeSent(CharSequence input);
        void setPairedDevices(ArrayList devices);
        void connectDevice(BluetoothDevice device);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProductActivity.class);
                startActivity(intent);
            }
        });
        ListView listView = view.findViewById(R.id.listview);
        Switch toggle = view.findViewById(R.id.switch1);

        final ArrayAdapter adapter = new ArrayAdapter(getContext() ,android.R.layout.simple_list_item_1, paired_devices_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);
                Toast.makeText(getContext(), info, Toast.LENGTH_LONG).show();

                BluetoothDevice device = pairedDevices.get(position);
                listener.connectDevice(device);
            }
        });



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentHomeListener) {
            listener = (FragmentHomeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentHomeListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void setPairedDevices(ArrayList<BluetoothDevice> devices) {
        pairedDevices = devices;
        for (BluetoothDevice device : pairedDevices) {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress(); // MAC address
            paired_devices_list.add(deviceName + "\n" + deviceHardwareAddress);
        }
        //adapter.notifyItemInserted(codes.size() - 1);
    }
}
