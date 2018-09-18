package com.bms.user.bmssmartwatch;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import message.SMSArrayAdapter;

import static android.content.ContentValues.TAG;

public class SMSFragment extends DialogFragment{

    private String LOG_TAG = "debugdata";
    private  View mRootView;
    ArrayList<String> listSMS;
    ListView listViewSMS;

    String sms;
    public void setValue(String sms) {
        this.sms = sms;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_sms,container,false);

        listViewSMS = (ListView) mRootView.findViewById(R.id.listViewSMS);
        Button btnClose = (Button) mRootView.findViewById(R.id.btnCloseSMS);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Button Close");
                dismiss();
            }
        });

        listSMS = new ArrayList<>();
        listSMS.clear();
        listSMS.add(sms);
        Log.d(LOG_TAG, "list.size:" + listSMS.size());
        SMSArrayAdapter adapter = new SMSArrayAdapter(mRootView.getRootView().getContext(), listSMS);
        adapter.replaceData(listSMS);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "list.adapter:" + adapter.getCount());
        listViewSMS.setAdapter(adapter);

        return mRootView;
    }


}

